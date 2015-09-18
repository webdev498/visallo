package org.visallo.it;

import org.apache.commons.collections.iterators.LoopingIterator;
import org.junit.After;
import org.junit.Before;
import org.visallo.web.clientapi.VisalloApi;
import org.visallo.web.clientapi.VisalloClientApiException;
import org.visallo.web.clientapi.auth.UsernameOnlyAuthentication;
import org.visallo.web.clientapi.codegen.VisalloApiBase;
import org.visallo.web.clientapi.model.ClientApiElement;

import java.util.ArrayList;
import java.util.List;

public abstract class IntegrationTestBase {
    protected static final int NUM_DEFAULT_PROPERTIES = 2;
    protected static final String PROPERTY_NAME = "http://visallo.org/test#firstName";
    protected static final String PROPERTY_KEY_PREFIX = "key-firstName-";
    protected static final String PROPERTY_VALUE_PREFIX = "First Name ";
    protected static final String USERNAME_TEST_USER_1 = "user1";
    protected VisalloApi api;

    @Before
    public void before() {
        VisalloApiBase.ignoreSslErrors();
        api = new VisalloApi("https://visallo-dev:8889");
        UsernameOnlyAuthentication.logIn(api, USERNAME_TEST_USER_1);
        addUserAuths(USERNAME_TEST_USER_1, "a", "b", "c", "d", "e", "f", "x", "y", "z");
    }

    @After
    public void after() {
        api.logout();
    }

    private void addUserAuths(String userName, String... auths) {
        for (String auth : auths) {
            api.getUser().postAuthAdd(userName, auth);
        }
    }

    protected List<String> createVertices(int numVertices, List<String> vertexVisibilities, int numPropertiesPerVertex, List<String> propertyVisibilities) throws VisalloClientApiException {
        LoopingIterator vertexVizIterator = new LoopingIterator(vertexVisibilities);
        LoopingIterator propertyVizIterator = new LoopingIterator(propertyVisibilities);
        List<String> vertexIds = new ArrayList<>();
        for (int i = 0; i < numVertices; i++) {
            ClientApiElement vertex = api.getVertex().postNew(null, TestOntology.CONCEPT_TYPE_PERSON, (String) vertexVizIterator.next(), null, "justification");
            String vertexId = vertex.getId();
            setVertexProperties(numPropertiesPerVertex, propertyVizIterator, vertexId);
            vertexIds.add(vertexId);
        }
        return vertexIds;
    }

    protected void setVertexProperties(int numPropertiesPerVertex, LoopingIterator propertyAuthIterator, String vertexId) throws VisalloClientApiException {
        for (int j = 0; j < numPropertiesPerVertex; j++) {
            api.getVertex().postProperty(vertexId, PROPERTY_KEY_PREFIX + j, PROPERTY_NAME, PROPERTY_VALUE_PREFIX + j, (String) propertyAuthIterator.next(), "justification");
        }
    }
}
