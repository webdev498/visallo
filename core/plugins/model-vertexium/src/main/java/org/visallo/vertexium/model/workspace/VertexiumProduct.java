package org.visallo.vertexium.model.workspace;

import org.visallo.core.model.workspace.product.Product;

public class VertexiumProduct extends Product {

    public VertexiumProduct(String id, String workspaceId, String title, String kind, String data, String extendedData) {
        super(id, workspaceId, kind, title, data, extendedData);
    }

}
