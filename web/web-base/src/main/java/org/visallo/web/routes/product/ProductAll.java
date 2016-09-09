package org.visallo.web.routes.product;

import com.google.inject.Inject;
import com.v5analytics.webster.ParameterizedHandler;
import com.v5analytics.webster.annotations.Handle;
import org.visallo.core.exception.VisalloResourceNotFoundException;
import org.visallo.core.model.workspace.product.Product;
import org.visallo.core.model.workspace.WorkspaceRepository;
import org.visallo.core.user.User;
import org.visallo.core.util.ClientApiConverter;
import org.visallo.web.clientapi.model.ClientApiProducts;
import org.visallo.web.parameterProviders.ActiveWorkspaceId;

import java.util.Collection;

public class ProductAll implements ParameterizedHandler {
    private final WorkspaceRepository workspaceRepository;

    @Inject
    public ProductAll(WorkspaceRepository workspaceRepository) {
        this.workspaceRepository = workspaceRepository;
    }

    @Handle
    public ClientApiProducts handle(
            @ActiveWorkspaceId String workspaceId,
            User user
    ) throws Exception {
        Collection<Product> products = workspaceRepository.findAllProductsForWorkspace(workspaceId, user);
        if (products == null) {
            throw new VisalloResourceNotFoundException("Could not find products for workspace " + workspaceId);
        }
        return ClientApiConverter.toClientApiProducts(products);
    }
}
