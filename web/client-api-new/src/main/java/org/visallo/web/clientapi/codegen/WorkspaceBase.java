package org.visallo.web.clientapi.codegen;

import org.json.JSONObject;
import org.visallo.web.clientapi.CategoryBase;
import org.visallo.web.clientapi.VisalloApi;
import org.visallo.web.clientapi.model.*;

import java.util.ArrayList;
import java.util.List;

public abstract class WorkspaceBase extends CategoryBase {
    public WorkspaceBase(VisalloApi visalloApi) {
        super(visalloApi);
    }

    /**
     */
    public ClientApiWorkspaces getAll() {
        List<VisalloApiBase.Parameter> parameters = new ArrayList<>();
        return getVisalloApi().execute("GET", "/workspace/all", parameters, ClientApiWorkspaces.class);
    }

    /**
     */
    public ClientApiWorkspaceDiff getDiff() {
        List<VisalloApiBase.Parameter> parameters = new ArrayList<>();
        return getVisalloApi().execute("GET", "/workspace/diff", parameters, ClientApiWorkspaceDiff.class);
    }

    /**
     * @param ids OPTIONAL
     */
    public ClientApiWorkspaceEdges getEdges(String[] ids) {
        List<VisalloApiBase.Parameter> parameters = new ArrayList<>();
        parameters.add(new VisalloApiBase.Parameter("ids", ids));
        return getVisalloApi().execute("GET", "/workspace/edges", parameters, ClientApiWorkspaceEdges.class);
    }

    /**
     */
    public void getVertices() {
        List<VisalloApiBase.Parameter> parameters = new ArrayList<>();
        getVisalloApi().execute("GET", "/workspace/vertices", parameters, null);
    }

    /**
     * @param workspaceId REQUIRED
     */
    public ClientApiWorkspace get(String workspaceId) {
        List<VisalloApiBase.Parameter> parameters = new ArrayList<>();
        parameters.add(new VisalloApiBase.Parameter("workspaceId", workspaceId));
        return getVisalloApi().execute("GET", "/workspace", parameters, ClientApiWorkspace.class);
    }

    /**
     * @param title OPTIONAL
     */
    public ClientApiWorkspace postCreate(String title) {
        List<VisalloApiBase.Parameter> parameters = new ArrayList<>();
        parameters.add(new VisalloApiBase.Parameter("title", title));
        return getVisalloApi().execute("POST", "/workspace/create", parameters, ClientApiWorkspace.class);
    }

    /**
     * @param ids OPTIONAL
     */
    public ClientApiWorkspaceEdges postEdges(String[] ids) {
        List<VisalloApiBase.Parameter> parameters = new ArrayList<>();
        parameters.add(new VisalloApiBase.Parameter("ids", ids));
        return getVisalloApi().execute("POST", "/workspace/edges", parameters, ClientApiWorkspaceEdges.class);
    }

    /**
     * @param data REQUIRED
     */
    public void postUpdate(String data) {
        List<VisalloApiBase.Parameter> parameters = new ArrayList<>();
        parameters.add(new VisalloApiBase.Parameter("data", data));
        getVisalloApi().execute("POST", "/workspace/update", parameters, null);
    }

    /**
     * @param publishData REQUIRED
     */
    public ClientApiWorkspacePublishResponse postPublish(String publishData) {
        List<VisalloApiBase.Parameter> parameters = new ArrayList<>();
        parameters.add(new VisalloApiBase.Parameter("publishData", publishData));
        return getVisalloApi().execute("POST", "/workspace/publish", parameters, ClientApiWorkspacePublishResponse.class);
    }

    /**
     * @param undoData REQUIRED
     */
    public ClientApiWorkspaceUndoResponse postUndo(String undoData) {
        List<VisalloApiBase.Parameter> parameters = new ArrayList<>();
        parameters.add(new VisalloApiBase.Parameter("undoData", undoData));
        return getVisalloApi().execute("POST", "/workspace/undo", parameters, ClientApiWorkspaceUndoResponse.class);
    }

    /**
     * @param workspaceId REQUIRED
     * @param userName REQUIRED
     */
    public void postShareWithMe(String workspaceId, String userName) {
        List<VisalloApiBase.Parameter> parameters = new ArrayList<>();
        parameters.add(new VisalloApiBase.Parameter("workspaceId", workspaceId));
        parameters.add(new VisalloApiBase.Parameter("userName", userName));
        getVisalloApi().execute("POST", "/workspace/shareWithMe", parameters, null);
    }

    /**
     * @param workspaceId REQUIRED
     */
    public void delete(String workspaceId) {
        List<VisalloApiBase.Parameter> parameters = new ArrayList<>();
        parameters.add(new VisalloApiBase.Parameter("workspaceId", workspaceId));
        getVisalloApi().execute("DELETE", "/workspace", parameters, null);
    }

}