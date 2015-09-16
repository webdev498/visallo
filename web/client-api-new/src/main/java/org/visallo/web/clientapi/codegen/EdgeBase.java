package org.visallo.web.clientapi.codegen;

import org.json.JSONObject;
import org.visallo.web.clientapi.CategoryBase;
import org.visallo.web.clientapi.VisalloApi;
import org.visallo.web.clientapi.model.*;

import java.util.ArrayList;
import java.util.List;

public abstract class EdgeBase extends CategoryBase {
    public EdgeBase(VisalloApi visalloApi) {
        super(visalloApi);
    }

    /**
     * @param graphEdgeId REQUIRED
     * @param propertyName REQUIRED
     * @param propertyKey REQUIRED
     * @param startTime OPTIONAL
     * @param endTime OPTIONAL
     */
    public ClientApiHistoricalPropertyValues getPropertyHistory(String graphEdgeId, String propertyName, String propertyKey, Long startTime, Long endTime) {
        List<VisalloApiBase.Parameter> parameters = new ArrayList<>();
        parameters.add(new VisalloApiBase.Parameter("graphEdgeId", graphEdgeId));
        parameters.add(new VisalloApiBase.Parameter("propertyName", propertyName));
        parameters.add(new VisalloApiBase.Parameter("propertyKey", propertyKey));
        parameters.add(new VisalloApiBase.Parameter("startTime", startTime));
        parameters.add(new VisalloApiBase.Parameter("endTime", endTime));
        return getVisalloApi().execute("GET", "/edge/property/history", parameters, ClientApiHistoricalPropertyValues.class);
    }

    /**
     * @param edgeIds REQUIRED
     */
    public ClientApiEdgesExistsResponse getExists(String[] edgeIds) {
        List<VisalloApiBase.Parameter> parameters = new ArrayList<>();
        parameters.add(new VisalloApiBase.Parameter("edgeIds", edgeIds));
        return getVisalloApi().execute("GET", "/edge/exists", parameters, ClientApiEdgesExistsResponse.class);
    }

    /**
     * @param graphEdgeId REQUIRED
     */
    public ClientApiEdge getProperties(String graphEdgeId) {
        List<VisalloApiBase.Parameter> parameters = new ArrayList<>();
        parameters.add(new VisalloApiBase.Parameter("graphEdgeId", graphEdgeId));
        return getVisalloApi().execute("GET", "/edge/properties", parameters, ClientApiEdge.class);
    }

    /**
     * @param edgeId REQUIRED
     * @param propertyName REQUIRED
     * @param visibilitySource REQUIRED
     * @param propertyKey OPTIONAL
     */
    public ClientApiEdgePropertyDetails getPropertyDetails(String edgeId, String propertyName, String visibilitySource, String propertyKey) {
        List<VisalloApiBase.Parameter> parameters = new ArrayList<>();
        parameters.add(new VisalloApiBase.Parameter("edgeId", edgeId));
        parameters.add(new VisalloApiBase.Parameter("propertyName", propertyName));
        parameters.add(new VisalloApiBase.Parameter("visibilitySource", visibilitySource));
        parameters.add(new VisalloApiBase.Parameter("propertyKey", propertyKey));
        return getVisalloApi().execute("GET", "/edge/property/details", parameters, ClientApiEdgePropertyDetails.class);
    }

    /**
     * @param edgeId REQUIRED
     */
    public ClientApiEdgeDetails getDetails(String edgeId) {
        List<VisalloApiBase.Parameter> parameters = new ArrayList<>();
        parameters.add(new VisalloApiBase.Parameter("edgeId", edgeId));
        return getVisalloApi().execute("GET", "/edge/details", parameters, ClientApiEdgeDetails.class);
    }

    /**
     */
    public ClientApiVertexCount getCount() {
        List<VisalloApiBase.Parameter> parameters = new ArrayList<>();
        return getVisalloApi().execute("GET", "/edge/count", parameters, ClientApiVertexCount.class);
    }

    /**
     * @param edgeId REQUIRED
     * @param propertyName REQUIRED
     * @param value REQUIRED
     * @param visibilitySource REQUIRED
     * @param sourceInfo OPTIONAL
     * @param propertyKey OPTIONAL
     * @param metadata OPTIONAL
     */
    public void postProperty(String edgeId, String propertyName, String value, String visibilitySource, String sourceInfo, String propertyKey, String metadata) {
        List<VisalloApiBase.Parameter> parameters = new ArrayList<>();
        parameters.add(new VisalloApiBase.Parameter("edgeId", edgeId));
        parameters.add(new VisalloApiBase.Parameter("propertyName", propertyName));
        parameters.add(new VisalloApiBase.Parameter("value", value));
        parameters.add(new VisalloApiBase.Parameter("visibilitySource", visibilitySource));
        parameters.add(new VisalloApiBase.Parameter("sourceInfo", sourceInfo));
        parameters.add(new VisalloApiBase.Parameter("propertyKey", propertyKey));
        parameters.add(new VisalloApiBase.Parameter("metadata", metadata));
        getVisalloApi().execute("POST", "/edge/property", parameters, null);
    }

