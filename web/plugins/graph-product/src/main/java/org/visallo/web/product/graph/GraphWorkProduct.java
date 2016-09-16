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
import java.util.Map;

public class GraphWorkProduct implements WorkProduct {
    private static final VisalloLogger LOGGER = VisalloLoggerFactory.getLogger(GraphWorkProduct.class);
    public static final String WORKSPACE_PRODUCT_TO_ENTITY_RELATIONSHIP_IRI = "http://visallo.org/workspace/product#toEntity";
    public static final String EDGE_POSITION = "http://visallo.org/workspace/product#entityPosition";


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

        JSONArray updateVertices = params.optJSONArray("updateVertices");
        if (updateVertices != null) {
            Vertex productVertex = graph.getVertex(vertexBuilder.getVertexId(), authorizations);
            List<EdgeInfo> edgeInfos = Lists.newArrayList(
                productVertex.getEdgeInfos(Direction.OUT, WORKSPACE_PRODUCT_TO_ENTITY_RELATIONSHIP_IRI, authorizations)
            );
            JSONUtil.toList(updateVertices).stream().forEach(update -> {
                Map<String, Object> updateObj = (Map) update;
                String id = (String) updateObj.get("id");
                List<Integer> position = (List) updateObj.get("pos");
                EdgeInfo found = edgeInfos.stream().filter(edgeInfo -> edgeInfo.getVertexId().equals(id)).findFirst().get();

                EdgeBuilderByVertexId edgeBuilder = graph.prepareEdge(found.getEdgeId(), productVertex.getId(), found.getVertexId(), found.getLabel(), visibility);
                edgeBuilder.setProperty(EDGE_POSITION, position.toString(), visibility);

                edgeBuilder.save(authorizations);
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
                        String position = (String) propertyVertexEdge.getPropertyValue(EDGE_POSITION);
                        if (position == null) {
                            position = "[0,0]";
                        }
                        vertex.put("pos", new JSONArray(position));
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
}
