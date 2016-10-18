package org.visallo.core.formula;

import com.eclipsesource.v8.NodeJS;
import com.eclipsesource.v8.V8;
import com.eclipsesource.v8.V8Array;
import com.eclipsesource.v8.V8Object;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.base.Joiner;
import com.google.common.collect.Queues;
import com.google.inject.Inject;
import org.apache.commons.io.IOUtils;
//import org.mozilla.javascript.Context;
//import org.mozilla.javascript.Function;
//import org.mozilla.javascript.Scriptable;
//import org.mozilla.javascript.ScriptableObject;
import org.apache.commons.lang3.StringEscapeUtils;
import org.vertexium.Authorizations;
import org.vertexium.Element;
import org.vertexium.util.StreamUtils;
import org.visallo.core.config.Configuration;
import org.visallo.core.exception.VisalloException;
import org.visallo.core.model.ontology.OntologyRepository;
import org.visallo.core.util.ClientApiConverter;
import org.visallo.core.util.VisalloLogger;
import org.visallo.core.util.VisalloLoggerFactory;
import org.visallo.web.clientapi.model.ClientApiElement;
import org.visallo.web.clientapi.model.ClientApiOntology;
import org.visallo.web.clientapi.util.FileUtils;
import org.visallo.web.clientapi.util.ObjectMapperFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Evaluates JavaScript formulas (title, subtitle, etc) using Java's Rhino JavaScript interpreter.
 */
public class FormulaEvaluator {
    private static final VisalloLogger LOGGER = VisalloLoggerFactory.getLogger(FormulaEvaluator.class);
    public static final String CONFIGURATION_PARAMETER_MAX_THREADS = FormulaEvaluator.class.getName() + ".max.threads";
    public static final int CONFIGURATION_DEFAULT_MAX_THREADS = 1;
    private Configuration configuration;
    private OntologyRepository ontologyRepository;
    private ExecutorService executorService;

    private static final ThreadLocal<Map<String, NodeJS>> threadLocalScope = new ThreadLocal<Map<String, NodeJS>>() {
        @Override
        protected Map<String, NodeJS> initialValue() {
            return new HashMap<>();
        }
    };

    @Inject
    public FormulaEvaluator(Configuration configuration, OntologyRepository ontologyRepository) {
        this.configuration = configuration;
        this.ontologyRepository = ontologyRepository;

        executorService = Executors.newFixedThreadPool(configuration.getInt(
                CONFIGURATION_PARAMETER_MAX_THREADS,
                CONFIGURATION_DEFAULT_MAX_THREADS
        ));
    }

    public void close() {
        executorService.shutdown();
    }

    public String evaluateTitleFormula(Element element, UserContext userContext, Authorizations authorizations) {
        return evaluateFormula("Title", element, null, null, userContext, authorizations);
    }

    public String evaluateTimeFormula(Element element, UserContext userContext, Authorizations authorizations) {
        return evaluateFormula("Time", element, null, null, userContext, authorizations);
    }

    public String evaluateSubtitleFormula(Element element, UserContext userContext, Authorizations authorizations) {
        return evaluateFormula("Subtitle", element, null, null, userContext, authorizations);
    }

    public String evaluatePropertyDisplayFormula(
            Element element,
            String propertyKey,
            String propertyName,
            UserContext userContext,
            Authorizations authorizations
    ) {
        return evaluateFormula("Property", element, propertyKey, propertyName, userContext, authorizations);
    }

    private String evaluateFormula(
            String type,
            Element element,
            String propertyKey,
            String propertyName,
            UserContext userContext,
            Authorizations authorizations
    ) {
        FormulaEvaluatorCallable evaluationCallable = new FormulaEvaluatorCallable(
                type,
                element,
                propertyKey,
                propertyName,
                userContext,
                authorizations
        );

        try {
            return executorService.submit(evaluationCallable).get();
        } catch (InterruptedException e) {
            LOGGER.error(type + " evaluation interrupted", e);
        } catch (ExecutionException e) {
            LOGGER.error("Error encountered during " + type + " evaluation", e);
        }

        return "Unable to Evaluate " + type;
    }

    public NodeJS getScriptable(UserContext userContext) {
        Map<String, NodeJS> runtimes = threadLocalScope.get();

        String mapKey = userContext.locale.toString() + userContext.timeZone;
        NodeJS runtime = runtimes.get(mapKey);
        if (runtime == null) {
            runtime = setupContext(getConfigurationJson(userContext.locale), userContext.timeZone);
            runtimes.put(mapKey, runtime);
        }

        addVariable(runtime, "ONTOLOGY_JSON", getOntologyJson());

        return runtime;
    }

