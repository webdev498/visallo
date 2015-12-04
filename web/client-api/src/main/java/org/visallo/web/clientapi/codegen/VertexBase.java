package org.visallo.web.clientapi.codegen;

import org.json.JSONObject;
import org.visallo.web.clientapi.CategoryBase;
import org.visallo.web.clientapi.VisalloApi;
import org.visallo.web.clientapi.model.*;
import org.visallo.web.clientapi.util.*;

import java.util.ArrayList;
import java.util.List;
import java.io.InputStream;

public abstract class VertexBase extends CategoryBase {
    public VertexBase(VisalloApi visalloApi) {
        super(visalloApi);
    }

    /**
     * @param graphVertexId REQUIRED
     * @param propertyKey REQUIRED
     */
    public String getHighlightedText(
        @Required(name = "graphVertexId") String graphVertexId,
        @Required(name = "propertyKey") String propertyKey
    ) {
        List<VisalloApiBase.Parameter> parameters = new ArrayList<VisalloApiBase.Parameter>();
        parameters.add(new VisalloApiBase.Parameter("graphVertexId", graphVertexId));
        parameters.add(new VisalloApiBase.Parameter("propertyKey", propertyKey));
        return getVisalloApi().execute("GET", "/vertex/highlighted-text", parameters, String.class);
    }

    /**
     * @param graphVertexId REQUIRED
     * @param download OPTIONAL
     * @param playback OPTIONAL
     * @param type OPTIONAL
     */
    public InputStream getRaw(
        @Required(name = "graphVertexId") String graphVertexId,
        @Optional(name = "download", defaultValue = "false") boolean download,
        @Optional(name = "playback", defaultValue = "false") boolean playback,
        @Optional(name = "type") String type
    ) {
        List<VisalloApiBase.Parameter> parameters = new ArrayList<VisalloApiBase.Parameter>();
        parameters.add(new VisalloApiBase.Parameter("graphVertexId", graphVertexId));
        parameters.add(new VisalloApiBase.Parameter("download", download));
        parameters.add(new VisalloApiBase.Parameter("playback", playback));
        parameters.add(new VisalloApiBase.Parameter("type", type));
        return getVisalloApi().execute("GET", "/vertex/raw", parameters, InputStream.class);
    }

    /**
     * @param vertexIds REQUIRED
     */
    public ClientApiVerticesExistsResponse getExists(
        @Required(name = "vertexIds[]") String[] vertexIds
    ) {
        List<VisalloApiBase.Parameter> parameters = new ArrayList<VisalloApiBase.Parameter>();
        parameters.add(new VisalloApiBase.Parameter("vertexIds[]", vertexIds));
        return getVisalloApi().execute("GET", "/vertex/exists", parameters, ClientApiVerticesExistsResponse.class);
    }

    /**
     * @param graphVertexId REQUIRED
     * @param width OPTIONAL
     */
    public void getThumbnail(
        @Required(name = "graphVertexId") String graphVertexId,
        @Optional(name = "width") Integer width
    ) {
        List<VisalloApiBase.Parameter> parameters = new ArrayList<VisalloApiBase.Parameter>();
        parameters.add(new VisalloApiBase.Parameter("graphVertexId", graphVertexId));
        parameters.add(new VisalloApiBase.Parameter("width", width));
        getVisalloApi().execute("GET", "/vertex/thumbnail", parameters, null);
    }

    /**
     * @param graphVertexId REQUIRED
     * @param width OPTIONAL
     */
    public void getPosterFrame(
        @Required(name = "graphVertexId") String graphVertexId,
        @Optional(name = "width") Integer width
    ) {
        List<VisalloApiBase.Parameter> parameters = new ArrayList<VisalloApiBase.Parameter>();
        parameters.add(new VisalloApiBase.Parameter("graphVertexId", graphVertexId));
        parameters.add(new VisalloApiBase.Parameter("width", width));
        getVisalloApi().execute("GET", "/vertex/poster-frame", parameters, null);
    }

    /**
     * @param graphVertexId REQUIRED
     * @param width OPTIONAL
     */
    public void getVideoPreview(
        @Required(name = "graphVertexId") String graphVertexId,
        @Optional(name = "width") Integer width
    ) {
        List<VisalloApiBase.Parameter> parameters = new ArrayList<VisalloApiBase.Parameter>();
        parameters.add(new VisalloApiBase.Parameter("graphVertexId", graphVertexId));
        parameters.add(new VisalloApiBase.Parameter("width", width));
        getVisalloApi().execute("GET", "/vertex/video-preview", parameters, null);
    }

