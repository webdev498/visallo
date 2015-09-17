package org.visallo.web.clientapi;

import org.visallo.web.clientapi.codegen.WorkspaceBase;
import org.visallo.web.clientapi.model.*;

import java.util.ArrayList;
import java.util.List;

public class Workspace extends WorkspaceBase {
    public Workspace(VisalloApi visalloApi) {
        super(visalloApi);
    }

    public void setUserAccess(String userId, WorkspaceAccess access) throws VisalloClientApiException {
        ClientApiWorkspaceUpdateData addUser2WorkspaceUpdate = new ClientApiWorkspaceUpdateData();
        ClientApiWorkspaceUpdateData.UserUpdate addUser2Update = new ClientApiWorkspaceUpdateData.UserUpdate();
        addUser2Update.setUserId(userId);
        addUser2Update.setAccess(access);
        addUser2WorkspaceUpdate.getUserUpdates().add(addUser2Update);
        postUpdate(addUser2WorkspaceUpdate);
    }

    public ClientApiWorkspacePublishResponse postPublishAll(List<ClientApiWorkspaceDiff.Item> diffItems) {
        List<ClientApiPublishItem> publishItems = new ArrayList<>();
        for (ClientApiWorkspaceDiff.Item diffItem : diffItems) {
            publishItems.add(workspaceDiffItemToPublishItem(diffItem));
        }
        return postPublish(publishItems.toArray(new ClientApiPublishItem[publishItems.size()]));
    }

    public ClientApiPublishItem workspaceDiffItemToPublishItem(ClientApiWorkspaceDiff.Item workspaceDiffItem) {
        if (workspaceDiffItem instanceof ClientApiWorkspaceDiff.VertexItem) {
            ClientApiWorkspaceDiff.VertexItem vertexDiffItem = (ClientApiWorkspaceDiff.VertexItem) workspaceDiffItem;
            ClientApiVertexPublishItem publishItem = new ClientApiVertexPublishItem();
            publishItem.setAction(ClientApiPublishItem.Action.addOrUpdate);
            publishItem.setVertexId(vertexDiffItem.getVertexId());
            return publishItem;
        } else if (workspaceDiffItem instanceof ClientApiWorkspaceDiff.PropertyItem) {
            ClientApiWorkspaceDiff.PropertyItem propertyDiffItem = (ClientApiWorkspaceDiff.PropertyItem) workspaceDiffItem;
            ClientApiPropertyPublishItem publishItem = new ClientApiPropertyPublishItem();
            publishItem.setElementId(propertyDiffItem.getElementId());
            publishItem.setKey(propertyDiffItem.getKey());
            publishItem.setName(propertyDiffItem.getName());
            publishItem.setVisibilityString(propertyDiffItem.getVisibilityString());
            return publishItem;
        } else if (workspaceDiffItem instanceof ClientApiWorkspaceDiff.EdgeItem) {
            ClientApiWorkspaceDiff.EdgeItem edgeDiffItem = (ClientApiWorkspaceDiff.EdgeItem) workspaceDiffItem;
            ClientApiRelationshipPublishItem publishItem = new ClientApiRelationshipPublishItem();
            publishItem.setEdgeId(edgeDiffItem.getEdgeId());
            return publishItem;
        } else {
            throw new VisalloClientApiException("Unhandled WorkspaceDiffItem type: " + workspaceDiffItem.getType());
        }
    }

    public ClientApiWorkspaceUndoResponse postUndoAll(List<ClientApiWorkspaceDiff.Item> diffItems) {
        List<ClientApiUndoItem> undoItems = new ArrayList<>();
        for (ClientApiWorkspaceDiff.Item diffItem : diffItems) {
            undoItems.add(workspaceDiffItemToUndoItem(diffItem));
        }
        return postUndo(undoItems.toArray(new ClientApiUndoItem[undoItems.size()]));
    }

    public ClientApiUndoItem workspaceDiffItemToUndoItem(ClientApiWorkspaceDiff.Item workspaceDiffItem) {
        if (workspaceDiffItem instanceof ClientApiWorkspaceDiff.VertexItem) {
            ClientApiWorkspaceDiff.VertexItem vertexDiffItem = (ClientApiWorkspaceDiff.VertexItem) workspaceDiffItem;
            ClientApiVertexUndoItem undoItem = new ClientApiVertexUndoItem();
            undoItem.setVertexId(vertexDiffItem.getVertexId());
            return undoItem;
        } else if (workspaceDiffItem instanceof ClientApiWorkspaceDiff.PropertyItem) {
            ClientApiWorkspaceDiff.PropertyItem propertyDiffItem = (ClientApiWorkspaceDiff.PropertyItem) workspaceDiffItem;
            ClientApiPropertyUndoItem undoItem = new ClientApiPropertyUndoItem();
            undoItem.setElementId(propertyDiffItem.getElementId());
            undoItem.setKey(propertyDiffItem.getKey());
            undoItem.setName(propertyDiffItem.getName());
            undoItem.setVisibilityString(propertyDiffItem.getVisibilityString());
            return undoItem;
        } else if (workspaceDiffItem instanceof ClientApiWorkspaceDiff.EdgeItem) {
            ClientApiWorkspaceDiff.EdgeItem edgeDiffItem = (ClientApiWorkspaceDiff.EdgeItem) workspaceDiffItem;
            ClientApiRelationshipUndoItem undoItem = new ClientApiRelationshipUndoItem();
            undoItem.setEdgeId(edgeDiffItem.getEdgeId());
            return undoItem;
        } else {
            throw new VisalloClientApiException("Unhandled WorkspaceDiffItem type: " + workspaceDiffItem.getType());
        }
    }
}