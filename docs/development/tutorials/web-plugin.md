# New Visallo Web Plugin

This tutorial will show you how to add an item to the right-click context
menu for entities shown in search results and on the Graph. We will start
with the `visallo-helloworld` project created durring the
[New Archetype Project](archetype-project.md) tutorial.

## Prerequisites

1. Complete the [New Archetype Project](archetype-project.md) tutorial.

## Define a Web Plugin

The minimal Visallo web plugin is simply a Java class that implements
`org.visallo.web.WebAppPlugin` registered in a
`META-INF/services/org.visallo.web.WebAppPlugin` file.

Create a new Java file as shown below.

##### NEW `plugins/web/src/main/java/com/visalloexample/helloworld/web/TutorialWebAppPlugin.java`

```java
package com.visalloexample.helloworld.web;

import com.v5analytics.webster.Handler;
import org.visallo.core.model.Description;
import org.visallo.core.model.Name;
import org.visallo.web.WebApp;
import org.visallo.web.WebAppPlugin;

import javax.servlet.ServletContext;

@Name("Tutorial Web App Plugin")
@Description("http://docs.visallo.org/development/tutorials/web-plugin.html")
public class TutorialWebAppPlugin implements WebAppPlugin {
    @Override
    public void init(WebApp app, ServletContext servletContext, Handler handler) {
      // nothing here yet
    }
}
```

Edit the existing service locator file as shown below. It will already include a
line for the web plugin class created by the Maven archetype.

##### EDIT `plugins/web/src/main/resources/META-INF/services/org.visallo.web.WebAppPlugin`

```
com.visalloexample.helloworld.web.ExampleWebAppPlugin
com.visalloexample.helloworld.web.TutorialWebAppPlugin
```

## Test Registration

We can now rebuild the project and run the web application to test that
our new web plugin has been loaded. Run the following commands in the
`visallo-helloworld` project directory:

```bash
        mvn clean package
        ./run.sh
```

