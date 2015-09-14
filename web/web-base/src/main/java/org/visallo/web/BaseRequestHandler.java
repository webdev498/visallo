package org.visallo.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Preconditions;
import com.v5analytics.webster.Handler;
import com.v5analytics.webster.HandlerChain;
import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.vertexium.Authorizations;
import org.visallo.core.config.Configuration;
import org.visallo.core.exception.VisalloAccessDeniedException;
import org.visallo.core.exception.VisalloException;
import org.visallo.core.model.user.UserRepository;
import org.visallo.core.model.workspace.WorkspaceRepository;
import org.visallo.core.user.ProxyUser;
import org.visallo.core.user.User;
import org.visallo.web.clientapi.model.ClientApiObject;
import org.visallo.web.clientapi.util.ObjectMapperFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.*;

/**
 * Represents the base behavior that a {@link Handler} must support
 * and provides common methods for handler usage
 */
public abstract class BaseRequestHandler extends MinimalRequestHandler {
    public static final String VISALLO_WORKSPACE_ID_HEADER_NAME = "Visallo-Workspace-Id";
    public static final String WORKSPACE_ID_ATTRIBUTE_NAME = "workspaceId";
    private final UserRepository userRepository;
    private final WorkspaceRepository workspaceRepository;
    private final ObjectMapper objectMapper = ObjectMapperFactory.getInstance();

    protected BaseRequestHandler(UserRepository userRepository, WorkspaceRepository workspaceRepository, Configuration configuration) {
        super(configuration);
        this.userRepository = userRepository;
        this.workspaceRepository = workspaceRepository;
    }

    @Override
    public abstract void handle(HttpServletRequest request, HttpServletResponse response, HandlerChain chain) throws Exception;

    public static String getBaseUrl(HttpServletRequest request, Configuration configuration) {
        String configuredBaseUrl = configuration.get(Configuration.BASE_URL, null);
        if (configuredBaseUrl != null && configuredBaseUrl.trim().length() > 0) {
            return configuredBaseUrl;
        }

        String scheme = request.getScheme();
        String serverName = request.getServerName();
        int port = request.getServerPort();
        String contextPath = request.getContextPath();

        StringBuilder sb = new StringBuilder();
        sb.append(scheme).append("://").append(serverName);
        if (!(scheme.equals("http") && port == 80 || scheme.equals("https") && port == 443)) {
            sb.append(":").append(port);
        }
        sb.append(contextPath);
        return sb.toString();
    }

    protected String getActiveWorkspaceId(final HttpServletRequest request) {
        String workspaceId = getWorkspaceIdOrDefault(request);
        if (workspaceId == null || workspaceId.trim().length() == 0) {
            throw new VisalloException(VISALLO_WORKSPACE_ID_HEADER_NAME + " is a required header.");
        }
        return workspaceId;
    }

    protected String getWorkspaceIdOrDefault(final HttpServletRequest request) {
        String workspaceId = (String) request.getAttribute(WORKSPACE_ID_ATTRIBUTE_NAME);
        if (workspaceId == null || workspaceId.trim().length() == 0) {
            workspaceId = request.getHeader(VISALLO_WORKSPACE_ID_HEADER_NAME);
            if (workspaceId == null || workspaceId.trim().length() == 0) {
                workspaceId = getOptionalParameter(request, WORKSPACE_ID_ATTRIBUTE_NAME);
                if (workspaceId == null || workspaceId.trim().length() == 0) {
                    return null;
                }
            }
        }
        return workspaceId;
    }

    protected Authorizations getAuthorizations(final HttpServletRequest request, final User user) {
        String workspaceId = getWorkspaceIdOrDefault(request);
        if (workspaceId != null) {
            if (!this.workspaceRepository.hasReadPermissions(workspaceId, user)) {
                throw new VisalloAccessDeniedException("You do not have access to workspace: " + workspaceId, user, workspaceId);
            }
            return getUserRepository().getAuthorizations(user, workspaceId);
        }

        return getUserRepository().getAuthorizations(user);
    }

