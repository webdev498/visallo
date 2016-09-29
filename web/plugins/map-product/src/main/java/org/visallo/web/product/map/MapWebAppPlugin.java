package org.visallo.web.product.map;

import com.v5analytics.webster.Handler;
import org.visallo.web.WebApp;
import org.visallo.web.WebAppPlugin;

import javax.servlet.ServletContext;

public class MapWebAppPlugin implements WebAppPlugin {
    @Override
    public void init(WebApp app, ServletContext servletContext, Handler authenticationHandler) {

        app.registerJavaScript("/org/visallo/web/product/map/plugin.js");
        app.registerJavaScript("/org/visallo/web/product/map/multiPointCluster.js", false);
        app.registerJavaScriptComponent("/org/visallo/web/product/map/MapContainer.jsx");
        app.registerJavaScriptComponent("/org/visallo/web/product/map/Map.jsx");
        app.registerJavaScriptComponent("/org/visallo/web/product/map/OpenLayers.jsx");

        app.registerResourceBundle("/org/visallo/web/product/map/messages.properties");
    }
}
