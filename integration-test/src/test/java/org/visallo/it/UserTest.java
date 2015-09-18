package org.visallo.it;

import org.junit.Test;
import org.visallo.web.clientapi.model.ClientApiUser;

import static org.junit.Assert.assertNotNull;

public class UserTest extends IntegrationTestBase {
    @Test
    public void testUserMe() {
        ClientApiUser userMe = api.getUser().getMe();
        assertNotNull(userMe);
        assertNotNull(userMe.getId());
    }
}