    /**
     * @param vertexId REQUIRED
     */
    public ClientApiVertexDetails getDetails(
        @Required(name = "vertexId") String vertexId
    ) {
        List<VisalloApiBase.Parameter> parameters = new ArrayList<VisalloApiBase.Parameter>();
        parameters.add(new VisalloApiBase.Parameter("vertexId", vertexId));
        return getVisalloApi().execute("GET", "/vertex/details", parameters, ClientApiVertexDetails.class);
    }

    /**
     * @param vertexId REQUIRED
     * @param propertyKey OPTIONAL
     * @param propertyName REQUIRED
     * @param visibilitySource REQUIRED
     */
    public ClientApiVertexPropertyDetails getPropertyDetails(
        @Required(name = "vertexId") String vertexId,
        @Optional(name = "propertyKey") String propertyKey,
        @Required(name = "propertyName") String propertyName,
        @Required(name = "visibilitySource") String visibilitySource
    ) {
        List<VisalloApiBase.Parameter> parameters = new ArrayList<VisalloApiBase.Parameter>();
        parameters.add(new VisalloApiBase.Parameter("vertexId", vertexId));
        parameters.add(new VisalloApiBase.Parameter("propertyKey", propertyKey));
        parameters.add(new VisalloApiBase.Parameter("propertyName", propertyName));
        parameters.add(new VisalloApiBase.Parameter("visibilitySource", visibilitySource));
        return getVisalloApi().execute("GET", "/vertex/property/details", parameters, ClientApiVertexPropertyDetails.class);
    }

    /**
     * @param graphVertexId REQUIRED
     * @param propertyName REQUIRED
     * @param workspaceId REQUIRED
     */
    public ClientApiDetectedObjects getDetectedObjects(
        @Required(name = "graphVertexId") String graphVertexId,
        @Required(name = "propertyName") String propertyName,
        @Required(name = "workspaceId") String workspaceId
    ) {
        List<VisalloApiBase.Parameter> parameters = new ArrayList<VisalloApiBase.Parameter>();
        parameters.add(new VisalloApiBase.Parameter("graphVertexId", graphVertexId));
        parameters.add(new VisalloApiBase.Parameter("propertyName", propertyName));
        parameters.add(new VisalloApiBase.Parameter("workspaceId", workspaceId));
        return getVisalloApi().execute("GET", "/vertex/detected-objects", parameters, ClientApiDetectedObjects.class);
    }

    /**
     * @param graphVertexId REQUIRED
     * @param propertyKey REQUIRED
     * @param propertyName REQUIRED
     * @param Range OPTIONAL
     * @param download OPTIONAL
     * @param playback OPTIONAL
     */
    public void getProperty(
        @Required(name = "graphVertexId") String graphVertexId,
        @Required(name = "propertyKey") String propertyKey,
        @Required(name = "propertyName") String propertyName,
        @Optional(name = "Range") String Range,
        @Optional(name = "download", defaultValue = "false") boolean download,
        @Optional(name = "playback", defaultValue = "false") boolean playback
    ) {
        List<VisalloApiBase.Parameter> parameters = new ArrayList<VisalloApiBase.Parameter>();
        parameters.add(new VisalloApiBase.Parameter("graphVertexId", graphVertexId));
        parameters.add(new VisalloApiBase.Parameter("propertyKey", propertyKey));
        parameters.add(new VisalloApiBase.Parameter("propertyName", propertyName));
        parameters.add(new VisalloApiBase.Parameter("Range", Range));
        parameters.add(new VisalloApiBase.Parameter("download", download));
        parameters.add(new VisalloApiBase.Parameter("playback", playback));
        getVisalloApi().execute("GET", "/vertex/property", parameters, null);
    }

