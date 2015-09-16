package org.visallo.web.clientapi.codegen;

import org.json.JSONObject;
import org.visallo.web.clientapi.CategoryBase;
import org.visallo.web.clientapi.VisalloApi;
import org.visallo.web.clientapi.model.*;

import java.util.ArrayList;
import java.util.List;

public abstract class VertexBase extends CategoryBase {
    public VertexBase(VisalloApi visalloApi) {
        super(visalloApi);
    }

    /**
     * @param graphVertexId REQUIRED
     * @param propertyKey REQUIRED
     */
    public void getHighlightedText(String graphVertexId, String propertyKey) {
        List<VisalloApiBase.Parameter> parameters = new ArrayList<>();
        parameters.add(new VisalloApiBase.Parameter("graphVertexId", graphVertexId));
        parameters.add(new VisalloApiBase.Parameter("propertyKey", propertyKey));
        getVisalloApi().execute("GET", "/vertex/highlighted-text", parameters, null);
    }

    /**
     * @param graphVertexId REQUIRED
     * @param download OPTIONAL
     * @param playback OPTIONAL
     * @param type OPTIONAL
     */
    public void getRaw(String graphVertexId, boolean download, boolean playback, String type) {
        List<VisalloApiBase.Parameter> parameters = new ArrayList<>();
        parameters.add(new VisalloApiBase.Parameter("graphVertexId", graphVertexId));
        parameters.add(new VisalloApiBase.Parameter("download", download));
        parameters.add(new VisalloApiBase.Parameter("playback", playback));
        parameters.add(new VisalloApiBase.Parameter("type", type));
        getVisalloApi().execute("GET", "/vertex/raw", parameters, null);
    }

    /**
     * @param vertexIds REQUIRED
     */
    public ClientApiVerticesExistsResponse getExists(String[] vertexIds) {
        List<VisalloApiBase.Parameter> parameters = new ArrayList<>();
        parameters.add(new VisalloApiBase.Parameter("vertexIds", vertexIds));
        return getVisalloApi().execute("GET", "/vertex/exists", parameters, ClientApiVerticesExistsResponse.class);
    }

    /**
     * @param graphVertexId REQUIRED
     * @param width OPTIONAL
     */
    public void getThumbnail(String graphVertexId, Integer width) {
        List<VisalloApiBase.Parameter> parameters = new ArrayList<>();
        parameters.add(new VisalloApiBase.Parameter("graphVertexId", graphVertexId));
        parameters.add(new VisalloApiBase.Parameter("width", width));
        getVisalloApi().execute("GET", "/vertex/thumbnail", parameters, null);
    }

    /**
     * @param graphVertexId REQUIRED
     * @param width OPTIONAL
     */
    public void getPosterFrame(String graphVertexId, Integer width) {
        List<VisalloApiBase.Parameter> parameters = new ArrayList<>();
        parameters.add(new VisalloApiBase.Parameter("graphVertexId", graphVertexId));
        parameters.add(new VisalloApiBase.Parameter("width", width));
        getVisalloApi().execute("GET", "/vertex/poster-frame", parameters, null);
    }

    /**
     * @param graphVertexId REQUIRED
     * @param width OPTIONAL
     */
    public void getVideoPreview(String graphVertexId, Integer width) {
        List<VisalloApiBase.Parameter> parameters = new ArrayList<>();
        parameters.add(new VisalloApiBase.Parameter("graphVertexId", graphVertexId));
        parameters.add(new VisalloApiBase.Parameter("width", width));
        getVisalloApi().execute("GET", "/vertex/video-preview", parameters, null);
    }

    /**
     * @param vertexId REQUIRED
     */
    public ClientApiVertexDetails getDetails(String vertexId) {
        List<VisalloApiBase.Parameter> parameters = new ArrayList<>();
        parameters.add(new VisalloApiBase.Parameter("vertexId", vertexId));
        return getVisalloApi().execute("GET", "/vertex/details", parameters, ClientApiVertexDetails.class);
    }

