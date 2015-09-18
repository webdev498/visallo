package org.visallo.web.clientapi.codegen.formats;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.base.Predicates;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.v5analytics.webster.RequestResponseHandler;
import com.v5analytics.webster.RequestResponseHandlerParameterizedHandlerWrapper;
import com.v5analytics.webster.annotations.Handle;
import com.v5analytics.webster.annotations.Optional;
import com.v5analytics.webster.annotations.Required;
import org.visallo.web.clientapi.codegen.util.NameUtil;
import org.visallo.web.clientapi.model.ClientApiSuccess;
import org.visallo.web.parameterProviders.JustificationText;
import org.visallo.web.parameterProviders.JustificationTextParameterProviderFactory;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JavaClientApiFormat extends ClientApiFormat {
    public static final String BASE_PACKAGE_PATH = "org/visallo/web/clientapi/codegen";
    public static final String BASE_PACKAGE = "org.visallo.web.clientapi.codegen";

    @Override
    protected void writeCategoriesEndPoints(Map<String, List<EndPoint>> categoriesEndPoints, File outDir) {
        writeVisalloApi(categoriesEndPoints, outDir);
        super.writeCategoriesEndPoints(categoriesEndPoints, outDir);
    }

    @Override
    protected void writeCategoryEndPoints(String category, List<EndPoint> endPoints, File outDir) {
        String className = NameUtil.toClassName(category) + "Base";
        File outFile = new File(outDir, BASE_PACKAGE_PATH + "/" + className + ".java");
        Map<String, Object> context = new HashMap<>();
        context.put("package", BASE_PACKAGE);
        context.put("className", className);
        context.put("endPoints", getJavaEndPoints(endPoints));
        writeTemplate("Category.java", context, outFile);
    }

    protected List<JavaEndPoint> getJavaEndPoints(List<EndPoint> endPoints) {
        return Lists.newArrayList(Iterables.filter(Lists.transform(endPoints, new Function<EndPoint, JavaEndPoint>() {
            @Override
            public JavaEndPoint apply(EndPoint endPoint) {
                Method handleMethod = getHandleMethod(endPoint);
                if (handleMethod == null) {
                    return null;
                }
                return new JavaEndPoint(endPoint, handleMethod);
            }
        }), Predicates.notNull()));
    }

    protected Method getHandleMethod(EndPoint endPoint) {
        RequestResponseHandler[] handlers = endPoint.getRoute().getHandlers();
        RequestResponseHandler lastHandler = handlers[handlers.length - 1];
        if (lastHandler instanceof RequestResponseHandlerParameterizedHandlerWrapper) {
            return ((RequestResponseHandlerParameterizedHandlerWrapper) lastHandler).getHandleMethod();
        }
        for (Method method : lastHandler.getClass().getMethods()) {
            if (method.getAnnotation(Handle.class) != null) {
                return method;
            }
        }
        return null;
    }

    protected void writeVisalloApi(Map<String, List<EndPoint>> categoriesEndPoints, File outDir) {
        File outFile = new File(outDir, BASE_PACKAGE_PATH + "/VisalloApiBase.java");
        Map<String, Object> context = new HashMap<>();
        context.put("package", BASE_PACKAGE);
        context.put("categoryClasses", getCategoryClasses(categoriesEndPoints));
        writeTemplate("VisalloApiBase.java", context, outFile);
    }

    private List<CategoryClass> getCategoryClasses(Map<String, List<EndPoint>> categoriesEndPoints) {
        List<CategoryClass> results = new ArrayList<>();
        for (String category : categoriesEndPoints.keySet()) {
            if (category.length() == 0) {
                continue;
            }
            String fieldName = NameUtil.toFieldName(category);
            String className = NameUtil.toClassName(category);
            results.add(new CategoryClass(fieldName, className));
        }
        return results;
    }

    protected void writeTemplate(String templateName, Map<String, Object> context, File outFile) {
        writeTemplate("/org/visallo/web/clientapi/codegen/formats/java", templateName, context, outFile);
    }

    protected static class CategoryClass {
        private final String fieldName;
        private final String className;

        public CategoryClass(String fieldName, String className) {
            this.fieldName = fieldName;
            this.className = className;
        }

        public String getFieldName() {
            return fieldName;
        }

        public String getClassName() {
            return className;
        }
    }

    protected static class JavaEndPoint {
        private final EndPoint endPoint;
        private final Method handleMethod;
        private final List<Parameter> parameters;

        public JavaEndPoint(EndPoint endPoint, Method handleMethod) {
            this.endPoint = endPoint;
            this.handleMethod = handleMethod;
            this.parameters = getParameters(handleMethod);
        }

        public List<Parameter> getParameters() {
            return parameters;
        }

        private List<Parameter> getParameters(Method handleMethod) {
            List<Parameter> results = new ArrayList<>();
            Class<?>[] parameterTypes = this.handleMethod.getParameterTypes();
            Annotation[][] parametersAnnotations = this.handleMethod.getParameterAnnotations();
            for (int i = 0; i < parameterTypes.length; i++) {
                Class<?> parameterType = parameterTypes[i];
                Annotation[] parameterAnnotations = parametersAnnotations[i];
                String parameterName = getParameterName(parameterAnnotations);
                String name = parameterName;
                if (name == null) {
                    continue;
                }
                name = NameUtil.toFieldName(name);
                if (parameterType.isArray() && !name.endsWith("s")) {
                    name += "s";
                }
                String javadoc = isRequired(parameterAnnotations) ? "REQUIRED" : "OPTIONAL";
                results.add(new Parameter(name, parameterName, javadoc));
            }
            return results;
        }

        public String getName() {
            String function = this.endPoint.getFunction();
            if (function.length() > 0) {
                function = NameUtil.toClassName(function);
            }
            return this.endPoint.getMethod().name().toLowerCase() + function;
        }

        public String getReturnType() {
            if (handleMethod.getReturnType().equals(ClientApiSuccess.class)) {
                return "void";
            }
            return handleMethod.getReturnType().getSimpleName();
        }

        public String getReturnStmt() {
            return getReturnType().equals("void") ? "" : "return ";
        }

        public String getHttpVerb() {
            return this.endPoint.getMethod().name();
        }

        public String getPath() {
            return this.endPoint.getRoute().getPath();
        }

        public String getReturnTypeClass() {
            String returnType = getReturnType();
            if (returnType.equals("void")) {
                return "null";
            } else {
                return returnType + ".class";
            }
        }

        public String getParameterDeclarations() {
            Class<?>[] parameterTypes = this.handleMethod.getParameterTypes();
            Annotation[][] parametersAnnotations = this.handleMethod.getParameterAnnotations();
            List<String> parameterDeclarations = new ArrayList<>();
            for (int i = 0; i < parameterTypes.length; i++) {
                Class<?> parameterType = parameterTypes[i];
                Annotation[] parameterAnnotations = parametersAnnotations[i];
                String parameterName = getParameterName(parameterAnnotations);
                String simplifiedParameterName = parameterName;
                if (simplifiedParameterName == null) {
                    continue;
                }
                simplifiedParameterName = NameUtil.toFieldName(simplifiedParameterName);
                if (parameterType.isArray() && !simplifiedParameterName.endsWith("s")) {
                    simplifiedParameterName += "s";
                }
                boolean required = isRequired(parameterAnnotations);
                Optional optionalAnnotation = getOptionalAnnotation(parameterAnnotations);
                String annotation = (required ? "@Required" : "@Optional") + "(name = \"" + parameterName + "\"";
                if (optionalAnnotation != null && !optionalAnnotation.defaultValue().equals(Optional.NOT_SET)) {
                    annotation += ", defaultValue = \"" + optionalAnnotation.defaultValue() + "\"";
                }
                annotation += ")";
                parameterDeclarations.add(annotation + " " + parameterType.getSimpleName() + " " + simplifiedParameterName);
            }
            return Joiner.on(",\n        ").join(parameterDeclarations);
        }

        private Optional getOptionalAnnotation(Annotation[] parameterAnnotations) {
            for (Annotation parameterAnnotation : parameterAnnotations) {
                if (parameterAnnotation instanceof Optional) {
                    return (Optional) parameterAnnotation;
                }
            }
            return null;
        }

        private boolean isRequired(Annotation[] parameterAnnotations) {
            for (Annotation parameterAnnotation : parameterAnnotations) {
                if (parameterAnnotation instanceof Required) {
                    return true;
                }
                if (parameterAnnotation instanceof Optional) {
                    return false;
                }
            }
            return false;
        }

        private String getParameterName(Annotation[] parameterAnnotations) {
            for (Annotation parameterAnnotation : parameterAnnotations) {
                if (parameterAnnotation instanceof Required) {
                    return ((Required) parameterAnnotation).name();
                }
                if (parameterAnnotation instanceof Optional) {
                    return ((Optional) parameterAnnotation).name();
                }
                if (parameterAnnotation instanceof JustificationText) {
                    return JustificationTextParameterProviderFactory.JUSTIFICATION_TEXT;
                }
            }
            return null;
        }

        public static class Parameter {
            private final String parameterName;
            private final String name;
            private final String javadoc;

            public Parameter(String name, String parameterName, String javadoc) {
                this.name = name;
                this.parameterName = parameterName;
                this.javadoc = javadoc;
            }

            public String getName() {
                return name;
            }

            public String getJavadoc() {
                return javadoc;
            }

            public String getParameterName() {
                return parameterName;
            }
        }
    }
}
