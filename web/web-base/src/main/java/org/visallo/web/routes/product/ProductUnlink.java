package org.visallo.web.routes.product;

import com.google.inject.Inject;
import com.v5analytics.webster.ParameterizedHandler;
import com.v5analytics.webster.annotations.Handle;
import com.v5analytics.webster.annotations.Required;
import org.visallo.core.exception.VisalloResourceNotFoundException;
import org.visallo.core.model.workspace.WorkspaceRepository;
import org.visallo.core.model.workspace.product.Product;
import org.visallo.core.user.User;
import org.visallo.web.VisalloResponse;
import org.visallo.web.clientapi.model.ClientApiSuccess;
import org.visallo.web.parameterProviders.ActiveWorkspaceId;

public class ProductUnlink implements ParameterizedHandler {
    private final WorkspaceRepository workspaceRepository;

    @Inject
    public ProductUnlink(WorkspaceRepository workspaceRepository) {
        this.workspaceRepository = workspaceRepository;
    }

    @Handle
    public ClientApiSuccess handle(
            @Required(name = "product1Id") String product1Id,
            @Required(name = "product2Id") String product2Id,
            @ActiveWorkspaceId String workspaceId,
            User user
    ) throws Exception {
        Product product1 = workspaceRepository.findProductById(workspaceId, product1Id, null, false, user);
        if (product1 == null) {
            throw new VisalloResourceNotFoundException("Could not find product " + product1Id, product1Id);
        }

        Product product2 = workspaceRepository.findProductById(workspaceId, product2Id, null, false, user);
        if (product2 == null) {
            throw new VisalloResourceNotFoundException("Could not find product " + product2Id, product2Id);
        }

        workspaceRepository.unlinkWorkProducts(product1, product2, user);
        return VisalloResponse.SUCCESS;
    }

}
