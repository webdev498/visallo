package org.visallo.web.clientapi.model;

public class ClientApiProduct implements ClientApiObject {
    public String id;
    public String workspaceId;
    public String title;
    public String kind;
    public String data;
    public String extendedData;

    public ClientApiProduct(String id, String workspaceId, String title, String kind, String data, String extendedData) {
        this.id = id;
        this.workspaceId = workspaceId;
        this.title = title;
        this.kind = kind;
        this.data = data;
        this.extendedData = extendedData;
    }
}
