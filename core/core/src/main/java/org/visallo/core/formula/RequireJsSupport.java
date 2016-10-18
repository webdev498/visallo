package org.visallo.core.formula;

import com.eclipsesource.v8.*;
//import org.mozilla.javascript.Context;
//import org.mozilla.javascript.Function;
//import org.mozilla.javascript.Scriptable;
//import org.mozilla.javascript.ScriptableObject;
import org.visallo.core.util.VisalloLogger;
import org.visallo.core.util.VisalloLoggerFactory;

import java.io.File;
import java.io.IOException;

@SuppressWarnings("unused")
public class RequireJsSupport  {
    private static final long serialVersionUID = 1L;
    private static VisalloLogger LOGGER = VisalloLoggerFactory.getLogger(RequireJsSupport.class);

    public static void registerJavaCallbacks(NodeJS nodeJs){
        V8 runtime = nodeJs.getRuntime();
        registerPrint(runtime);
        registerConsoleError(runtime);
        registerConsoleWarn(runtime);

    }

    private static void registerPrint(V8 runtime) {
        runtime.registerJavaMethod((receiver, parameters) -> {
            for(int i = 0; i < parameters.length(); i++){
                LOGGER.debug(parameters.get(i).toString());
            }

            return null;
        }, "print");
    }

    private static void registerConsoleWarn(V8 runtime) {
        runtime.registerJavaMethod((receiver, parameters) -> {
            for(int i = 0; i < parameters.length(); i++){
                LOGGER.warn(parameters.get(i).toString());
            }

            return null;
        }, "consoleWarn");
    }

    private static void registerConsoleError(V8 runtime) {
        runtime.registerJavaMethod((receiver, parameters) -> {
            for(int i = 0; i < parameters.length(); i++){
                LOGGER.warn(parameters.get(i).toString());
            }

            return null;
        }, "consoleError");
    }

//    public static void load(V8 cx, Scriptable thisObj, Object[] args, Function funObj) throws IOException {
//        RequireJsSupport shell = (RequireJsSupport) getTopLevelScope(thisObj);
//        for (Object arg : args) {
//            shell.processSource(cx, Context.toString(arg));
//        }
//    }
//
//    public static String readFully(V8 cx, Scriptable thisObj, Object[] args, Function funObj) throws IOException {
//        RequireJsSupport shell = (RequireJsSupport) getTopLevelScope(thisObj);
//        if (args.length == 1) {
//            return shell.getFileContents(Context.toString(args[0]));
//        }
//        return null;
//    }

    private void processSource(NodeJS nodeJs, String filename) throws IOException {
        nodeJs.exec(new File(filename));
    }
}