    /**
     * @param edgeId REQUIRED
     * @param propertyName REQUIRED
     * @param value REQUIRED
     * @param visibilitySource REQUIRED
     * @param sourceInfo OPTIONAL
     * @param propertyKey OPTIONAL
     * @param metadata OPTIONAL
     */
    public void postComment(String edgeId, String propertyName, String value, String visibilitySource, String sourceInfo, String propertyKey, String metadata) {
        List<VisalloApiBase.Parameter> parameters = new ArrayList<>();
        parameters.add(new VisalloApiBase.Parameter("edgeId", edgeId));
        parameters.add(new VisalloApiBase.Parameter("propertyName", propertyName));
        parameters.add(new VisalloApiBase.Parameter("value", value));
        parameters.add(new VisalloApiBase.Parameter("visibilitySource", visibilitySource));
        parameters.add(new VisalloApiBase.Parameter("sourceInfo", sourceInfo));
        parameters.add(new VisalloApiBase.Parameter("propertyKey", propertyKey));
        parameters.add(new VisalloApiBase.Parameter("metadata", metadata));
        getVisalloApi().execute("POST", "/edge/comment", parameters, null);
    }

    /**
     * @param edgeIds REQUIRED
     */
    public ClientApiEdgesExistsResponse postExists(String[] edgeIds) {
        List<VisalloApiBase.Parameter> parameters = new ArrayList<>();
        parameters.add(new VisalloApiBase.Parameter("edgeIds", edgeIds));
        return getVisalloApi().execute("POST", "/edge/exists", parameters, ClientApiEdgesExistsResponse.class);
    }

    /**
     * @param edgeIds REQUIRED
     */
    public ClientApiEdgeMultipleResponse postMultiple(String[] edgeIds) {
        List<VisalloApiBase.Parameter> parameters = new ArrayList<>();
        parameters.add(new VisalloApiBase.Parameter("edgeIds", edgeIds));
        return getVisalloApi().execute("POST", "/edge/multiple", parameters, ClientApiEdgeMultipleResponse.class);
    }

    /**
     * @param sourceGraphVertexId REQUIRED
     * @param destGraphVertexId REQUIRED
     * @param predicateLabel REQUIRED
     * @param visibilitySource REQUIRED
     * @param edgeId OPTIONAL
     */
    public ClientApiElement postCreate(String sourceGraphVertexId, String destGraphVertexId, String predicateLabel, String visibilitySource, String edgeId) {
        List<VisalloApiBase.Parameter> parameters = new ArrayList<>();
        parameters.add(new VisalloApiBase.Parameter("sourceGraphVertexId", sourceGraphVertexId));
        parameters.add(new VisalloApiBase.Parameter("destGraphVertexId", destGraphVertexId));
        parameters.add(new VisalloApiBase.Parameter("predicateLabel", predicateLabel));
        parameters.add(new VisalloApiBase.Parameter("visibilitySource", visibilitySource));
        parameters.add(new VisalloApiBase.Parameter("edgeId", edgeId));
        return getVisalloApi().execute("POST", "/edge/create", parameters, ClientApiElement.class);
    }

    /**
     * @param graphEdgeId REQUIRED
     * @param visibilitySource REQUIRED
     */
    public ClientApiElement postVisibility(String graphEdgeId, String visibilitySource) {
        List<VisalloApiBase.Parameter> parameters = new ArrayList<>();
        parameters.add(new VisalloApiBase.Parameter("graphEdgeId", graphEdgeId));
        parameters.add(new VisalloApiBase.Parameter("visibilitySource", visibilitySource));
        return getVisalloApi().execute("POST", "/edge/visibility", parameters, ClientApiElement.class);
    }

    /**
     * @param edgeId REQUIRED
     */
    public void delete(String edgeId) {
        List<VisalloApiBase.Parameter> parameters = new ArrayList<>();
        parameters.add(new VisalloApiBase.Parameter("edgeId", edgeId));
        getVisalloApi().execute("DELETE", "/edge", parameters, null);
    }

    /**
     * @param propertyName REQUIRED
     * @param propertyKey REQUIRED
     * @param edgeId REQUIRED
     */
    public void deleteProperty(String propertyName, String propertyKey, String edgeId) {
        List<VisalloApiBase.Parameter> parameters = new ArrayList<>();
        parameters.add(new VisalloApiBase.Parameter("propertyName", propertyName));
        parameters.add(new VisalloApiBase.Parameter("propertyKey", propertyKey));
        parameters.add(new VisalloApiBase.Parameter("edgeId", edgeId));
        getVisalloApi().execute("DELETE", "/edge/property", parameters, null);
    }

}