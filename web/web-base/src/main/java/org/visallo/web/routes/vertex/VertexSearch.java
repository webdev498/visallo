package org.visallo.web.routes.vertex;

import com.google.inject.Inject;
import com.v5analytics.webster.ParameterizedHandler;
import com.v5analytics.webster.annotations.Handle;
import org.vertexium.Authorizations;
import org.vertexium.ElementType;
import org.vertexium.Graph;
import org.visallo.core.config.Configuration;
import org.visallo.core.model.ontology.OntologyRepository;
import org.visallo.web.clientapi.model.ClientApiElementSearchResponse;
import org.visallo.web.parameterProviders.ActiveWorkspaceId;

import javax.servlet.http.HttpServletRequest;
import java.util.EnumSet;

public class VertexSearch extends ElementSearchWithRelatedBase implements ParameterizedHandler {
    @Inject
    public VertexSearch(
            OntologyRepository ontologyRepository,
            Graph graph,
            Configuration configuration
    ) {
        super(ontologyRepository, graph, configuration);
    }

    @Override
    @Handle
    public ClientApiElementSearchResponse handle(
            HttpServletRequest request,
            @ActiveWorkspaceId String workspaceId,
            Authorizations authorizations
    ) throws Exception {
        return super.handle(request, workspaceId, authorizations);
    }

    @Override
    protected EnumSet<ElementType> getResultType() {
        return EnumSet.of(ElementType.VERTEX);
    }
}
