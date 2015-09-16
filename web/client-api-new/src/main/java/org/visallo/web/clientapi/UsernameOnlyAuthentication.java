package org.visallo.web.clientapi;

import org.visallo.web.clientapi.model.ClientApiUser;
import org.visallo.web.clientapi.model.ClientApiWorkspace;

import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

public class UsernameOnlyAuthentication {
    public static ClientApiUser logIn(VisalloApi api, String username) {
        try {
            URL url = new URL(api.getBasePath() + "/login");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setUseCaches(false);
            conn.setDoOutput(true);

            DataOutputStream out = new DataOutputStream(conn.getOutputStream());
            out.writeBytes("username=" + URLEncoder.encode(username, "UTF-8"));
            out.flush();
            out.close();

            int code = conn.getResponseCode();
            if (code != 200) {
                throw new VisalloClientApiException("Invalid response code. Expected 200. Found " + code);
            }
            Map<String, List<String>> responseHeaders = conn.getHeaderFields();
            List<String> cookies = responseHeaders.get("Set-Cookie");
            if (cookies == null) {
                throw new VisalloClientApiException("Could not find cookie header in response");
            }
            for (String cookie : cookies) {
                if (!cookie.startsWith("JSESSIONID=")) {
                    continue;
                }
                String cookieValue = cookie.substring("JSESSIONID=".length());
                int sep = cookieValue.indexOf(';');
                if (sep > 0) {
                    cookieValue = cookieValue.substring(0, sep);
                }
                api.setSessionCookie(cookieValue);

                ClientApiUser user = api.getUser().getMe();
                api.setCsrfToken(user.getCsrfToken());
                if (user.getCurrentWorkspaceId() != null) {
                    api.setWorkspaceId(user.getCurrentWorkspaceId());
                } else {
                    List<ClientApiWorkspace> workspaces = api.getWorkspace().getAll().getWorkspaces();
                    if (workspaces.size() > 0) {
                        api.setWorkspaceId(workspaces.get(0).getWorkspaceId());
                    } else {
                        ClientApiWorkspace newWorkspace = api.getWorkspace().postCreate("Default - " + username);
                        api.setWorkspaceId(newWorkspace.getWorkspaceId());
                    }
                }

                return user;
            }
            throw new VisalloClientApiException("Could not find JSESSIONID cookie");
        } catch (Exception e) {
            throw new VisalloClientApiException("Could not login: " + api.getBasePath() + " (username: " + username + ")", e);
        }
    }
}
