package org.visallo.it;

import org.visallo.web.clientapi.VisalloApi;
import org.visallo.web.clientapi.auth.UsernameOnlyAuthentication;
import org.visallo.web.clientapi.codegen.VisalloApiBase;

public abstract class IntegrationTestBase {
    protected VisalloApi getVisalloApi() {
        VisalloApiBase.ignoreSslErrors();
        VisalloApi visalloApi = new VisalloApi("https://visallo-dev:8889");
        UsernameOnlyAuthentication.logIn(visalloApi, "testUser");
        return visalloApi;
    }
}
