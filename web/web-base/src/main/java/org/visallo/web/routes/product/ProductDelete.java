package org.visallo.web.routes.product;

import com.v5analytics.webster.ParameterizedHandler;
import com.v5analytics.webster.annotations.Handle;
import org.visallo.core.user.User;
import org.visallo.web.VisalloResponse;
import org.visallo.web.clientapi.model.ClientApiSuccess;
import org.visallo.web.parameterProviders.ActiveWorkspaceId;

public class ProductDelete implements ParameterizedHandler {
    @Handle
    public ClientApiSuccess handle(
            @ActiveWorkspaceId String workspaceId,
            User user
    ) throws Exception {
        return VisalloResponse.SUCCESS;
    }
}
