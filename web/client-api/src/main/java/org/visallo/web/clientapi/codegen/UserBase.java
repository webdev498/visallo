package org.visallo.web.clientapi.codegen;

import org.json.JSONObject;
import org.visallo.web.clientapi.CategoryBase;
import org.visallo.web.clientapi.VisalloApi;
import org.visallo.web.clientapi.model.*;

import java.util.ArrayList;
import java.util.List;

public abstract class UserBase extends CategoryBase {
    public UserBase(VisalloApi visalloApi) {
        super(visalloApi);
    }

    /**
     */
    public ClientApiUser getMe() {
        List<VisalloApiBase.Parameter> parameters = new ArrayList<>();
        return getVisalloApi().execute("GET", "/user/me", parameters, ClientApiUser.class);
    }

    /**
     * @param q OPTIONAL
     * @param workspaceId OPTIONAL
     * @param userIds OPTIONAL
     */
    public ClientApiUsers getAll(String q, String workspaceId, String[] userIds) {
        List<VisalloApiBase.Parameter> parameters = new ArrayList<>();
        parameters.add(new VisalloApiBase.Parameter("q", q));
        parameters.add(new VisalloApiBase.Parameter("workspaceId", workspaceId));
        parameters.add(new VisalloApiBase.Parameter("userIds", userIds));
        return getVisalloApi().execute("GET", "/user/all", parameters, ClientApiUsers.class);
    }

    /**
     * @param userName REQUIRED
     */
    public ClientApiUser get(String userName) {
        List<VisalloApiBase.Parameter> parameters = new ArrayList<>();
        parameters.add(new VisalloApiBase.Parameter("userName", userName));
        return getVisalloApi().execute("GET", "/user", parameters, ClientApiUser.class);
    }

    /**
     * @param uiPreferences OPTIONAL
     * @param name OPTIONAL
     * @param value OPTIONAL
     */
    public JSONObject postUiPreferences(String uiPreferences, String name, String value) {
        List<VisalloApiBase.Parameter> parameters = new ArrayList<>();
        parameters.add(new VisalloApiBase.Parameter("uiPreferences", uiPreferences));
        parameters.add(new VisalloApiBase.Parameter("name", name));
        parameters.add(new VisalloApiBase.Parameter("value", value));
        return getVisalloApi().execute("POST", "/user/ui-preferences", parameters, JSONObject.class);
    }

    /**
     * @param q OPTIONAL
     * @param workspaceId OPTIONAL
     * @param userIds OPTIONAL
     */
    public ClientApiUsers postAll(String q, String workspaceId, String[] userIds) {
        List<VisalloApiBase.Parameter> parameters = new ArrayList<>();
        parameters.add(new VisalloApiBase.Parameter("q", q));
        parameters.add(new VisalloApiBase.Parameter("workspaceId", workspaceId));
        parameters.add(new VisalloApiBase.Parameter("userIds", userIds));
        return getVisalloApi().execute("POST", "/user/all", parameters, ClientApiUsers.class);
    }

    /**
     * @param userName REQUIRED
     * @param auth REQUIRED
     */
    public JSONObject postAuthAdd(String userName, String auth) {
        List<VisalloApiBase.Parameter> parameters = new ArrayList<>();
        parameters.add(new VisalloApiBase.Parameter("userName", userName));
        parameters.add(new VisalloApiBase.Parameter("auth", auth));
        return getVisalloApi().execute("POST", "/user/auth/add", parameters, JSONObject.class);
    }

    /**
     * @param userName REQUIRED
     * @param auth REQUIRED
     */
    public JSONObject postAuthRemove(String userName, String auth) {
        List<VisalloApiBase.Parameter> parameters = new ArrayList<>();
        parameters.add(new VisalloApiBase.Parameter("userName", userName));
        parameters.add(new VisalloApiBase.Parameter("auth", auth));
        return getVisalloApi().execute("POST", "/user/auth/remove", parameters, JSONObject.class);
    }

    /**
     * @param userName REQUIRED
     */
    public void postDelete(String userName) {
        List<VisalloApiBase.Parameter> parameters = new ArrayList<>();
        parameters.add(new VisalloApiBase.Parameter("userName", userName));
        getVisalloApi().execute("POST", "/user/delete", parameters, null);
    }

    /**
     * @param userName REQUIRED
     * @param privileges REQUIRED
     */
    public JSONObject postPrivilegesUpdate(String userName, String privileges) {
        List<VisalloApiBase.Parameter> parameters = new ArrayList<>();
        parameters.add(new VisalloApiBase.Parameter("userName", userName));
        parameters.add(new VisalloApiBase.Parameter("privileges", privileges));
        return getVisalloApi().execute("POST", "/user/privileges/update", parameters, JSONObject.class);
    }

}