    public static void addVariable(NodeJS runtime, String variableName, String value){
        runtime.getRuntime().executeVoidScript(String.format("var %s = \"%s\";", variableName, StringEscapeUtils.escapeJava(value)));
    }

    private NodeJS setupContext(String configurationJson, String timeZone) {
        NodeJS runtime = NodeJS.createNodeJS();

        RequireJsSupport.registerJavaCallbacks(runtime);

        try {
            FormulaEvaluator.addVariable(runtime, "CONFIG_JSON", configurationJson);
            FormulaEvaluator.addVariable(runtime, "USERS_TIMEZONE", timeZone);
        } catch (Exception e) {
            throw new VisalloException("Json resource not available", e);
        }
        addVariable(runtime, "ONTOLOGY_JSON", getOntologyJson());
        requireFile(runtime, "libs/r.js");
        evaluateFile(runtime, "libs/underscore.js");
        evaluateFile(runtime, "libs/windowTimers.js");
//        evaluateFile(runtime, "util_vertex_formatters.js");
        evaluateFile(runtime, "loader.js");

        return runtime;
    }

    protected String getOntologyJson() {
        ClientApiOntology result = ontologyRepository.getClientApiObject();
        try {
            return ObjectMapperFactory.getInstance().writeValueAsString(result);
        } catch (JsonProcessingException ex) {
            throw new VisalloException("Could not evaluate JSON: " + result, ex);
        }
    }

    protected String getConfigurationJson(Locale locale) {
        return configuration.toJSON(locale).toString();
    }

    private void evaluateFile(NodeJS nodeJs, String filename) {
        LOGGER.debug("evaluating file: %s", filename);
        File file = new File("/home/ryan/repos/visallo-lts/visallo/core/core/src/main/resources/org/visallo/core/formula/", filename);

        LOGGER.debug("full file: %s", file.getAbsoluteFile());
        try {
            String s = IOUtils.toString(new FileInputStream(file));
            nodeJs.getRuntime().executeVoidScript(s);
        } catch (IOException e) {
            throw new VisalloException();
        }
//        nodeJs.exec(file);

        while(nodeJs.isRunning()){
            nodeJs.handleMessage();
            LOGGER.debug("handling message");
        }
    }

    private void requireFile(NodeJS nodeJs, String filename){
        LOGGER.debug("evaluating file: %s", filename);
        File file = new File("/home/ryan/repos/visallo-lts/visallo/core/core/src/main/resources/org/visallo/core/formula/", filename);

        LOGGER.debug("full file: %s", file.getAbsoluteFile());

        nodeJs.require(file);

        while(nodeJs.isRunning()){
            nodeJs.handleMessage();
            LOGGER.debug("handling message");
        }
    }

    protected String toJson(Element element, String workspaceId, Authorizations authorizations) {
        ClientApiElement v = ClientApiConverter.toClientApi(element, workspaceId, authorizations);
        return v.toString();
    }

    public static class UserContext {
        private final Locale locale;
        private final String timeZone;
        private final String workspaceId;

        public UserContext(Locale locale, String timeZone, String workspaceId) {
            this.locale = locale == null ? Locale.getDefault() : locale;
            this.timeZone = timeZone;
            this.workspaceId = workspaceId;
        }

        public Locale getLocale() {
            return locale;
        }

        public String getTimeZone() {
            return timeZone;
        }

        public String getWorkspaceId() {
            return workspaceId;
        }
    }

    private class FormulaEvaluatorCallable implements Callable<String> {
        private final String propertyKey;
        private final String propertyName;
        private UserContext userContext;
        private String fieldName;
        private Element element;
        private Authorizations authorizations;

        public FormulaEvaluatorCallable(
                String fieldName,
                Element element,
                String propertyKey,
                String propertyName,
                UserContext userContext,
                Authorizations authorizations
        ) {
            this.fieldName = fieldName;
            this.element = element;
            this.propertyKey = propertyKey;
            this.propertyName = propertyName;
            this.userContext = userContext;
            this.authorizations = authorizations;
        }

        @Override
        public String call() throws Exception {
            NodeJS runtime = getScriptable(userContext);
            String json = toJson(element, userContext.getWorkspaceId(), authorizations);
//            V8Object obj = new V8Object(runtime.getRuntime());

            V8Array arr = new V8Array(runtime.getRuntime());
            arr.push(json);
            arr.push(propertyKey);
            arr.push(propertyName);

            String functionName = "evaluate" + fieldName + "FormulaJson";

            while(!Arrays.asList(runtime.getRuntime().getKeys()).contains(functionName)){
                runtime.handleMessage();
            }

            return runtime.getRuntime().executeStringFunction(functionName, arr);
        }
    }
}