    /**
     * @param vertexId REQUIRED
     * @param propertyName REQUIRED
     * @param visibilitySource REQUIRED
     * @param propertyKey OPTIONAL
     */
    public ClientApiVertexPropertyDetails getPropertyDetails(String vertexId, String propertyName, String visibilitySource, String propertyKey) {
        List<VisalloApiBase.Parameter> parameters = new ArrayList<>();
        parameters.add(new VisalloApiBase.Parameter("vertexId", vertexId));
        parameters.add(new VisalloApiBase.Parameter("propertyName", propertyName));
        parameters.add(new VisalloApiBase.Parameter("visibilitySource", visibilitySource));
        parameters.add(new VisalloApiBase.Parameter("propertyKey", propertyKey));
        return getVisalloApi().execute("GET", "/vertex/property/details", parameters, ClientApiVertexPropertyDetails.class);
    }

    /**
     * @param graphVertexId REQUIRED
     * @param propertyName REQUIRED
     * @param workspaceId REQUIRED
     */
    public ClientApiDetectedObjects getDetectedObjects(String graphVertexId, String propertyName, String workspaceId) {
        List<VisalloApiBase.Parameter> parameters = new ArrayList<>();
        parameters.add(new VisalloApiBase.Parameter("graphVertexId", graphVertexId));
        parameters.add(new VisalloApiBase.Parameter("propertyName", propertyName));
        parameters.add(new VisalloApiBase.Parameter("workspaceId", workspaceId));
        return getVisalloApi().execute("GET", "/vertex/detected-objects", parameters, ClientApiDetectedObjects.class);
    }

    /**
     * @param graphVertexId REQUIRED
     * @param propertyName REQUIRED
     * @param propertyKey REQUIRED
     * @param Range OPTIONAL
     * @param download OPTIONAL
     * @param playback OPTIONAL
     */
    public void getProperty(String graphVertexId, String propertyName, String propertyKey, String Range, boolean download, boolean playback) {
        List<VisalloApiBase.Parameter> parameters = new ArrayList<>();
        parameters.add(new VisalloApiBase.Parameter("graphVertexId", graphVertexId));
        parameters.add(new VisalloApiBase.Parameter("propertyName", propertyName));
        parameters.add(new VisalloApiBase.Parameter("propertyKey", propertyKey));
        parameters.add(new VisalloApiBase.Parameter("Range", Range));
        parameters.add(new VisalloApiBase.Parameter("download", download));
        parameters.add(new VisalloApiBase.Parameter("playback", playback));
        getVisalloApi().execute("GET", "/vertex/property", parameters, null);
    }

    /**
     * @param graphVertexId REQUIRED
     * @param propertyName REQUIRED
     * @param propertyKey REQUIRED
     * @param startTime OPTIONAL
     * @param endTime OPTIONAL
     */
    public ClientApiHistoricalPropertyValues getPropertyHistory(String graphVertexId, String propertyName, String propertyKey, Long startTime, Long endTime) {
        List<VisalloApiBase.Parameter> parameters = new ArrayList<>();
        parameters.add(new VisalloApiBase.Parameter("graphVertexId", graphVertexId));
        parameters.add(new VisalloApiBase.Parameter("propertyName", propertyName));
        parameters.add(new VisalloApiBase.Parameter("propertyKey", propertyKey));
        parameters.add(new VisalloApiBase.Parameter("startTime", startTime));
        parameters.add(new VisalloApiBase.Parameter("endTime", endTime));
        return getVisalloApi().execute("GET", "/vertex/property/history", parameters, ClientApiHistoricalPropertyValues.class);
    }

    /**
     * @param graphVertexId REQUIRED
     * @param propertyName REQUIRED
     * @param propertyKey REQUIRED
     */
    public ClientApiTermMentionsResponse getTermMentions(String graphVertexId, String propertyName, String propertyKey) {
        List<VisalloApiBase.Parameter> parameters = new ArrayList<>();
        parameters.add(new VisalloApiBase.Parameter("graphVertexId", graphVertexId));
        parameters.add(new VisalloApiBase.Parameter("propertyName", propertyName));
        parameters.add(new VisalloApiBase.Parameter("propertyKey", propertyKey));
        return getVisalloApi().execute("GET", "/vertex/term-mentions", parameters, ClientApiTermMentionsResponse.class);
    }

