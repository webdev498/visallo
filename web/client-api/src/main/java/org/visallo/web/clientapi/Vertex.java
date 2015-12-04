package org.visallo.web.clientapi;

import org.visallo.web.clientapi.codegen.VertexBase;
import org.visallo.web.clientapi.codegen.VisalloApiBase;
import org.visallo.web.clientapi.model.ClientApiArtifactImportResponse;
import org.visallo.web.clientapi.model.ClientApiElement;
import org.visallo.web.clientapi.model.ClientApiElementSearchResponse;
import org.visallo.web.clientapi.model.ClientApiVertexEdges;
import org.visallo.web.clientapi.util.Optional;
import org.visallo.web.clientapi.util.Required;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class Vertex extends VertexBase {
    public Vertex(VisalloApi visalloApi) {
        super(visalloApi);
    }

    public ClientApiElementSearchResponse getSearch(String query) {
        List<VisalloApiBase.Parameter> parameters = new ArrayList<VisalloApiBase.Parameter>();
        parameters.add(new VisalloApiBase.Parameter("q", query));
        return getVisalloApi().execute("GET", "/vertex/search", parameters, ClientApiElementSearchResponse.class);
    }

    public InputStream getRaw(String graphVertexId) {
        return getRaw(graphVertexId, false, false, null);
    }

    public ClientApiArtifactImportResponse postImport(String visibilitySource, String fileName, InputStream data) {
        List<VisalloApiBase.Parameter> parameters = new ArrayList<VisalloApiBase.Parameter>();
        parameters.add(new VisalloApiBase.Parameter("visibilitySource", visibilitySource));
        parameters.add(new VisalloApiBase.MultiPartParameter("file", fileName, data));
        return getVisalloApi().execute("POST", "/vertex/import", parameters, ClientApiArtifactImportResponse.class);
    }

    public ClientApiVertexEdges getEdges(String graphVertexId) {
        return getEdges(graphVertexId, 0, 25, null, null);
    }

    public ClientApiElement postProperty(
            @Required(name = "graphVertexId") String graphVertexId,
            @Optional(name = "propertyKey") String propertyKey,
            @Required(name = "propertyName") String propertyName,
            @Optional(name = "value") String value,
            @Required(name = "visibilitySource") String visibilitySource,
            @Optional(name = "justificationText") String justificationText
    ) {
        return super.postProperty(graphVertexId, propertyKey, propertyName, value, null, visibilitySource, null, null, null, justificationText);
    }
}