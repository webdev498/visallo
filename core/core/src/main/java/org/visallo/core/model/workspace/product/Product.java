package org.visallo.core.model.workspace.product;

import java.io.Serializable;

public abstract class Product implements Serializable {
    static long serialVersionUID = 1L;
    private final String id;
    private final String workspaceId;
    private final String title;
    private final String kind;
    private final String data;


    public Product(String id, String workspaceId, String kind, String title, String data) {
        this.id = id;
        this.workspaceId = workspaceId;
        this.kind = kind;
        this.data = data;
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public String getWorkspaceId() {
        return workspaceId;
    }

    public String getKind() {
        return kind;
    }

    public String getData() {
        return data;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Product product = (Product) o;

        return id.equals(product.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return "Product{" +
                "title='" + getTitle() + '\'' +
                ", id='" + id + '\'' +
                ", workspaceId='" + workspaceId + '\'' +
                '}';
    }
}
