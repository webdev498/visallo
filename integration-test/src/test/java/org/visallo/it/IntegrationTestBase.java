package org.visallo.it;

import org.apache.commons.collections.iterators.LoopingIterator;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TestName;
import org.visallo.web.clientapi.VisalloApi;
import org.visallo.web.clientapi.VisalloClientApiException;
import org.visallo.web.clientapi.auth.UsernameOnlyAuthentication;
import org.visallo.web.clientapi.codegen.VisalloApiBase;
import org.visallo.web.clientapi.model.ClientApiElement;
import org.visallo.web.clientapi.model.ClientApiUser;
import org.visallo.web.clientapi.model.ClientApiWorkspace;

import java.text.SimpleDateFormat;
import java.util.*;

public abstract class IntegrationTestBase {
    private static final Date START_DATE = new Date();
    protected static final int NUM_DEFAULT_PROPERTIES = 2;
    protected static final String PROPERTY_NAME = "http://visallo.org/test#firstName";
    protected static final String PROPERTY_KEY_PREFIX = "key-firstName-";
    protected static final String PROPERTY_VALUE_PREFIX = "First Name ";
    protected static final String USERNAME_TEST_USER_1 = "user1";
    protected VisalloApi api;

    @Rule
    public TestName name = new TestName();

    @Before
    public void before() {
        VisalloApiBase.ignoreSslErrors();
        api = new VisalloApi("https://visallo-dev:8889");
        api.setDebug(true);
        UsernameOnlyAuthentication.logIn(api, USERNAME_TEST_USER_1);

        ClientApiWorkspace newWorkspace = api.getWorkspace().postCreate("Test: " + getDateTimeAndTestMethodName());
        System.out.println(newWorkspace.getTitle());
        api.setWorkspaceId(newWorkspace.getWorkspaceId());

        addUserAuths(USERNAME_TEST_USER_1, "a", "b", "c", "d", "e", "f", "x", "y", "z");
    }

    protected String getDateTimeAndTestMethodName() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm").format(START_DATE) + " - " + this.getClass().getSimpleName() + "#" + name.getMethodName();
    }

    @After
    public void after() {
        api.logout();
    }

    protected void addUserAuths(String userName, String... auths) {
        for (String auth : auths) {
            api.getUser().postAuthAdd(userName, auth);
        }
    }

    protected void setAuths(String userName, String... newAuths) {
        Set<String> newAuthsSet = new HashSet<>();
        Collections.addAll(newAuthsSet, newAuths);
        ClientApiUser user = api.getUser().get(userName);
        List<String> oldAuths = user.getAuthorizations();
        for (String oldAuth : oldAuths) {
            if (newAuthsSet.contains(oldAuth)) {
                continue;
            }
            api.getUser().postAuthRemove(userName, oldAuth);
        }
        for (String newAuth : newAuthsSet) {
            if (oldAuths.contains(newAuth)) {
                continue;
            }
            api.getUser().postAuthAdd(userName, newAuth);
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
