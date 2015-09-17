package org.visallo.web.clientapi.codegen;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.converters.FileConverter;
import com.google.inject.Injector;
import com.v5analytics.webster.Route;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.visallo.core.bootstrap.InjectHelper;
import org.visallo.core.cmdline.CommandLineTool;
import org.visallo.web.Router;
import org.visallo.web.clientapi.codegen.formats.ClientApiFormat;
import org.visallo.web.clientapi.codegen.formats.JavaClientApiFormat;

import javax.servlet.ServletContext;
import java.io.File;
import java.util.List;
import java.util.Map;

public class ClientApiCodeGenerator extends CommandLineTool {
    @Parameter(names = {"--outdir"}, description = "Output direction", converter = FileConverter.class)
    private File outDir;

    public static void main(String[] args) throws Exception {
        int ret = new ClientApiCodeGenerator().run(args);
        System.exit(ret);
    }

    @Override
    protected int run() throws Exception {
        Router router = createRouter();
        Map<Route.Method, List<Route>> routes = router.getApp().getRouter().getRoutes();
        new JavaClientApiFormat().write(routes, outDir);
        return 0;
    }

    private Router createRouter() {
        ServletContext servletContext = new ContextHandler.NoContext();
        servletContext.setAttribute(Injector.class.getName(), InjectHelper.getInjector());
        return new Router(servletContext);
    }
}
