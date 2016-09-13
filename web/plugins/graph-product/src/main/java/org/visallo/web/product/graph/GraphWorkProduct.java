package org.visallo.web.product.graph;

import com.google.common.collect.Lists;
import org.json.JSONArray;
import org.json.JSONObject;
import org.vertexium.*;
import org.visallo.core.model.workspace.product.WorkProduct;
import org.visallo.core.user.User;
import org.visallo.core.util.JSONUtil;
import org.visallo.core.util.VisalloLogger;
import org.visallo.core.util.VisalloLoggerFactory;

import java.util.List;

public class GraphWorkProduct implements WorkProduct {
    private static final VisalloLogger LOGGER = VisalloLoggerFactory.getLogger(GraphWorkProduct.class);
    public static final String WORKSPACE_PRODUCT_TO_ENTITY_RELATIONSHIP_IRI = "http://visallo.org/workspace/product#toEntity";


    public GraphWorkProduct() {
        LOGGER.info("Created graph work product");
    }

    @Override
    public void update(JSONObject params, Graph graph, Vertex workspaceVertex, VertexBuilder vertexBuilder, User user, Visibility visibility, Authorizations authorizations) {
        JSONArray addVertices = params.optJSONArray("addVertices");

        if (addVertices != null) {
            String productId = vertexBuilder.getVertexId();
            JSONUtil.toList(addVertices).stream().forEach(vertexId ->  graph.addEdge(
                productId + "_hasVertex_" + vertexId,
                productId,
                (String) vertexId,
                WORKSPACE_PRODUCT_TO_ENTITY_RELATIONSHIP_IRI,
                vertexBuilder.getVisibility(),
                authorizations
            ));
        }
    }

    @Override
    public JSONObject get(JSONObject params, Graph graph, Vertex vertex, Vertex productVertex, User user, Authorizations authorizations) {
        JSONObject extendedData = new JSONObject();

        List<String> vertexIds = Lists.newArrayList(productVertex.getVertexIds(Direction.OUT, WORKSPACE_PRODUCT_TO_ENTITY_RELATIONSHIP_IRI, authorizations));

        if (params.optBoolean("includeEdges")) {
            JSONArray edges = new JSONArray();
            Iterable<RelatedEdge> relatedEdges = graph.findRelatedEdgeSummary(vertexIds, authorizations);
            for (RelatedEdge relatedEdge : relatedEdges) {
                JSONObject edge = new JSONObject();
                edge.put("edgeId", relatedEdge.getEdgeId());
                edge.put("label", relatedEdge.getLabel());
                edge.put("outVertexId", relatedEdge.getOutVertexId());
                edge.put("inVertexId", relatedEdge.getInVertexId());
                edges.put(edge);
            }
            extendedData.put("edges", edges);
        }

        if (params.optBoolean("includeVertices")) {
            extendedData.put("vertices", new JSONArray(vertexIds));
        }

        return extendedData;
    }
}
