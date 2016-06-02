package org.visallo.web.embed;

import com.v5analytics.webster.Handler;
import org.visallo.core.model.Description;
import org.visallo.core.model.Name;
import org.visallo.web.WebApp;
import org.visallo.web.WebAppPlugin;

import javax.servlet.ServletContext;

@Name("Embed External")
@Description("Add Dashboard card that can embed external content")
public class EmbedExternalWebAppPlugin implements WebAppPlugin {
    @Override
    public void init(WebApp app, ServletContext servletContext, Handler authenticationHandler) {
        app.registerJavaScript("/org/visallo/web/embed/embedPlugin.js");
        app.registerJavaScript("/org/visallo/web/embed/embed.js", false);
        app.registerJavaScript("/org/visallo/web/embed/configure.js", false);
        app.registerJavaScriptTemplate("/org/visallo/web/embed/configureTpl.hbs");

        app.registerLess("/org/visallo/web/embed/embed.less");
    }
}