    /**
     * @param graphVertexId REQUIRED
     * @param propertyKey REQUIRED
     * @param propertyName REQUIRED
     * @param startTime OPTIONAL
     * @param endTime OPTIONAL
     */
    public ClientApiHistoricalPropertyValues getPropertyHistory(
        @Required(name = "graphVertexId") String graphVertexId,
        @Required(name = "propertyKey") String propertyKey,
        @Required(name = "propertyName") String propertyName,
        @Optional(name = "startTime") Long startTime,
        @Optional(name = "endTime") Long endTime
    ) {
        List<VisalloApiBase.Parameter> parameters = new ArrayList<VisalloApiBase.Parameter>();
        parameters.add(new VisalloApiBase.Parameter("graphVertexId", graphVertexId));
        parameters.add(new VisalloApiBase.Parameter("propertyKey", propertyKey));
        parameters.add(new VisalloApiBase.Parameter("propertyName", propertyName));
        parameters.add(new VisalloApiBase.Parameter("startTime", startTime));
        parameters.add(new VisalloApiBase.Parameter("endTime", endTime));
        return getVisalloApi().execute("GET", "/vertex/property/history", parameters, ClientApiHistoricalPropertyValues.class);
    }

    /**
     * @param graphVertexId REQUIRED
     * @param propertyKey REQUIRED
     * @param propertyName REQUIRED
     */
    public ClientApiTermMentionsResponse getTermMentions(
        @Required(name = "graphVertexId") String graphVertexId,
        @Required(name = "propertyKey") String propertyKey,
        @Required(name = "propertyName") String propertyName
    ) {
        List<VisalloApiBase.Parameter> parameters = new ArrayList<VisalloApiBase.Parameter>();
        parameters.add(new VisalloApiBase.Parameter("graphVertexId", graphVertexId));
        parameters.add(new VisalloApiBase.Parameter("propertyKey", propertyKey));
        parameters.add(new VisalloApiBase.Parameter("propertyName", propertyName));
        return getVisalloApi().execute("GET", "/vertex/term-mentions", parameters, ClientApiTermMentionsResponse.class);
    }

