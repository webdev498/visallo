package org.visallo.web.clientapi;

import org.visallo.web.clientapi.codegen.VisalloApiBase;
import org.visallo.web.clientapi.model.ClientApiUser;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class VisalloApi extends VisalloApiBase {
    private String sessionCookieValue;
    private String workspaceId;
    private ClientApiUser user;

    public VisalloApi(String basePath) {
        super(basePath);
    }

    public void setSessionCookie(String sessionCookieValue) {
        this.sessionCookieValue = sessionCookieValue;
    }

    @Override
    protected String getSessionCookieValue() {
        return sessionCookieValue;
    }

    @Override
    public String getWorkspaceId() {
        return workspaceId;
    }

    public void setWorkspaceId(String workspaceId) {
        this.workspaceId = workspaceId;
    }

    public void setUser(ClientApiUser user) {
        this.user = user;
    }

    @Override
    public String getCsrfToken() {
        return user.getCsrfToken();
    }

    public String getUserId() {
        return user.getId();
    }

    public <T> T execute(String httpVerb, String path, List<Parameter> parameters, Class<T> returnType) {
        if (parameters == null) {
            parameters = new ArrayList<>();
        }
        String targetUrl = getTargetUrl(path);
        HttpURLConnection connection = null;
        try {
            //Send request
            String urlParameters = getParametersAsUrlEncodedString(parameters);
            if (parameters.size() > 0 && !httpVerb.equals("POST")) {
                targetUrl += "?" + urlParameters;
            }
            URL url = new URL(targetUrl);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(httpVerb);
            connection.setUseCaches(false);
            connection.setDoOutput(true);
            if (getWorkspaceId() != null) {
                connection.setRequestProperty("Visallo-Workspace-Id", getWorkspaceId());
            }
            if (httpVerb.equals("POST") && getCsrfToken() != null) {
                connection.setRequestProperty("Visallo-CSRF-Token", getCsrfToken());
            }
            if (getSessionCookieValue() != null) {
                connection.setRequestProperty("Cookie", "JSESSIONID=" + getSessionCookieValue());
            }
            if (parameters.size() > 0 && httpVerb.equals("POST")) {
                connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                connection.setRequestProperty("Content-Length", Integer.toString(urlParameters.getBytes().length));
                OutputStream out = connection.getOutputStream();
                out.write(urlParameters.getBytes());
            }

            //Get Response
            byte[] result = readAllFromHttpURLConnection(connection);
            return byteArrayToReturnType(result, returnType);
        } catch (Exception ex) {
            throw new VisalloClientApiException("Could not execute request " + httpVerb + " " + targetUrl, ex);
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }
}