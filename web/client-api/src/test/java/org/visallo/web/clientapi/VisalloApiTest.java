package org.visallo.web.clientapi;

import org.junit.Test;
import org.visallo.web.clientapi.model.ClientApiUser;
import org.visallo.web.clientapi.model.ClientApiVertex;
import org.visallo.web.clientapi.model.ClientApiWorkspaceVertices;

public class VisalloApiTest {
    @Test
    public void testUserMe() {
        VisalloApi api = new VisalloApi("https://visallo-dev:8889", true);
        ClientApiUser user = UsernameOnlyAuthentication.logIn(api, "testUser");
        System.out.println("user id: " + user.getId());
        ClientApiWorkspaceVertices vertices = api.getWorkspace().getVertices();
        for (ClientApiVertex clientApiVertex : vertices.getVertices()) {
            System.out.println(clientApiVertex.getId());
        }
    }
}