package org.visallo.web.clientapi;

import org.junit.Test;
import org.visallo.web.clientapi.model.ClientApiUser;

public class VisalloApiTest {
    @Test
    public void testUserMe() {
        VisalloApi api = new VisalloApi("https://visallo-dev:8889", true);
        UsernameOnlyAuthentication.logIn(api, "testUser");
        ClientApiUser user = api.getUser().getMe();
        api.setWorkspaceId(user.getCurrentWorkspaceId());
        api.getWorkspace().getVertices();
    }
}