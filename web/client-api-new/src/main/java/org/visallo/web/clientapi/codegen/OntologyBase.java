package org.visallo.web.clientapi.codegen;

import org.json.JSONObject;
import org.visallo.web.clientapi.CategoryBase;
import org.visallo.web.clientapi.VisalloApi;
import org.visallo.web.clientapi.model.*;

import java.util.ArrayList;
import java.util.List;

public abstract class OntologyBase extends CategoryBase {
    public OntologyBase(VisalloApi visalloApi) {
        super(visalloApi);
    }

    /**
     */
    public ClientApiOntology get() {
        List<VisalloApiBase.Parameter> parameters = new ArrayList<>();
        return getVisalloApi().execute("GET", "/ontology", parameters, ClientApiOntology.class);
    }

}