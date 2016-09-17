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
    public static final String EDGE_POSITION = "http://visallo.org/workspace/product#entityPosition";

    @Override
    public void update(JSONObject params, Graph graph, Vertex workspaceVertex, VertexBuilder vertexBuilder, User user, Visibility visibility, Authorizations authorizations) {
        JSONObject updateVertices = params.optJSONObject("updateVertices");
        if (updateVertices != null) {
            Vertex productVertex = graph.getVertex(vertexBuilder.getVertexId(), authorizations);
            List<String> vertexIds = Lists.newArrayList(updateVertices.keys());
            for (String id : vertexIds) {
                JSONObject position = updateVertices.getJSONObject(id);
                String edgeId = getEdgeId(productVertex.getId(), id);
                EdgeBuilderByVertexId edgeBuilder = graph.prepareEdge(edgeId, productVertex.getId(), id, WORKSPACE_PRODUCT_TO_ENTITY_RELATIONSHIP_IRI, visibility);
                edgeBuilder.setProperty(EDGE_POSITION, position.toString(), visibility);
                edgeBuilder.save(authorizations);
            }
        }
        JSONArray removeVertices = params.optJSONArray("removeVertices");
        if (removeVertices != null) {
            JSONUtil.toList(removeVertices).stream().forEach(id -> {
                graph.softDeleteEdge(getEdgeId(vertexBuilder.getVertexId(), (String) id), authorizations);
            });
        }
    }

    @Override
    public JSONObject get(JSONObject params, Graph graph, Vertex workspaceVertex, Vertex productVertex, User user, Authorizations authorizations) {
        JSONObject extendedData = new JSONObject();

        boolean includeVertices = params.optBoolean("includeVertices");
        boolean includeEdges = params.optBoolean("includeEdges");
        String id = productVertex.getId();

        if (includeVertices || includeEdges) {
            List<String> vertexIds = Lists.newArrayList(productVertex.getVertexIds(Direction.OUT, WORKSPACE_PRODUCT_TO_ENTITY_RELATIONSHIP_IRI, authorizations));

            JSONArray edges = new JSONArray();
            JSONArray vertices = new JSONArray();

            if (includeVertices) {
                Iterable<Edge> productVertexEdges = productVertex.getEdges(Direction.OUT, WORKSPACE_PRODUCT_TO_ENTITY_RELATIONSHIP_IRI, authorizations);
                for (Edge propertyVertexEdge : productVertexEdges) {
                    String other = propertyVertexEdge.getOtherVertexId(id);
                    if (includeVertices) {
                        JSONObject vertex = new JSONObject();
                        vertex.put("id", other);
                        String positionStr = (String) propertyVertexEdge.getPropertyValue(EDGE_POSITION);
                        JSONObject position;
                        if (positionStr == null) {
                            position = new JSONObject();
                            position.put("x", 0);
                            position.put("y", 0);
                        } else {
                            if (positionStr.startsWith("[")) {
                                JSONArray legacy = new JSONArray(positionStr);
                                position = new JSONObject();
                                position.put("x", legacy.get(0));
                                position.put("y", legacy.get(1));
                            } else {
                                position = new JSONObject(positionStr);
                            }
                        }
                        vertex.put("pos", position);
                        vertices.put(vertex);
                    }
                }
                extendedData.put("vertices", vertices);
            }

            if (includeEdges) {
                Iterable<RelatedEdge> relatedEdges = graph.findRelatedEdgeSummary(vertexIds, authorizations);
                for (RelatedEdge relatedEdge : relatedEdges) {
                    if (includeEdges) {
                        JSONObject edge = new JSONObject();
                        edge.put("edgeId", relatedEdge.getEdgeId());
                        edge.put("label", relatedEdge.getLabel());
                        edge.put("outVertexId", relatedEdge.getOutVertexId());
                        edge.put("inVertexId", relatedEdge.getInVertexId());
                        edges.put(edge);
                    }
                }
                extendedData.put("edges", edges);
            }
        }

        return extendedData;
    }

    private String getEdgeId(String productId, String vertexId) {
        return productId + "_hasVertex_" + vertexId;
    }
}