    /**
     * @param graphVertexId REQUIRED
     */
    public ClientApiElement getProperties(
        @Required(name = "graphVertexId") String graphVertexId
    ) {
        List<VisalloApiBase.Parameter> parameters = new ArrayList<VisalloApiBase.Parameter>();
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
    public ClientApiVertexEdges getEdges(
        @Required(name = "graphVertexId") String graphVertexId,
        @Optional(name = "offset", defaultValue = "0") int offset,
        @Optional(name = "size", defaultValue = "25") int size,
        @Optional(name = "edgeLabel") String edgeLabel,
        @Optional(name = "relatedVertexId") String relatedVertexId
    ) {
        List<VisalloApiBase.Parameter> parameters = new ArrayList<VisalloApiBase.Parameter>();
        parameters.add(new VisalloApiBase.Parameter("graphVertexId", graphVertexId));
        parameters.add(new VisalloApiBase.Parameter("offset", offset));
        parameters.add(new VisalloApiBase.Parameter("size", size));
        parameters.add(new VisalloApiBase.Parameter("edgeLabel", edgeLabel));
        parameters.add(new VisalloApiBase.Parameter("relatedVertexId", relatedVertexId));
        return getVisalloApi().execute("GET", "/vertex/edges", parameters, ClientApiVertexEdges.class);
    }

    /**
     */
    public ClientApiElementSearchResponse getSearch(
        
    ) {
        List<VisalloApiBase.Parameter> parameters = new ArrayList<VisalloApiBase.Parameter>();
        return getVisalloApi().execute("GET", "/vertex/search", parameters, ClientApiElementSearchResponse.class);
    }

    /**
     * @param lat REQUIRED
     * @param lon REQUIRED
     * @param radius REQUIRED
     */
    public ClientApiElementSearchResponse getGeoSearch(
        @Required(name = "lat") double lat,
        @Required(name = "lon") double lon,
        @Required(name = "radius") double radius
    ) {
        List<VisalloApiBase.Parameter> parameters = new ArrayList<VisalloApiBase.Parameter>();
        parameters.add(new VisalloApiBase.Parameter("lat", lat));
        parameters.add(new VisalloApiBase.Parameter("lon", lon));
        parameters.add(new VisalloApiBase.Parameter("radius", radius));
        return getVisalloApi().execute("GET", "/vertex/geo-search", parameters, ClientApiElementSearchResponse.class);
    }

    /**
     * @param outVertexId REQUIRED
     * @param inVertexId REQUIRED
     * @param hops REQUIRED
     * @param labels OPTIONAL
     */
    public ClientApiLongRunningProcessSubmitResponse getFindPath(
        @Required(name = "outVertexId") String outVertexId,
        @Required(name = "inVertexId") String inVertexId,
        @Required(name = "hops") int hops,
        @Optional(name = "labels[]") String[] labels
    ) {
        List<VisalloApiBase.Parameter> parameters = new ArrayList<VisalloApiBase.Parameter>();
        parameters.add(new VisalloApiBase.Parameter("outVertexId", outVertexId));
        parameters.add(new VisalloApiBase.Parameter("inVertexId", inVertexId));
        parameters.add(new VisalloApiBase.Parameter("hops", hops));
        parameters.add(new VisalloApiBase.Parameter("labels[]", labels));
        return getVisalloApi().execute("GET", "/vertex/find-path", parameters, ClientApiLongRunningProcessSubmitResponse.class);
    }

    /**
     */
    public ClientApiVertexCountsByConceptType getCountsByConceptType(
        
    ) {
        List<VisalloApiBase.Parameter> parameters = new ArrayList<VisalloApiBase.Parameter>();
        return getVisalloApi().execute("GET", "/vertex/counts-by-concept-type", parameters, ClientApiVertexCountsByConceptType.class);
    }

    /**
     */
    public ClientApiVertexCount getCount(
        
    ) {
        List<VisalloApiBase.Parameter> parameters = new ArrayList<VisalloApiBase.Parameter>();
        return getVisalloApi().execute("GET", "/vertex/count", parameters, ClientApiVertexCount.class);
    }

    /**
     * @param vertexIds REQUIRED
     */
    public ClientApiVerticesExistsResponse postExists(
        @Required(name = "vertexIds[]") String[] vertexIds
    ) {
        List<VisalloApiBase.Parameter> parameters = new ArrayList<VisalloApiBase.Parameter>();
        parameters.add(new VisalloApiBase.Parameter("vertexIds[]", vertexIds));
        return getVisalloApi().execute("POST", "/vertex/exists", parameters, ClientApiVerticesExistsResponse.class);
    }

    /**
     * @param publish OPTIONAL
     * @param addToWorkspace OPTIONAL
     */
    public ClientApiArtifactImportResponse postImport(
        @Optional(name = "publish", defaultValue = "false") boolean publish,
        @Optional(name = "addToWorkspace", defaultValue = "false") boolean addToWorkspace
    ) {
        List<VisalloApiBase.Parameter> parameters = new ArrayList<VisalloApiBase.Parameter>();
        parameters.add(new VisalloApiBase.Parameter("publish", publish));
        parameters.add(new VisalloApiBase.Parameter("addToWorkspace", addToWorkspace));
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
     * @param justificationText OPTIONAL
     */
    public void postResolveTerm(
        @Required(name = "artifactId") String artifactId,
        @Required(name = "propertyKey") String propertyKey,
        @Required(name = "mentionStart") long mentionStart,
        @Required(name = "mentionEnd") long mentionEnd,
        @Required(name = "sign") String sign,
        @Required(name = "conceptId") String conceptId,
        @Required(name = "visibilitySource") String visibilitySource,
        @Optional(name = "resolvedVertexId") String resolvedVertexId,
        @Optional(name = "sourceInfo") String sourceInfo,
        @Optional(name = "justificationText") String justificationText
    ) {
        List<VisalloApiBase.Parameter> parameters = new ArrayList<VisalloApiBase.Parameter>();
        parameters.add(new VisalloApiBase.Parameter("artifactId", artifactId));
        parameters.add(new VisalloApiBase.Parameter("propertyKey", propertyKey));
        parameters.add(new VisalloApiBase.Parameter("mentionStart", mentionStart));
        parameters.add(new VisalloApiBase.Parameter("mentionEnd", mentionEnd));
        parameters.add(new VisalloApiBase.Parameter("sign", sign));
        parameters.add(new VisalloApiBase.Parameter("conceptId", conceptId));
        parameters.add(new VisalloApiBase.Parameter("visibilitySource", visibilitySource));
        parameters.add(new VisalloApiBase.Parameter("resolvedVertexId", resolvedVertexId));
        parameters.add(new VisalloApiBase.Parameter("sourceInfo", sourceInfo));
        parameters.add(new VisalloApiBase.Parameter("justificationText", justificationText));
        getVisalloApi().execute("POST", "/vertex/resolve-term", parameters, null);
    }

    /**
     * @param termMentionId REQUIRED
     */
    public void postUnresolveTerm(
        @Required(name = "termMentionId") String termMentionId
    ) {
        List<VisalloApiBase.Parameter> parameters = new ArrayList<VisalloApiBase.Parameter>();
        parameters.add(new VisalloApiBase.Parameter("termMentionId", termMentionId));
        getVisalloApi().execute("POST", "/vertex/unresolve-term", parameters, null);
    }

    /**
     * @param artifactId REQUIRED
     * @param title REQUIRED
     * @param conceptId REQUIRED
     * @param visibilitySource REQUIRED
     * @param graphVertexId OPTIONAL
     * @param justificationText OPTIONAL
     * @param sourceInfo OPTIONAL
     * @param originalPropertyKey OPTIONAL
     * @param x1 REQUIRED
     * @param x2 REQUIRED
     * @param y1 REQUIRED
     * @param y2 REQUIRED
     */
    public ClientApiElement postResolveDetectedObject(
        @Required(name = "artifactId") String artifactId,
        @Required(name = "title") String title,
        @Required(name = "conceptId") String conceptId,
        @Required(name = "visibilitySource") String visibilitySource,
        @Optional(name = "graphVertexId") String graphVertexId,
        @Optional(name = "justificationText") String justificationText,
        @Optional(name = "sourceInfo") String sourceInfo,
        @Optional(name = "originalPropertyKey") String originalPropertyKey,
        @Required(name = "x1") double x1,
        @Required(name = "x2") double x2,
        @Required(name = "y1") double y1,
        @Required(name = "y2") double y2
    ) {
        List<VisalloApiBase.Parameter> parameters = new ArrayList<VisalloApiBase.Parameter>();
        parameters.add(new VisalloApiBase.Parameter("artifactId", artifactId));
        parameters.add(new VisalloApiBase.Parameter("title", title));
        parameters.add(new VisalloApiBase.Parameter("conceptId", conceptId));
        parameters.add(new VisalloApiBase.Parameter("visibilitySource", visibilitySource));
        parameters.add(new VisalloApiBase.Parameter("graphVertexId", graphVertexId));
        parameters.add(new VisalloApiBase.Parameter("justificationText", justificationText));
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
    public ClientApiElement postUnresolveDetectedObject(
        @Required(name = "vertexId") String vertexId,
        @Required(name = "multiValueKey") String multiValueKey
    ) {
        List<VisalloApiBase.Parameter> parameters = new ArrayList<VisalloApiBase.Parameter>();
        parameters.add(new VisalloApiBase.Parameter("vertexId", vertexId));
        parameters.add(new VisalloApiBase.Parameter("multiValueKey", multiValueKey));
        return getVisalloApi().execute("POST", "/vertex/unresolve-detected-object", parameters, ClientApiElement.class);
    }

    /**
     * @param graphVertexId REQUIRED
     * @param propertyKey OPTIONAL
     * @param propertyName REQUIRED
     * @param value OPTIONAL
     * @param values OPTIONAL
     * @param visibilitySource REQUIRED
     * @param oldVisibilitySource OPTIONAL
     * @param sourceInfo OPTIONAL
     * @param metadata OPTIONAL
     * @param justificationText OPTIONAL
     */
    public ClientApiElement postProperty(
        @Required(name = "graphVertexId") String graphVertexId,
        @Optional(name = "propertyKey") String propertyKey,
        @Required(name = "propertyName") String propertyName,
        @Optional(name = "value") String value,
        @Optional(name = "value[]") String[] values,
        @Required(name = "visibilitySource") String visibilitySource,
        @Optional(name = "oldVisibilitySource") String oldVisibilitySource,
        @Optional(name = "sourceInfo") String sourceInfo,
        @Optional(name = "metadata") String metadata,
        @Optional(name = "justificationText") String justificationText
    ) {
        List<VisalloApiBase.Parameter> parameters = new ArrayList<VisalloApiBase.Parameter>();
        parameters.add(new VisalloApiBase.Parameter("graphVertexId", graphVertexId));
        parameters.add(new VisalloApiBase.Parameter("propertyKey", propertyKey));
        parameters.add(new VisalloApiBase.Parameter("propertyName", propertyName));
        parameters.add(new VisalloApiBase.Parameter("value", value));
        parameters.add(new VisalloApiBase.Parameter("value[]", values));
        parameters.add(new VisalloApiBase.Parameter("visibilitySource", visibilitySource));
        parameters.add(new VisalloApiBase.Parameter("oldVisibilitySource", oldVisibilitySource));
        parameters.add(new VisalloApiBase.Parameter("sourceInfo", sourceInfo));
        parameters.add(new VisalloApiBase.Parameter("metadata", metadata));
        parameters.add(new VisalloApiBase.Parameter("justificationText", justificationText));
        return getVisalloApi().execute("POST", "/vertex/property", parameters, ClientApiElement.class);
    }

    /**
     * @param graphVertexId REQUIRED
     * @param propertyKey OPTIONAL
     * @param propertyName REQUIRED
     * @param value OPTIONAL
     * @param values OPTIONAL
     * @param visibilitySource REQUIRED
     * @param oldVisibilitySource OPTIONAL
     * @param sourceInfo OPTIONAL
     * @param metadata OPTIONAL
     * @param justificationText OPTIONAL
     */
    public ClientApiElement postComment(
        @Required(name = "graphVertexId") String graphVertexId,
        @Optional(name = "propertyKey") String propertyKey,
        @Required(name = "propertyName") String propertyName,
        @Optional(name = "value") String value,
        @Optional(name = "value[]") String[] values,
        @Required(name = "visibilitySource") String visibilitySource,
        @Optional(name = "oldVisibilitySource") String oldVisibilitySource,
        @Optional(name = "sourceInfo") String sourceInfo,
        @Optional(name = "metadata") String metadata,
        @Optional(name = "justificationText") String justificationText
    ) {
        List<VisalloApiBase.Parameter> parameters = new ArrayList<VisalloApiBase.Parameter>();
        parameters.add(new VisalloApiBase.Parameter("graphVertexId", graphVertexId));
        parameters.add(new VisalloApiBase.Parameter("propertyKey", propertyKey));
        parameters.add(new VisalloApiBase.Parameter("propertyName", propertyName));
        parameters.add(new VisalloApiBase.Parameter("value", value));
        parameters.add(new VisalloApiBase.Parameter("value[]", values));
        parameters.add(new VisalloApiBase.Parameter("visibilitySource", visibilitySource));
        parameters.add(new VisalloApiBase.Parameter("oldVisibilitySource", oldVisibilitySource));
        parameters.add(new VisalloApiBase.Parameter("sourceInfo", sourceInfo));
        parameters.add(new VisalloApiBase.Parameter("metadata", metadata));
        parameters.add(new VisalloApiBase.Parameter("justificationText", justificationText));
        return getVisalloApi().execute("POST", "/vertex/comment", parameters, ClientApiElement.class);
    }

    /**
     * @param graphVertexId REQUIRED
     * @param visibilitySource REQUIRED
     */
    public ClientApiElement postVisibility(
        @Required(name = "graphVertexId") String graphVertexId,
        @Required(name = "visibilitySource") String visibilitySource
    ) {
        List<VisalloApiBase.Parameter> parameters = new ArrayList<VisalloApiBase.Parameter>();
        parameters.add(new VisalloApiBase.Parameter("graphVertexId", graphVertexId));
        parameters.add(new VisalloApiBase.Parameter("visibilitySource", visibilitySource));
        return getVisalloApi().execute("POST", "/vertex/visibility", parameters, ClientApiElement.class);
    }

    /**
     * @param vertexIds REQUIRED
     * @param fallbackToPublic OPTIONAL
     */
    public ClientApiVertexMultipleResponse postMultiple(
        @Required(name = "vertexIds[]") String[] vertexIds,
        @Optional(name = "fallbackToPublic", defaultValue = "false") boolean fallbackToPublic
    ) {
        List<VisalloApiBase.Parameter> parameters = new ArrayList<VisalloApiBase.Parameter>();
        parameters.add(new VisalloApiBase.Parameter("vertexIds[]", vertexIds));
        parameters.add(new VisalloApiBase.Parameter("fallbackToPublic", fallbackToPublic));
        return getVisalloApi().execute("POST", "/vertex/multiple", parameters, ClientApiVertexMultipleResponse.class);
    }

    /**
     * @param vertexId OPTIONAL
     * @param conceptType REQUIRED
     * @param visibilitySource REQUIRED
     * @param properties OPTIONAL
     * @param publish OPTIONAL
     * @param justificationText OPTIONAL
     */
    public ClientApiElement postNew(
        @Optional(name = "vertexId") String vertexId,
        @Required(name = "conceptType") String conceptType,
        @Required(name = "visibilitySource") String visibilitySource,
        @Optional(name = "properties") String properties,
        @Optional(name = "publish", defaultValue = "false") boolean publish,
        @Optional(name = "justificationText") String justificationText
    ) {
        List<VisalloApiBase.Parameter> parameters = new ArrayList<VisalloApiBase.Parameter>();
        parameters.add(new VisalloApiBase.Parameter("vertexId", vertexId));
        parameters.add(new VisalloApiBase.Parameter("conceptType", conceptType));
        parameters.add(new VisalloApiBase.Parameter("visibilitySource", visibilitySource));
        parameters.add(new VisalloApiBase.Parameter("properties", properties));
        parameters.add(new VisalloApiBase.Parameter("publish", publish));
        parameters.add(new VisalloApiBase.Parameter("justificationText", justificationText));
        return getVisalloApi().execute("POST", "/vertex/new", parameters, ClientApiElement.class);
    }

    /**
     */
    public ClientApiElementSearchResponse postSearch(
        
    ) {
        List<VisalloApiBase.Parameter> parameters = new ArrayList<VisalloApiBase.Parameter>();
        return getVisalloApi().execute("POST", "/vertex/search", parameters, ClientApiElementSearchResponse.class);
    }

    /**
     * @param graphVertexId REQUIRED
     */
    public ClientApiElement postUploadImage(
        @Required(name = "graphVertexId") String graphVertexId
    ) {
        List<VisalloApiBase.Parameter> parameters = new ArrayList<VisalloApiBase.Parameter>();
        parameters.add(new VisalloApiBase.Parameter("graphVertexId", graphVertexId));
        return getVisalloApi().execute("POST", "/vertex/upload-image", parameters, ClientApiElement.class);
    }

    /**
     * @param graphVertexIds REQUIRED
     * @param limitParentConceptId OPTIONAL
     * @param limitEdgeLabel OPTIONAL
     * @param maxVerticesToReturn OPTIONAL
     */
    public ClientApiElementFindRelatedResponse postFindRelated(
        @Required(name = "graphVertexIds[]") String[] graphVertexIds,
        @Optional(name = "limitParentConceptId") String limitParentConceptId,
        @Optional(name = "limitEdgeLabel") String limitEdgeLabel,
        @Optional(name = "maxVerticesToReturn", defaultValue = "250") long maxVerticesToReturn
    ) {
        List<VisalloApiBase.Parameter> parameters = new ArrayList<VisalloApiBase.Parameter>();
        parameters.add(new VisalloApiBase.Parameter("graphVertexIds[]", graphVertexIds));
        parameters.add(new VisalloApiBase.Parameter("limitParentConceptId", limitParentConceptId));
        parameters.add(new VisalloApiBase.Parameter("limitEdgeLabel", limitEdgeLabel));
        parameters.add(new VisalloApiBase.Parameter("maxVerticesToReturn", maxVerticesToReturn));
        return getVisalloApi().execute("POST", "/vertex/find-related", parameters, ClientApiElementFindRelatedResponse.class);
    }

    /**
     * @param graphVertexId REQUIRED
     */
    public void delete(
        @Required(name = "graphVertexId") String graphVertexId
    ) {
        List<VisalloApiBase.Parameter> parameters = new ArrayList<VisalloApiBase.Parameter>();
        parameters.add(new VisalloApiBase.Parameter("graphVertexId", graphVertexId));
        getVisalloApi().execute("DELETE", "/vertex", parameters, null);
    }

    /**
     * @param graphVertexId REQUIRED
     * @param propertyKey REQUIRED
     * @param propertyName REQUIRED
     */
    public void deleteProperty(
        @Required(name = "graphVertexId") String graphVertexId,
        @Required(name = "propertyKey") String propertyKey,
        @Required(name = "propertyName") String propertyName
    ) {
        List<VisalloApiBase.Parameter> parameters = new ArrayList<VisalloApiBase.Parameter>();
        parameters.add(new VisalloApiBase.Parameter("graphVertexId", graphVertexId));
        parameters.add(new VisalloApiBase.Parameter("propertyKey", propertyKey));
        parameters.add(new VisalloApiBase.Parameter("propertyName", propertyName));
        getVisalloApi().execute("DELETE", "/vertex/property", parameters, null);
    }

}