    /**
     * @param graphVertexId REQUIRED
     */
    public ClientApiElement getProperties(String graphVertexId) {
        List<VisalloApiBase.Parameter> parameters = new ArrayList<>();
        parameters.add(new VisalloApiBase.Parameter("graphVertexId", graphVertexId));
        return getVisalloApi().execute("GET", "/vertex/properties", parameters, ClientApiElement.class);
    }

    /**
     * @param graphVertexId REQUIRED
     * @param offset OPTIONAL
     * @param size OPTIONAL
     * @param edgeLabel OPTIONAL
     * @param relatedVertexId OPTIONAL
     */
    public ClientApiVertexEdges getEdges(String graphVertexId, int offset, int size, String edgeLabel, String relatedVertexId) {
        List<VisalloApiBase.Parameter> parameters = new ArrayList<>();
        parameters.add(new VisalloApiBase.Parameter("graphVertexId", graphVertexId));
        parameters.add(new VisalloApiBase.Parameter("offset", offset));
        parameters.add(new VisalloApiBase.Parameter("size", size));
        parameters.add(new VisalloApiBase.Parameter("edgeLabel", edgeLabel));
        parameters.add(new VisalloApiBase.Parameter("relatedVertexId", relatedVertexId));
        return getVisalloApi().execute("GET", "/vertex/edges", parameters, ClientApiVertexEdges.class);
    }

    /**
     */
    public ClientApiVertexSearchResponse getSearch() {
        List<VisalloApiBase.Parameter> parameters = new ArrayList<>();
        return getVisalloApi().execute("GET", "/vertex/search", parameters, ClientApiVertexSearchResponse.class);
    }

    /**
     * @param lat REQUIRED
     * @param lon REQUIRED
     * @param radius REQUIRED
     */
    public ClientApiVertexSearchResponse getGeoSearch(double lat, double lon, double radius) {
        List<VisalloApiBase.Parameter> parameters = new ArrayList<>();
        parameters.add(new VisalloApiBase.Parameter("lat", lat));
        parameters.add(new VisalloApiBase.Parameter("lon", lon));
        parameters.add(new VisalloApiBase.Parameter("radius", radius));
        return getVisalloApi().execute("GET", "/vertex/geo-search", parameters, ClientApiVertexSearchResponse.class);
    }

    /**
     * @param sourceGraphVertexId REQUIRED
     * @param destGraphVertexId REQUIRED
     * @param hops REQUIRED
     * @param labels OPTIONAL
     */
    public void getFindPath(String sourceGraphVertexId, String destGraphVertexId, int hops, String[] labels) {
        List<VisalloApiBase.Parameter> parameters = new ArrayList<>();
        parameters.add(new VisalloApiBase.Parameter("sourceGraphVertexId", sourceGraphVertexId));
        parameters.add(new VisalloApiBase.Parameter("destGraphVertexId", destGraphVertexId));
        parameters.add(new VisalloApiBase.Parameter("hops", hops));
        parameters.add(new VisalloApiBase.Parameter("labels", labels));
        getVisalloApi().execute("GET", "/vertex/find-path", parameters, null);
    }

    /**
     */
    public ClientApiVertexCountsByConceptType getCountsByConceptType() {
        List<VisalloApiBase.Parameter> parameters = new ArrayList<>();
        return getVisalloApi().execute("GET", "/vertex/counts-by-concept-type", parameters, ClientApiVertexCountsByConceptType.class);
    }

    /**
     */
    public ClientApiVertexCount getCount() {
        List<VisalloApiBase.Parameter> parameters = new ArrayList<>();
        return getVisalloApi().execute("GET", "/vertex/count", parameters, ClientApiVertexCount.class);
    }

    /**
     * @param vertexIds REQUIRED
     */
    public ClientApiVerticesExistsResponse postExists(String[] vertexIds) {
        List<VisalloApiBase.Parameter> parameters = new ArrayList<>();
        parameters.add(new VisalloApiBase.Parameter("vertexIds", vertexIds));
        return getVisalloApi().execute("POST", "/vertex/exists", parameters, ClientApiVerticesExistsResponse.class);
    }

