# New Visallo Archetype Project

This tutorial will show you how to use the Visallo
[Maven Archetype](https://maven.apache.org/guides/introduction/introduction-to-archetypes.html)
to create a sample project that includes example web
and Graph Propery Worker plugins.

The sample project will include:

* A web authentication plugin that logs you in as long as your username and password are the same.
* A Graph Property Worker that extracts person names from a `.csv` file that is imported.
* A web plugin which adds the ability to Google a person concept's name from the details pane of that entity inside of Visallo.

## Prerequisites

1. Install the required [devleopment dependencies](../dependencies.md).

## Generate Project

Run the following command in a directory under which you would like to
create a new project directory:

```bash
        mvn archetype:generate -DarchetypeGroupId=com.visallo \
                               -DarchetypeArtifactId=visallo-plugin-archetype
```

Maven will prompt you for several values:

* for `groupId`, enter **`com.visalloexample.helloworld`**
* for `artifactId`, enter **`visallo-helloworld`**
* accept the defaults for `version`, `package`, and `ontologyBaseIri`

Maven will create files for the new project in a subdirectory with
the same name that you provided for `artifactId`.

## Build

With the new project files created we need to build the project.

First change into the new directory and then run `mvn package`:

```bash
        cd visallo-helloworld
        mvn package
```

Maven will compile and package the project files including downloading
many files comprising Visallo and our dependencies which may take
several minutes. Watch for an `[INFO] BUILD SUCCESS` message.

## Run

After Maven finishes packaging you are ready to run Visallo with the
example web plugin and Graph Property Worker and using embedded H2
and Elasticsearch datastores.

```bash
        ./run.sh
```

Watch for a `[visallo.web.JettyWebServer] Listening on http port 8080
and https port 8443` message.

Browse to [https://localhost:8443](https://localhost:8443) and login
with any username and the same value as your password, e.g. `admin` and
`admin` (the example `visallo-helloworld-auth` authentication plugin has
been enabled).

## Explore the Examples

### Web Authentication Plugin

Since you were able to login with a username and matching password you
saw that the web authentication module works. Take a look at the
Java implementation in:

```
plugins/auth/src/main/java/com/visalloexample/helloworld/auth/Login.java
```

### Graph Property Worker

You can test the Graph Property Worker by importing an example `.csv` file:

1. Switch to the Graph by clicking on <img src="img/graph.png" height="22" width="22"/> `Graph` on the left side of the Visallo web page
1. Drag-and-drop the example file, `plugins/worker/src/test/resources/contacts.csv`, onto the Graph
1. Click **Import** in the dialog that pops up

Visallo will import the file and and add the vertex to the graph property
work queue. The configured Graph Property Worker will execute and create new
entities for `Bruce Wayne` and `Clark Kent` adding them to your graph.

Take a look at the Java implementation in:

```
plugins/worker/src/main/java/com/visalloexample/helloworld/worker/ExampleGraphPropertyWorker.java
```

### Web Plugin

You can test the web plugin by opening the detail pane for one of our
new entities:

1. Switch to the Graph by clicking on <img src="img/graph.png" height="22" width="22"/> `Graph` on the left side of the Visallo web page
1. Click on the `Bruce Wayne` entity and the detail pane should open
1. Click the **Google** button in the detail pane menu bar and a new
   window should open with Google search results for Bruce Wayne

Take a look at the Java and Javascript implementations in:

```
plugins/web/src/main/java/com/visalloexample/helloworld/web/ExampleWebAppPlugin.java
plugins/web/src/main/resources/com/visalloexample/helloworld/web/plugin.js
```
