package org.visallo.web.clientapi.auth;

import org.visallo.web.clientapi.VisalloApi;
import org.visallo.web.clientapi.VisalloClientApiException;
import org.visallo.web.clientapi.model.ClientApiUser;

import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

public class UsernameOnlyAuthentication extends AuthenticationBase {
    public static ClientApiUser logIn(VisalloApi api, String username) {
        String defaultWorkspaceTitle = "Default - " + username;
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
            updateVisalloApiWithSessionCookie(api, cookies);
            return updateVisalloApiWithUserAndWorkspace(api, defaultWorkspaceTitle);
        } catch (Exception e) {
            throw new VisalloClientApiException("Could not login: " + api.getBasePath() + " (username: " + username + ")", e);
        }
    }
}
