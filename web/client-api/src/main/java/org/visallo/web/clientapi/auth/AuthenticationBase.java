package org.visallo.web.clientapi.auth;

import org.visallo.web.clientapi.VisalloApi;
import org.visallo.web.clientapi.VisalloClientApiException;
import org.visallo.web.clientapi.model.ClientApiUser;
import org.visallo.web.clientapi.model.ClientApiWorkspace;

import java.util.List;

public abstract class AuthenticationBase {
    protected static ClientApiUser updateVisalloApiWithUserAndWorkspace(VisalloApi api, String defaultWorkspaceTitle) {
        ClientApiUser user = api.getUser().getMe();
        api.setUser(user);
        if (user.getCurrentWorkspaceId() != null) {
            api.setWorkspaceId(user.getCurrentWorkspaceId());
        } else {
            List<ClientApiWorkspace> workspaces = api.getWorkspace().getAll().getWorkspaces();
            if (workspaces.size() > 0) {
                api.setWorkspaceId(workspaces.get(0).getWorkspaceId());
            } else {
                ClientApiWorkspace newWorkspace = api.getWorkspace().postCreate(defaultWorkspaceTitle);
                api.setWorkspaceId(newWorkspace.getWorkspaceId());
            }
        }
        return user;
    }

    protected static void updateVisalloApiWithSessionCookie(VisalloApi api, List<String> cookies) {
        String cookieValue = findJSessionIdCookieValue(cookies);
        if (cookieValue == null) {
            throw new VisalloClientApiException("Could not find JSESSIONID cookie");
        }
        api.setSessionCookie(cookieValue);
    }

    private static String findJSessionIdCookieValue(List<String> cookies) {
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
            return cookieValue;
        }
        return null;
    }
}