    /**
     */
    public ClientApiArtifactImportResponse postImport() {
        List<VisalloApiBase.Parameter> parameters = new ArrayList<>();
        return getVisalloApi().execute("POST", "/vertex/import", parameters, ClientApiArtifactImportResponse.class);
    }

    /**
     * @param artifactId REQUIRED
     * @param propertyKey REQUIRED
     * @param mentionStart REQUIRED
     * @param mentionEnd REQUIRED
     * @param sign REQUIRED
     * @param conceptId REQUIRED
     * @param visibilitySource REQUIRED
     * @param resolvedVertexId OPTIONAL
     * @param sourceInfo OPTIONAL
     */
    public void postResolveTerm(String artifactId, String propertyKey, long mentionStart, long mentionEnd, String sign, String conceptId, String visibilitySource, String resolvedVertexId, String sourceInfo) {
        List<VisalloApiBase.Parameter> parameters = new ArrayList<>();
        parameters.add(new VisalloApiBase.Parameter("artifactId", artifactId));
        parameters.add(new VisalloApiBase.Parameter("propertyKey", propertyKey));
        parameters.add(new VisalloApiBase.Parameter("mentionStart", mentionStart));
        parameters.add(new VisalloApiBase.Parameter("mentionEnd", mentionEnd));
        parameters.add(new VisalloApiBase.Parameter("sign", sign));
        parameters.add(new VisalloApiBase.Parameter("conceptId", conceptId));
        parameters.add(new VisalloApiBase.Parameter("visibilitySource", visibilitySource));
        parameters.add(new VisalloApiBase.Parameter("resolvedVertexId", resolvedVertexId));
        parameters.add(new VisalloApiBase.Parameter("sourceInfo", sourceInfo));
        getVisalloApi().execute("POST", "/vertex/resolve-term", parameters, null);
    }

    /**
     * @param termMentionId REQUIRED
     */
    public void postUnresolveTerm(String termMentionId) {
        List<VisalloApiBase.Parameter> parameters = new ArrayList<>();
        parameters.add(new VisalloApiBase.Parameter("termMentionId", termMentionId));
        getVisalloApi().execute("POST", "/vertex/unresolve-term", parameters, null);
    }

    /**
     * @param artifactId REQUIRED
     * @param title REQUIRED
     * @param conceptId REQUIRED
     * @param visibilitySource REQUIRED
     * @param graphVertexId OPTIONAL
     * @param sourceInfo OPTIONAL
     * @param originalPropertyKey OPTIONAL
     * @param x1 REQUIRED
     * @param x2 REQUIRED
     * @param y1 REQUIRED
     * @param y2 REQUIRED
     */
    public ClientApiElement postResolveDetectedObject(String artifactId, String title, String conceptId, String visibilitySource, String graphVertexId, String sourceInfo, String originalPropertyKey, double x1, double x2, double y1, double y2) {
        List<VisalloApiBase.Parameter> parameters = new ArrayList<>();
        parameters.add(new VisalloApiBase.Parameter("artifactId", artifactId));
        parameters.add(new VisalloApiBase.Parameter("title", title));
        parameters.add(new VisalloApiBase.Parameter("conceptId", conceptId));
        parameters.add(new VisalloApiBase.Parameter("visibilitySource", visibilitySource));
        parameters.add(new VisalloApiBase.Parameter("graphVertexId", graphVertexId));
        parameters.add(new VisalloApiBase.Parameter("sourceInfo", sourceInfo));
        parameters.add(new VisalloApiBase.Parameter("originalPropertyKey", originalPropertyKey));
        parameters.add(new VisalloApiBase.Parameter("x1", x1));
        parameters.add(new VisalloApiBase.Parameter("x2", x2));
        parameters.add(new VisalloApiBase.Parameter("y1", y1));
        parameters.add(new VisalloApiBase.Parameter("y2", y2));
        return getVisalloApi().execute("POST", "/vertex/resolve-detected-object", parameters, ClientApiElement.class);
    }

