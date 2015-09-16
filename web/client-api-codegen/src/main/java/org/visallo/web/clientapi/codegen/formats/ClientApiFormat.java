package org.visallo.web.clientapi.codegen.formats;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import com.github.jknack.handlebars.io.ClassPathTemplateLoader;
import com.github.jknack.handlebars.io.TemplateLoader;
import com.v5analytics.webster.Route;
import org.apache.commons.io.FileUtils;
import org.visallo.core.exception.VisalloException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class ClientApiFormat {
    public void write(Map<Route.Method, List<Route>> routesMap, File outDir) {
        Map<String, List<EndPoint>> categoriesEndPoints = new HashMap<>();
        for (Map.Entry<Route.Method, List<Route>> routeEntry : routesMap.entrySet()) {
            Route.Method method = routeEntry.getKey();
            List<Route> routes = routeEntry.getValue();
            for (Route route : routes) {
                EndPoint endPoint = new EndPoint(method, route);
                if (!shouldIncludeEndPoint(endPoint)) {
                    continue;
                }
                List<EndPoint> endPoints = categoriesEndPoints.get(endPoint.getCategory());
                if (endPoints == null) {
                    endPoints = new ArrayList<>();
                    categoriesEndPoints.put(endPoint.getCategory(), endPoints);
                }
                endPoints.add(endPoint);
            }
        }
        writeCategoriesEndPoints(categoriesEndPoints, outDir);
    }

    private boolean shouldIncludeEndPoint(EndPoint endPoint) {
        String category = endPoint.getCategory();
        if (category.equals("vertex")
                || category.equals("edge")
                || category.equals("workspace")
                || category.equals("ontology")
                || category.equals("user")
                || category.equals("long-running-process")
                || category.equals("admin")) {
            return true;
        }
        return false;
    }

    protected void writeCategoriesEndPoints(Map<String, List<EndPoint>> categoriesEndPoints, File outDir) {
        for (Map.Entry<String, List<EndPoint>> categoryEndPoints : categoriesEndPoints.entrySet()) {
            writeCategoryEndPoints(categoryEndPoints.getKey(), categoryEndPoints.getValue(), outDir);
        }
    }

    protected abstract void writeCategoryEndPoints(String category, List<EndPoint> endPoints, File outDir);

    protected void writeTemplate(String templatePath, String templateName, Map<String, Object> context, File outFile) {
        String templateResult = runTemplate(templatePath, templateName, context);
        try {
            FileUtils.writeStringToFile(outFile, templateResult);
        } catch (IOException ex) {
            throw new VisalloException("Could not write template: " + templatePath + " " + templateName);
        }
    }

    protected String runTemplate(String templatePath, String templateName, Map<String, Object> context) {
        try {
            TemplateLoader templateLoader = new ClassPathTemplateLoader(templatePath);
            Handlebars handlebars = new Handlebars(templateLoader);
            handlebars.setPrettyPrint(true);
            Template template = handlebars.compile(templateName);
            return template.apply(context);
        } catch (IOException ex) {
            throw new VisalloException("Could not run template: " + templatePath + " " + templateName, ex);
        }
    }

    public static class EndPoint {
        private final String category;
        private final String function;
        private final Route.Method method;
        private final Route route;

        public EndPoint(Route.Method method, Route route) {
            this.method = method;
            this.route = route;

            String routePath = route.getPath();
            if (routePath.startsWith("/")) {
                routePath = routePath.substring(1);
            }
            int firstSlash = routePath.indexOf('/');
            if (firstSlash > 0) {
                this.category = routePath.substring(0, firstSlash);
                this.function = routePath.substring(firstSlash + 1);
            } else {
                this.category = routePath;
                this.function = "";
            }
        }

        public String getCategory() {
            return category;
        }

        public String getFunction() {
            return function;
        }

        public Route.Method getMethod() {
            return method;
        }

        public Route getRoute() {
            return route;
        }
    }
}
