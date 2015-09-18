package org.visallo.web.clientapi;

import org.visallo.web.clientapi.codegen.VisalloApiBase;
import org.visallo.web.clientapi.model.ClientApiUser;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class VisalloApi extends VisalloApiBase {
    private String sessionCookieValue;
    private String workspaceId;
    private ClientApiUser user;
    private boolean debug;

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

    @Override
    public void logout() {
        super.logout();
        this.sessionCookieValue = null;
        this.workspaceId = null;
        this.user = null;
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

    protected String getParametersAsUrlEncodedString(List<Parameter> parameters) {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for (Parameter parameter : parameters) {
            String[] valueStrings = parameterValueToUrlEncodedStrings(parameter.getValue());
            if (valueStrings == null) {
                continue;
            }
            for (String valueString : valueStrings) {
                if (!first) {
                    result.append("&");
                }
                result.append(parameter.getName());
                result.append("=");
                result.append(valueString);
                first = false;
            }
        }
        return result.toString();
    }

    protected String[] parameterValueToUrlEncodedStrings(Object value) {
        try {
            if (value == null) {
                return null;
            }
            if (value.getClass().isArray()) {
                Class<?> componentType = value.getClass().getComponentType();
                if (componentType.equals(String.class)) {
                    String[] values = (String[]) value;
                    for (int i = 0; i < values.length; i++) {
                        values[i] = urlEncodeValueObject(values[i]);
                    }
                    return values;
                } else {
                    throw new VisalloClientApiException("Unhandled array component type: " + componentType.getName());
                }
            }
            return new String[]{urlEncodeValueObject(value)};
        } catch (UnsupportedEncodingException ex) {
            throw new VisalloClientApiException("Could not encode string", ex);
        }
    }

    protected String urlEncodeValueObject(Object value) throws UnsupportedEncodingException {
        return URLEncoder.encode(value.toString(), "utf8");
    }

    protected <T> T byteArrayToReturnType(byte[] result, Class<T> cls) {
        if (cls == null) {
            return null;
        }
        try {
            String json = new String(result);
            if (String.class.equals(cls)) {
                if (json.startsWith("\"") && json.endsWith("\"") && json.length() > 1) {
                    return (T) json.substring(1, json.length() - 2);
                } else {
                    return (T) json;
                }
            } else {
                return JsonUtil.getJsonMapper().readValue(json, cls);
            }
        } catch (IOException ex) {
            throw new VisalloClientApiException("Could not convert results to " + cls.getName(), ex);
        }
    }

    protected byte[] readAllFromHttpURLConnection(HttpURLConnection connection) throws IOException {
        try {
            InputStream in = connection.getInputStream();
            return inputStreamToByteArray(in);
        } catch (IOException ex) {
            if (isDebug()) {
                System.out.println(new String(inputStreamToByteArray(connection.getErrorStream())));
            }
            throw ex;
        }
    }

    private byte[] inputStreamToByteArray(InputStream in) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int read;
        byte[] buffer = new byte[10 * 1024];
        while ((read = in.read(buffer)) > 0) {
            out.write(buffer, 0, read);
        }
        return out.toByteArray();
    }

    public boolean isDebug() {
        return debug;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }
}