    /**
     * @param vertexId REQUIRED
     * @param multiValueKey REQUIRED
     */
    public ClientApiElement postUnresolveDetectedObject(String vertexId, String multiValueKey) {
        List<VisalloApiBase.Parameter> parameters = new ArrayList<>();
        parameters.add(new VisalloApiBase.Parameter("vertexId", vertexId));
        parameters.add(new VisalloApiBase.Parameter("multiValueKey", multiValueKey));
        return getVisalloApi().execute("POST", "/vertex/unresolve-detected-object", parameters, ClientApiElement.class);
    }

    /**
     * @param graphVertexId REQUIRED
     * @param propertyName REQUIRED
     * @param propertyKey OPTIONAL
     * @param value OPTIONAL
     * @param values OPTIONAL
     * @param visibilitySource REQUIRED
     * @param oldVisibilitySource OPTIONAL
     * @param sourceInfo OPTIONAL
     * @param metadata OPTIONAL
     */
    public ClientApiElement postProperty(String graphVertexId, String propertyName, String propertyKey, String value, String[] values, String visibilitySource, String oldVisibilitySource, String sourceInfo, String metadata) {
        List<VisalloApiBase.Parameter> parameters = new ArrayList<>();
        parameters.add(new VisalloApiBase.Parameter("graphVertexId", graphVertexId));
        parameters.add(new VisalloApiBase.Parameter("propertyName", propertyName));
        parameters.add(new VisalloApiBase.Parameter("propertyKey", propertyKey));
        parameters.add(new VisalloApiBase.Parameter("value", value));
        parameters.add(new VisalloApiBase.Parameter("values", values));
        parameters.add(new VisalloApiBase.Parameter("visibilitySource", visibilitySource));
        parameters.add(new VisalloApiBase.Parameter("oldVisibilitySource", oldVisibilitySource));
        parameters.add(new VisalloApiBase.Parameter("sourceInfo", sourceInfo));
        parameters.add(new VisalloApiBase.Parameter("metadata", metadata));
        return getVisalloApi().execute("POST", "/vertex/property", parameters, ClientApiElement.class);
    }

    /**
     * @param graphVertexId REQUIRED
     * @param propertyName REQUIRED
     * @param propertyKey OPTIONAL
     * @param value OPTIONAL
     * @param values OPTIONAL
     * @param visibilitySource REQUIRED
     * @param oldVisibilitySource OPTIONAL
     * @param sourceInfo OPTIONAL
     * @param metadata OPTIONAL
     */
    public ClientApiElement postComment(String graphVertexId, String propertyName, String propertyKey, String value, String[] values, String visibilitySource, String oldVisibilitySource, String sourceInfo, String metadata) {
        List<VisalloApiBase.Parameter> parameters = new ArrayList<>();
        parameters.add(new VisalloApiBase.Parameter("graphVertexId", graphVertexId));
        parameters.add(new VisalloApiBase.Parameter("propertyName", propertyName));
        parameters.add(new VisalloApiBase.Parameter("propertyKey", propertyKey));
        parameters.add(new VisalloApiBase.Parameter("value", value));
        parameters.add(new VisalloApiBase.Parameter("values", values));
        parameters.add(new VisalloApiBase.Parameter("visibilitySource", visibilitySource));
        parameters.add(new VisalloApiBase.Parameter("oldVisibilitySource", oldVisibilitySource));
        parameters.add(new VisalloApiBase.Parameter("sourceInfo", sourceInfo));
        parameters.add(new VisalloApiBase.Parameter("metadata", metadata));
        return getVisalloApi().execute("POST", "/vertex/comment", parameters, ClientApiElement.class);
    }

    /**
     * @param graphVertexId REQUIRED
     * @param visibilitySource REQUIRED
     */
    public ClientApiElement postVisibility(String graphVertexId, String visibilitySource) {
        List<VisalloApiBase.Parameter> parameters = new ArrayList<>();
        parameters.add(new VisalloApiBase.Parameter("graphVertexId", graphVertexId));
        parameters.add(new VisalloApiBase.Parameter("visibilitySource", visibilitySource));
        return getVisalloApi().execute("POST", "/vertex/visibility", parameters, ClientApiElement.class);
    }

