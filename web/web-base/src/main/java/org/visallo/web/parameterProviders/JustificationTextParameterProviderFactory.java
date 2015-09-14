package org.visallo.web.parameterProviders;

import com.google.inject.Inject;
import com.v5analytics.webster.HandlerChain;
import com.v5analytics.webster.parameterProviders.ParameterProvider;
import com.v5analytics.webster.parameterProviders.ParameterProviderFactory;
import org.visallo.core.config.Configuration;
import org.visallo.core.model.user.UserRepository;
import org.visallo.web.MinimalRequestHandler;
import org.visallo.web.RouteHelper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public class JustificationTextParameterProviderFactory extends ParameterProviderFactory<String> {
    private final ParameterProvider<String> parameterProvider;

    @Inject
    public JustificationTextParameterProviderFactory(UserRepository userRepository, Configuration configuration) {
        // this is a temporary fix until all routes are moved over and the RouteHelper methods can be moved into this class
        final RouteHelper routeHelper = new RouteHelper(configuration, new MinimalRequestHandler(configuration) {
            @Override
            public void handle(HttpServletRequest request, HttpServletResponse response, HandlerChain chain) throws Exception {

            }
        });
        parameterProvider = new VisalloBaseParameterProvider<String>(userRepository, configuration) {
            @Override
            public String getParameter(HttpServletRequest request, HttpServletResponse response, HandlerChain chain) {
                return routeHelper.getJustificationText(request);
            }
        };
    }

    @Override
    public boolean isHandled(Method handleMethod, Class<? extends String> parameterType, Annotation[] parameterAnnotations) {
        return getJustificationTextAnnotation(parameterAnnotations) != null;
    }

    @Override
    public ParameterProvider<String> createParameterProvider(Method handleMethod, Class<?> parameterType, Annotation[] parameterAnnotations) {
        return parameterProvider;
    }

    private static JustificationText getJustificationTextAnnotation(Annotation[] annotations) {
        for (Annotation annotation : annotations) {
            if (annotation instanceof JustificationText) {
                return (JustificationText) annotation;
            }
        }
        return null;
    }
}
