package org.visallo.web.parameterProviders;

import com.google.common.base.Preconditions;
import com.v5analytics.webster.App;
import com.v5analytics.webster.parameterProviders.ParameterProvider;
import org.visallo.core.config.Configuration;
import org.visallo.core.exception.VisalloException;
import org.visallo.core.model.user.UserRepository;
import org.visallo.core.user.ProxyUser;
import org.visallo.core.user.User;
import org.visallo.web.BaseRequestHandler;
import org.visallo.web.CurrentUser;
import org.visallo.web.WebApp;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;
import java.util.TimeZone;

public abstract class VisalloBaseParameterProvider<T> extends ParameterProvider<T> {
    private static final String VISALLO_WORKSPACE_ID_HEADER_NAME = BaseRequestHandler.VISALLO_WORKSPACE_ID_HEADER_NAME;
    private static final String LOCALE_LANGUAGE_PARAMETER = "localeLanguage";
    private static final String LOCALE_COUNTRY_PARAMETER = "localeCountry";
    private static final String LOCALE_VARIANT_PARAMETER = "localeVariant";
    private static final String VISALLO_TIME_ZONE_HEADER_NAME = "Visallo-TimeZone";
    private static final String TIME_ZONE_ATTRIBUTE_NAME = "timeZone";
    private static final String TIME_ZONE_PARAMETER_NAME = "timeZone";
    private final UserRepository userRepository;
    private final Configuration configuration;

    public VisalloBaseParameterProvider(UserRepository userRepository, Configuration configuration) {
        this.userRepository = userRepository;
        this.configuration = configuration;
    }

    protected String getWorkspaceIdOrDefault(final HttpServletRequest request) {
        String workspaceId = (String) request.getAttribute("workspaceId");
        if (workspaceId == null || workspaceId.trim().length() == 0) {
            workspaceId = request.getHeader(VISALLO_WORKSPACE_ID_HEADER_NAME);
            if (workspaceId == null || workspaceId.trim().length() == 0) {
                workspaceId = getOptionalParameter(request, "workspaceId");
                if (workspaceId == null || workspaceId.trim().length() == 0) {
                    return null;
                }
            }
        }
        return workspaceId;
    }

    protected String getActiveWorkspaceId(final HttpServletRequest request) {
        String workspaceId = getWorkspaceIdOrDefault(request);
        if (workspaceId == null || workspaceId.trim().length() == 0) {
            throw new VisalloException(VISALLO_WORKSPACE_ID_HEADER_NAME + " is a required header.");
        }
        return workspaceId;
    }

    private String getOptionalParameter(final HttpServletRequest request, final String parameterName) {
        Preconditions.checkNotNull(request, "The provided request was invalid");
        return getParameter(request, parameterName, true);
    }

    private String getParameter(final HttpServletRequest request, final String parameterName, final boolean optional) {
        final String paramValue = request.getParameter(parameterName);

        if (paramValue == null) {
            if (!optional) {
                throw new VisalloException(String.format("Parameter: '%s' is required in the request", parameterName));
            }

            return null;
        }

        return paramValue;
    }

    protected User getUser(HttpServletRequest request) {
        ProxyUser user = (ProxyUser) request.getAttribute("user");
        if (user != null) {
            return user;
        }
        user = new ProxyUser(CurrentUser.getUserId(request), getUserRepository());
        request.setAttribute("user", user);
        return user;
    }

    protected WebApp getWebApp(HttpServletRequest request) {
        return (WebApp) App.getApp(request);
    }

    protected Locale getLocale(HttpServletRequest request) {
        String language = getOptionalParameter(request, LOCALE_LANGUAGE_PARAMETER);
        String country = getOptionalParameter(request, LOCALE_COUNTRY_PARAMETER);
        String variant = getOptionalParameter(request, LOCALE_VARIANT_PARAMETER);

        if (language != null) {
            return WebApp.getLocal(language, country, variant);
        }
        return request.getLocale();
    }

    protected String getTimeZone(final HttpServletRequest request) {
        String timeZone = (String) request.getAttribute(TIME_ZONE_ATTRIBUTE_NAME);
        if (timeZone == null || timeZone.trim().length() == 0) {
            timeZone = request.getHeader(VISALLO_TIME_ZONE_HEADER_NAME);
            if (timeZone == null || timeZone.trim().length() == 0) {
                timeZone = getOptionalParameter(request, TIME_ZONE_PARAMETER_NAME);
                if (timeZone == null || timeZone.trim().length() == 0) {
                    timeZone = this.configuration.get(Configuration.DEFAULT_TIME_ZONE, TimeZone.getDefault().getDisplayName());
                }
            }
        }
        return timeZone;
    }

    public UserRepository getUserRepository() {
        return userRepository;
    }
}