    /**
     * @param vertexIds REQUIRED
     * @param fallbackToPublic OPTIONAL
     */
    public ClientApiVertexMultipleResponse postMultiple(String[] vertexIds, boolean fallbackToPublic) {
        List<VisalloApiBase.Parameter> parameters = new ArrayList<>();
        parameters.add(new VisalloApiBase.Parameter("vertexIds", vertexIds));
        parameters.add(new VisalloApiBase.Parameter("fallbackToPublic", fallbackToPublic));
        return getVisalloApi().execute("POST", "/vertex/multiple", parameters, ClientApiVertexMultipleResponse.class);
    }

    /**
     * @param vertexId OPTIONAL
     * @param conceptType REQUIRED
     * @param visibilitySource REQUIRED
     * @param properties OPTIONAL
     */
    public ClientApiElement postNew(String vertexId, String conceptType, String visibilitySource, String properties) {
        List<VisalloApiBase.Parameter> parameters = new ArrayList<>();
        parameters.add(new VisalloApiBase.Parameter("vertexId", vertexId));
        parameters.add(new VisalloApiBase.Parameter("conceptType", conceptType));
        parameters.add(new VisalloApiBase.Parameter("visibilitySource", visibilitySource));
        parameters.add(new VisalloApiBase.Parameter("properties", properties));
        return getVisalloApi().execute("POST", "/vertex/new", parameters, ClientApiElement.class);
    }

    /**
     */
    public ClientApiVertexSearchResponse postSearch() {
        List<VisalloApiBase.Parameter> parameters = new ArrayList<>();
        return getVisalloApi().execute("POST", "/vertex/search", parameters, ClientApiVertexSearchResponse.class);
    }

    /**
     * @param graphVertexId REQUIRED
     */
    public ClientApiElement postUploadImage(String graphVertexId) {
        List<VisalloApiBase.Parameter> parameters = new ArrayList<>();
        parameters.add(new VisalloApiBase.Parameter("graphVertexId", graphVertexId));
        return getVisalloApi().execute("POST", "/vertex/upload-image", parameters, ClientApiElement.class);
    }

    /**
     * @param graphVertexIds REQUIRED
     * @param limitParentConceptId OPTIONAL
     * @param limitEdgeLabel OPTIONAL
     * @param maxVerticesToReturn OPTIONAL
     */
    public ClientApiVertexFindRelatedResponse postFindRelated(String[] graphVertexIds, String limitParentConceptId, String limitEdgeLabel, long maxVerticesToReturn) {
        List<VisalloApiBase.Parameter> parameters = new ArrayList<>();
        parameters.add(new VisalloApiBase.Parameter("graphVertexIds", graphVertexIds));
        parameters.add(new VisalloApiBase.Parameter("limitParentConceptId", limitParentConceptId));
        parameters.add(new VisalloApiBase.Parameter("limitEdgeLabel", limitEdgeLabel));
        parameters.add(new VisalloApiBase.Parameter("maxVerticesToReturn", maxVerticesToReturn));
        return getVisalloApi().execute("POST", "/vertex/find-related", parameters, ClientApiVertexFindRelatedResponse.class);
    }

    /**
     * @param graphVertexId REQUIRED
     */
    public void delete(String graphVertexId) {
        List<VisalloApiBase.Parameter> parameters = new ArrayList<>();
        parameters.add(new VisalloApiBase.Parameter("graphVertexId", graphVertexId));
        getVisalloApi().execute("DELETE", "/vertex", parameters, null);
    }

    /**
     * @param graphVertexId REQUIRED
     * @param propertyName REQUIRED
     * @param propertyKey REQUIRED
     */
    public void deleteProperty(String graphVertexId, String propertyName, String propertyKey) {
        List<VisalloApiBase.Parameter> parameters = new ArrayList<>();
        parameters.add(new VisalloApiBase.Parameter("graphVertexId", graphVertexId));
        parameters.add(new VisalloApiBase.Parameter("propertyName", propertyName));
        parameters.add(new VisalloApiBase.Parameter("propertyKey", propertyKey));
        getVisalloApi().execute("DELETE", "/vertex/property", parameters, null);
    }

}