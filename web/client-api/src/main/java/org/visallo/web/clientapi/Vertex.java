package org.visallo.web.clientapi;

import org.visallo.web.clientapi.codegen.VertexBase;
import org.visallo.web.clientapi.codegen.VisalloApiBase;
import org.visallo.web.clientapi.model.ClientApiArtifactImportResponse;
import org.visallo.web.clientapi.model.ClientApiVertexEdges;
import org.visallo.web.clientapi.model.ClientApiVertexSearchResponse;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class Vertex extends VertexBase {
    public Vertex(VisalloApi visalloApi) {
        super(visalloApi);
    }

    public ClientApiVertexSearchResponse getSearch(String query) {
        List<VisalloApiBase.Parameter> parameters = new ArrayList<>();
        parameters.add(new VisalloApiBase.Parameter("q", query));
        return getVisalloApi().execute("GET", "/vertex/search", parameters, ClientApiVertexSearchResponse.class);
    }

    public InputStream getRaw(String graphVertexId) {
        return getRaw(graphVertexId, false, false, null);
    }

    public ClientApiArtifactImportResponse postImport(String visibilitySource, String fileName, InputStream data) {
        List<VisalloApiBase.Parameter> parameters = new ArrayList<>();
        parameters.add(new VisalloApiBase.Parameter("visibilitySource", visibilitySource));
        parameters.add(new VisalloApiBase.MultiPartParameter("file", fileName, data));
        return getVisalloApi().execute("POST", "/vertex/import", parameters, ClientApiArtifactImportResponse.class);
    }

    public ClientApiVertexEdges getEdges(String graphVertexId) {
        return getEdges(graphVertexId, 0, 25, null, null);
    }
}