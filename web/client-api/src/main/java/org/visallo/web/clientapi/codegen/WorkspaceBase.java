package org.visallo.web.clientapi.codegen;

import org.json.JSONObject;
import org.visallo.web.clientapi.CategoryBase;
import org.visallo.web.clientapi.VisalloApi;
import org.visallo.web.clientapi.model.*;
import org.visallo.web.clientapi.util.*;

import java.util.ArrayList;
import java.util.List;
import java.io.InputStream;

public abstract class WorkspaceBase extends CategoryBase {
    public WorkspaceBase(VisalloApi visalloApi) {
        super(visalloApi);
    }

    /**
     * @param title OPTIONAL
     */
    public ClientApiWorkspace postCreate(
        @Optional(name = "title") String title
    ) {
        List<VisalloApiBase.Parameter> parameters = new ArrayList<>();
        parameters.add(new VisalloApiBase.Parameter("title", title));
        return getVisalloApi().execute("POST", "/workspace/create", parameters, ClientApiWorkspace.class);
    }

    /**
     * @param ids OPTIONAL
     */
    public ClientApiWorkspaceEdges postEdges(
        @Optional(name = "ids[]") String[] ids
    ) {
        List<VisalloApiBase.Parameter> parameters = new ArrayList<>();
        parameters.add(new VisalloApiBase.Parameter("ids[]", ids));
        return getVisalloApi().execute("POST", "/workspace/edges", parameters, ClientApiWorkspaceEdges.class);
    }

    /**
     * @param data REQUIRED
     */
    public void postUpdate(
        @Required(name = "data") ClientApiWorkspaceUpdateData data
    ) {
        List<VisalloApiBase.Parameter> parameters = new ArrayList<>();
        parameters.add(new VisalloApiBase.Parameter("data", data));
        getVisalloApi().execute("POST", "/workspace/update", parameters, null);
    }

    /**
     * @param publishDatas REQUIRED
     */
    public ClientApiWorkspacePublishResponse postPublish(
        @Required(name = "publishData") ClientApiPublishItem[] publishDatas
    ) {
        List<VisalloApiBase.Parameter> parameters = new ArrayList<>();
        parameters.add(new VisalloApiBase.Parameter("publishData", publishDatas));
        return getVisalloApi().execute("POST", "/workspace/publish", parameters, ClientApiWorkspacePublishResponse.class);
    }

    /**
     * @param undoDatas REQUIRED
     */
    public ClientApiWorkspaceUndoResponse postUndo(
        @Required(name = "undoData") ClientApiUndoItem[] undoDatas
    ) {
        List<VisalloApiBase.Parameter> parameters = new ArrayList<>();
        parameters.add(new VisalloApiBase.Parameter("undoData", undoDatas));
        return getVisalloApi().execute("POST", "/workspace/undo", parameters, ClientApiWorkspaceUndoResponse.class);
    }

    /**
     * @param workspaceId REQUIRED
     * @param userName REQUIRED
     */
    public void postShareWithMe(
        @Required(name = "workspaceId") String workspaceId,
        @Required(name = "user-name") String userName
    ) {
        List<VisalloApiBase.Parameter> parameters = new ArrayList<>();
        parameters.add(new VisalloApiBase.Parameter("workspaceId", workspaceId));
        parameters.add(new VisalloApiBase.Parameter("user-name", userName));
        getVisalloApi().execute("POST", "/workspace/shareWithMe", parameters, null);
    }

    /**
     * @param workspaceId REQUIRED
     */
    public void delete(
        @Required(name = "workspaceId") String workspaceId
    ) {
        List<VisalloApiBase.Parameter> parameters = new ArrayList<>();
        parameters.add(new VisalloApiBase.Parameter("workspaceId", workspaceId));
        getVisalloApi().execute("DELETE", "/workspace", parameters, null);
    }

    /**
     */
    public ClientApiWorkspaces getAll(
        
    ) {
        List<VisalloApiBase.Parameter> parameters = new ArrayList<>();
        return getVisalloApi().execute("GET", "/workspace/all", parameters, ClientApiWorkspaces.class);
    }

    /**
     */
    public ClientApiWorkspaceDiff getDiff(
        
    ) {
        List<VisalloApiBase.Parameter> parameters = new ArrayList<>();
        return getVisalloApi().execute("GET", "/workspace/diff", parameters, ClientApiWorkspaceDiff.class);
    }

    /**
     * @param ids OPTIONAL
     */
    public ClientApiWorkspaceEdges getEdges(
        @Optional(name = "ids[]") String[] ids
    ) {
        List<VisalloApiBase.Parameter> parameters = new ArrayList<>();
        parameters.add(new VisalloApiBase.Parameter("ids[]", ids));
        return getVisalloApi().execute("GET", "/workspace/edges", parameters, ClientApiWorkspaceEdges.class);
    }

    /**
     */
    public ClientApiWorkspaceVertices getVertices(
        
    ) {
        List<VisalloApiBase.Parameter> parameters = new ArrayList<>();
        return getVisalloApi().execute("GET", "/workspace/vertices", parameters, ClientApiWorkspaceVertices.class);
    }

    /**
     * @param workspaceId REQUIRED
     */
    public ClientApiWorkspace get(
        @Required(name = "workspaceId") String workspaceId
    ) {
        List<VisalloApiBase.Parameter> parameters = new ArrayList<>();
        parameters.add(new VisalloApiBase.Parameter("workspaceId", workspaceId));
        return getVisalloApi().execute("GET", "/workspace", parameters, ClientApiWorkspace.class);
    }

}