    public static void setMaxAge(final HttpServletResponse response, int numberOfSeconds) {
        response.setHeader("Cache-Control", "max-age=" + numberOfSeconds);
    }

    protected void respondWithNotFound(final HttpServletResponse response) throws IOException {
        response.sendError(HttpServletResponse.SC_NOT_FOUND);
    }

    protected void respondWithNotFound(final HttpServletResponse response, String message) throws IOException {
        response.sendError(HttpServletResponse.SC_NOT_FOUND, message);
    }

    /**
     * Configures the content type for the provided response to contain {@link JSONObject} data
     *
     * @param response   The response instance to modify
     * @param jsonObject The JSON data to include in the response
     */
    protected void respondWithJson(final HttpServletResponse response, final JSONObject jsonObject) {
        configureResponse(ResponseTypes.JSON_OBJECT, response, jsonObject);
    }

    protected void respondWithClientApiObject(HttpServletResponse response, ClientApiObject obj) throws IOException {
        if (obj == null) {
            respondWithNotFound(response);
            return;
        }
        try {
            String jsonObject = objectMapper.writeValueAsString(obj);
            configureResponse(ResponseTypes.JSON_OBJECT, response, jsonObject);
        } catch (JsonProcessingException e) {
            throw new VisalloException("Could not write json", e);
        }
    }

    /**
     * Configures the content type for the provided response to contain {@link JSONArray} data
     *
     * @param response  The response instance to modify
     * @param jsonArray The JSON data to include in the response
     */
    protected void respondWithJson(final HttpServletResponse response, final JSONArray jsonArray) {
        configureResponse(ResponseTypes.JSON_ARRAY, response, jsonArray);
    }

    protected void respondWithPlaintext(final HttpServletResponse response, final String plaintext) {
        configureResponse(ResponseTypes.PLAINTEXT, response, plaintext);
    }

    protected User getUser(HttpServletRequest request) {
        return new ProxyUser(CurrentUser.getUserId(request), this.userRepository);
    }

    public static void configureResponse(final ResponseTypes type, final HttpServletResponse response, final Object responseData) {
        Preconditions.checkNotNull(response, "The provided response was invalid");
        Preconditions.checkNotNull(responseData, "The provided data was invalid");

        try {
            switch (type) {
                case JSON_OBJECT:
                    response.setContentType("application/json");
                    response.setCharacterEncoding("UTF-8");
                    response.getWriter().write(responseData.toString());
                    break;
                case JSON_ARRAY:
                    response.setContentType("application/json");
                    response.setCharacterEncoding("UTF-8");
                    response.getWriter().write(responseData.toString());
                    break;
                case PLAINTEXT:
                    response.setContentType("text/plain");
                    response.setCharacterEncoding("UTF-8");
                    response.getWriter().write(responseData.toString());
                    break;
                case HTML:
                    response.setContentType("text/html");
                    response.setCharacterEncoding("UTF-8");
                    response.getWriter().write(responseData.toString());
                    break;
                default:
                    throw new VisalloException("Unsupported response type encountered");
            }

            if (response.getWriter().checkError()) {
                throw new ConnectionClosedException();
            }
        } catch (IOException e) {
            throw new VisalloException("Error occurred while writing response", e);
        }
    }

    public static void copyPartToOutputStream(Part part, OutputStream out) throws IOException {
        try (InputStream in = part.getInputStream()) {
            IOUtils.copy(in, out);
        }
    }

    public static void copyPartToFile(Part part, File outFile) throws IOException {
        try (FileOutputStream out = new FileOutputStream(outFile)) {
            copyPartToOutputStream(part, out);
        }
    }

    public UserRepository getUserRepository() {
        return userRepository;
    }

    public WorkspaceRepository getWorkspaceRepository() {
        return workspaceRepository;
    }
}
