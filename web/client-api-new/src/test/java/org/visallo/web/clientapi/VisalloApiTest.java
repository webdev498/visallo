package org.visallo.web.clientapi;

import org.junit.Test;
import org.visallo.web.clientapi.model.ClientApiVertex;
import org.visallo.web.clientapi.model.ClientApiWorkspaceVertices;

public class VisalloApiTest {
    @Test
    public void testUserMe() {
        VisalloApi api = new VisalloApi("https://visallo-dev:8889", true);
        UsernameOnlyAuthentication.logIn(api, "testUser");
        ClientApiWorkspaceVertices vertices = api.getWorkspace().getVertices();
        for (ClientApiVertex clientApiVertex : vertices.getVertices()) {
            System.out.println(clientApiVertex.getId());
        }
    }
}