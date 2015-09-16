package org.visallo.web.clientapi.codegen;

import org.json.JSONObject;
import org.visallo.web.clientapi.CategoryBase;
import org.visallo.web.clientapi.VisalloApi;
import org.visallo.web.clientapi.model.*;

import java.util.ArrayList;
import java.util.List;

public abstract class AdminBase extends CategoryBase {
    public AdminBase(VisalloApi visalloApi) {
        super(visalloApi);
    }

    /**
     */
    public String getAll() {
        List<VisalloApiBase.Parameter> parameters = new ArrayList<>();
        return getVisalloApi().execute("GET", "/admin/all", parameters, String.class);
    }

    /**
     */
    public JSONObject getPlugins() {
        List<VisalloApiBase.Parameter> parameters = new ArrayList<>();
        return getVisalloApi().execute("GET", "/admin/plugins", parameters, JSONObject.class);
    }

    /**
     */
    public void getRouteRunner() {
        List<VisalloApiBase.Parameter> parameters = new ArrayList<>();
        getVisalloApi().execute("GET", "/admin/routeRunner", parameters, null);
    }

    /**
     * @param documentIRI OPTIONAL
     */
    public void postUploadOntology(String documentIRI) {
        List<VisalloApiBase.Parameter> parameters = new ArrayList<>();
        parameters.add(new VisalloApiBase.Parameter("documentIRI", documentIRI));
        getVisalloApi().execute("POST", "/admin/upload-ontology", parameters, null);
    }

    /**
     * @param propertyName OPTIONAL
     */
    public void postQueueVertices(String propertyName) {
        List<VisalloApiBase.Parameter> parameters = new ArrayList<>();
        parameters.add(new VisalloApiBase.Parameter("propertyName", propertyName));
        getVisalloApi().execute("POST", "/admin/queueVertices", parameters, null);
    }

    /**
     * @param label OPTIONAL
     */
    public void postQueueEdges(String label) {
        List<VisalloApiBase.Parameter> parameters = new ArrayList<>();
        parameters.add(new VisalloApiBase.Parameter("label", label));
        getVisalloApi().execute("POST", "/admin/queueEdges", parameters, null);
    }

    /**
     * @param graphVertexId REQUIRED
     */
    public void postDeleteVertex(String graphVertexId) {
        List<VisalloApiBase.Parameter> parameters = new ArrayList<>();
        parameters.add(new VisalloApiBase.Parameter("graphVertexId", graphVertexId));
        getVisalloApi().execute("POST", "/admin/deleteVertex", parameters, null);
    }

    /**
     * @param edgeId REQUIRED
     */
    public void postDeleteEdge(String edgeId) {
        List<VisalloApiBase.Parameter> parameters = new ArrayList<>();
        parameters.add(new VisalloApiBase.Parameter("edgeId", edgeId));
        getVisalloApi().execute("POST", "/admin/deleteEdge", parameters, null);
    }

}