1. Browse to [https://localhost:8443](https://localhost:8443) and login with
any username and the same value as your password.

1. Open the Admin panel by clicking on <img src="img/admin.png" height="22" width="22"/> `Admin`.

1. Navigate to **Plugin | List > Web App Plugins** and look for: `Tutorial Web App Plugin`.

## Add JavaScript

Often a Visallo web plugin will contribute JavaScript to extend the
Visallo web interface. We need to create a new JavaScript file and
edit our new Java class to instruct Visallo load the JavaScript file.

Create the new JavaScript file as shown below.

##### NEW `plugins/web/src/main/resources/com/visalloexample/helloworld/web/tutorialVertexMenuEvent.js`

```javascript
require([
    'public/v1/api'
], function (
    api
) {
    'use strict';

    // register a menu extension with the specified label that
    // will fire a 'tutorialEvent' event when clicked
    api.registry.registerExtension('org.visallo.vertex.menu', {
        label: 'Tutorial Web Plugin',
        event: 'tutorialEvent'
    });

    // define a handler for the 'tutorialEvent' event
    $(document).on('tutorialEvent', function(e, data) {
        console.log('tutorialEvent fired', e, data);
        // nothing useful yet
    });
});
```

Edit the new Java file as show below, adding one line to register the
new JavaScript file.

##### EDIT `plugins/web/src/main/java/com/visalloexample/helloworld/web/TutorialWebAppPlugin.java`

```java
package com.visalloexample.helloworld.web;

import com.v5analytics.webster.Handler;
import org.visallo.core.model.Description;
import org.visallo.core.model.Name;
import org.visallo.web.WebApp;
import org.visallo.web.WebAppPlugin;

import javax.servlet.ServletContext;

@Name("Tutorial Web App Plugin")
@Description("http://docs.visallo.org/development/tutorials/web-plugin.html")
public class TutorialWebAppPlugin implements WebAppPlugin {
    @Override
    public void init(WebApp app, ServletContext servletContext, Handler handler) {
      app.registerJavaScript("/com/visalloexample/helloworld/web/tutorialVertexMenuEvent.js", true);
    }
}
```

## Test JavaScript

We can now rebuild the project and run the web application to test
our new context menu item. Run the following commands in the
`visallo-helloworld` project directory:

```bash
        mvn clean package
        ./run.sh
```

1. Browse to [https://localhost:8443](https://localhost:8443) and login with
any username and the same value as your password.

1. Open the JavaScript console in your web browser (`Ctrl-Option-J` for
Google Chrome on macOS).

1. Search for all entities in the system by clicking on <img src="img/find.png" height="22" width="22"/> `Find`,
type an asterix `*` in the text field and press `Enter`.

1. Right-click on a search result and then click **Tutoral Web Plugin** in
the context menu.

1. Look for a `tutorialEvent fired...` message in the JavaScript console.

## Add a Route

Next we will add a Java server-side route and update our JavaScript extension to call it.

We need to create a new Java class that extends `com.v5analytics.webster.ParameterizedHandler`
as shown below.

##### NEW `plugins/web/src/main/java/com/visalloexample/helloworld/web/TutorialEventPost.java`

```java
package com.visalloexample.helloworld.web;

import com.v5analytics.webster.ParameterizedHandler;
import com.v5analytics.webster.annotations.Handle;
import com.v5analytics.webster.annotations.Required;
import org.visallo.web.clientapi.model.ClientApiObject;
import org.visallo.web.clientapi.model.ClientApiSuccess;

public class TutorialEventPost implements ParameterizedHandler {
    @Handle
    public ClientApiObject handle(
        @Required(name = "vertexId") String vertexId
    ) {
        System.out.printf("WEB PLUGIN TUTORIAL - vertexId: %s\n", vertexId);
        return new ClientApiSuccess();
    }
}
```

In order for our JavaScript extension to interact with the Java web server
wihout impacting user interface performance we use a web worker service.

Create a new JavaScript file defining a `tutorialService` with a
`tutorialEvent` function as follows.

##### NEW `plugins/web/src/main/resources/com/visalloexample/helloworld/web/tutorialServiceWebWorker.js`

```javascript
define('data/web-worker/services/tutorialService', [
    'public/v1/workerApi'
], function(workerApi) {
    var ajax = workerApi.ajax;
    'use strict';
    return {
        tutorialEvent: function(vertexId) {
            return ajax('POST', '/tutorialEvent', { vertexId: vertexId });
        }
    }
})
```

Edit the first Java file as show below, adding two lines.
The first to register the new web worker JavaScript file.
The second to register the new Java file to service HTTP `POST` requests to `/tutorialEvent`.

##### EDIT `plugins/web/src/main/java/com/visalloexample/helloworld/web/TutorialWebAppPlugin.java`

```java
package com.visalloexample.helloworld.web;

import com.v5analytics.webster.Handler;
import org.visallo.core.model.Description;
import org.visallo.core.model.Name;
import org.visallo.web.VisalloCsrfHandler;
import org.visallo.web.WebApp;
import org.visallo.web.WebAppPlugin;

import javax.servlet.ServletContext;

@Name("Tutorial Web App Plugin")
@Description("http://docs.visallo.org/development/tutorials/web-plugin.html")
public class TutorialWebAppPlugin implements WebAppPlugin {
    @Override
    public void init(WebApp app, ServletContext servletContext, Handler handler) {
      app.registerJavaScript("/com/visalloexample/helloworld/web/tutorialVertexMenuEvent.js", true);
      app.registerWebWorkerJavaScript("/com/visalloexample/helloworld/web/tutorialServiceWebWorker.js");
      app.post("/tutorialEvent", handler.getClass(), VisalloCsrfHandler.class, TutorialEventPost.class);
    }
}
```

Edit the first JavaScript file to do more than log that the JavaScript
event fired. Add code to use the new web worker service as shown below.

##### EDIT `plugins/web/src/main/resources/com/visalloexample/helloworld/web/tutorialVertexMenuEvent.js`

```javascript
require([
    'public/v1/api'
], function (
    api
) {
    'use strict';

    // register a menu extension with the specified label that
    // will fire a 'tutorialEvent' event when clicked
    api.registry.registerExtension('org.visallo.vertex.menu', {
        label: 'Tutorial Web Plugin',
        event: 'tutorialEvent'
    });

    // define a handler for the 'tutorialEvent' event
    $(document).on('tutorialEvent', function(e, data) {
        console.log('tutorialEvent fired', e, data);
        api.connect().then(function(connectApi) {
            // invoke the tutorialService web worker's tutorialEvent function passing the vertexId
            connectApi
                .dataRequest('tutorialService', 'tutorialEvent', data.vertexId)
                .then(function(response) {
                    console.log('tutorialEvent response', response);
                });
        });
    });
});
```

## Test Route

We can once again rebuild the project and run the web application. This time
to test the web worker service and server-side route.  Run the following
commands in the `visallo-helloworld` project directory:

```bash
        mvn clean package
        ./run.sh
```

1. Browse to [https://localhost:8443](https://localhost:8443) and login with
any username and the same value as your password.

1. Open the JavaScript console in your web browser (`Option-Command-J` for
Google Chrome on macOS).

1. Search for all entities in the system by clicking on <img src="img/find.png" height="22" width="22"/> `Find`,
type an asterix `*` in the text field and press `Enter`.

1. Right-click on a search result and then click **Tutoral Web Plugin** in
the context menu.

1. Look for a `tutorialEvent fired...` message in the JavaScript console.

1. Look for a `WEB PLUGIN TUTORIAL..` message in the output from the
`./run.sh` command.

1. Look for a `tutorialEvent response...` message in the JavaScript console.

---

### Doing something with the data

Typically, you are going to want to do something on the back end, and then send the results to the front.  To demonstrate this concept, we will need to make a couple of changes to what happens in the route that we just made.  Change the ```SelectedVertexAction.java``` file to look like the following: 

```java
package com.visalloexample.helloworld.web;

import com.v5analytics.webster.ParameterizedHandler;
import com.v5analytics.webster.annotations.Handle;
import com.v5analytics.webster.annotations.Required;
import org.vertexium.Authorizations;
import org.vertexium.Graph;
import org.vertexium.Vertex;
import org.visallo.core.model.workQueue.WorkQueueRepository;
import org.visallo.web.clientapi.model.ClientApiObject;
import org.visallo.web.clientapi.model.ClientApiSuccess;
import org.visallo.web.parameterProviders.ActiveWorkspaceId;

import javax.inject.Inject;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SelectedVertexAction implements ParameterizedHandler {
    private Graph graph;
    private WorkQueueRepository repository;

    @Inject
    public SelectedVertexAction(Graph graph, WorkQueueRepository repository) {
        this.graph = graph;
        this.repository = repository;
    }

    @Handle
    public ClientApiObject handle(
            @Required(name = "vertexId") String vertexId,
            @ActiveWorkspaceId(required = false) String workspaceId,
            Authorizations authorizations
    ) {
        //get the vertex that was sent back from the front end
        Vertex v = graph.getVertex(vertexId, authorizations);

        //put the current date and time into the string
        String format = new SimpleDateFormat().format(new Date());

        //make a new title for the vertex
        final String newName = String.format("Action (%s)", format);

        //set the property on the vertex
        v.setProperty("http://example.org/visallo-helloworld-gpw#fullName", newName, v.getVisibility(), authorizations);

        // make sure that the changes are persisted into the graph
        this.graph.flush();

        //tell the workspace that the vertex has changed so it needs to be reloaded
        this.repository.broadcastElement(v, workspaceId);

        return new ClientApiSuccess();
    }
}
```

This code will now change the name of the vertex that the menu item was opened on to "Action (<the current time>)".  Note that, in this example, we didn't send the information back to the front-end through the web request.  We instead broadcasted a message to the front end that told it that the element that we clicked on had changed, and that it needed to reload it.  

Let's send the new title to the front end and have it create a simple javascript alert box.  At the end of the ```handle``` method, instead of returning the ClientApiSuccess object return: 

```java
        return new ClientApiObject() {
            public String getNewName(){
                return newName;
            }
        };
```

Now we are passing the message back to the front end instead of just passing back a success message.  Go into the ```selectedvertexplugin.js``` file and add the following instead of the console message:

```javascript
  alert("Vertex " + data + " title changed to " + response.title);
```

The new name of the element will be passed back to the front end and we can construct a javascript alert with it in there.

### Conclusion

So that was a tutorial that took you through the basic steps of creating a webapp plugin.  We created a web plugin that can pass data from the front end to the back end, and then send data in multiple ways back to the front end.  Using these simple concepts, it is possible to build some more of the complicated behaviors that makes Visallo customized for your organization.  
