package org.visallo.it;

import org.junit.Test;
import org.visallo.clavin.ClavinTermMentionFilter;
import org.visallo.core.model.properties.VisalloProperties;
import org.visallo.web.clientapi.VisalloApi;
import org.visallo.web.clientapi.VisalloClientApiException;
import org.visallo.web.clientapi.model.*;
import org.visallo.zipCodeResolver.ZipCodeResolverTermMentionFilter;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

import static org.junit.Assert.*;

public class WorkspaceUndoIntegrationTest extends TestBase {
    public static final String FILE_CONTENTS = "Susan Feng knows Jeff Kunkle. They both worked in Reston, VA, 20191";

    @Test
    public void testWorkspaceUndo() throws IOException, VisalloClientApiException {
        importArtifact();
        undoAll();
    }

    private void importArtifact() throws IOException, VisalloClientApiException {
        VisalloApi visalloApi = login(USERNAME_TEST_USER_1);
        addUserAuths(visalloApi, USERNAME_TEST_USER_1, "auth1");
        ClientApiArtifactImportResponse artifact = visalloApi.getVertex().postImport("auth1", "test.txt", new ByteArrayInputStream(FILE_CONTENTS.getBytes()));
        assertEquals(1, artifact.getVertexIds().size());
        String artifactVertexId = artifact.getVertexIds().get(0);
        assertNotNull(artifactVertexId);

        visalloTestCluster.processGraphPropertyQueue();

        ClientApiElement susanFengVertex = visalloApi.getVertex().postNew(null, TestOntology.CONCEPT_PERSON, "", null, "justification");
        visalloApi.getVertex().postProperty(susanFengVertex.getId(), VisalloProperties.TITLE.getPropertyName(), TEST_MULTI_VALUE_KEY, "Susan Feng", null, "", null, null, null, "test");

        ClientApiElement jeffKunkleVertex = visalloApi.getVertex().postNew(null, TestOntology.CONCEPT_PERSON, "", null, "justification");
        visalloApi.getVertex().postProperty(jeffKunkleVertex.getId(), VisalloProperties.TITLE.getPropertyName(), TEST_MULTI_VALUE_KEY, "Jeff Kunkle", null, "", null, null, null, "test");

        visalloApi.getEdge().postCreate(susanFengVertex.getId(), jeffKunkleVertex.getId(), TestOntology.EDGE_LABEL_WORKS_FOR, "", "test", null);
        visalloApi.getEdge().postProperty(susanFengVertex.getId(), "http://visallo.org/test#firstName", "edge property value", "", null, "key1", null, "test");
        ClientApiVertexEdges edges = visalloApi.getVertex().getEdges(susanFengVertex.getId(), 0, 25, null, null);
        assertEquals(1, edges.getRelationships().size());
        List<ClientApiProperty> edgeProperties = edges.getRelationships().get(0).getRelationship().getProperties();
        assertEquals(7, edgeProperties.size());
        boolean foundFirstNameEdgeProperty = false;
        for (ClientApiProperty edgeProperty : edgeProperties) {
            if (edgeProperty.getKey().equals("key1") && edgeProperty.getName().equals("http://visallo.org/test#firstName")) {
                assertEquals("edge property value", edgeProperty.getValue().toString());
                foundFirstNameEdgeProperty = true;
            }
        }
        assertTrue(foundFirstNameEdgeProperty);

        edges = visalloApi.getVertex().getEdges(artifactVertexId, 0, 25, null, null);
        assertEquals(2, edges.getRelationships().size());
        ClientApiElement restonVertex = edges.getRelationships().get(0).getVertex();
        assertHasProperty(restonVertex.getProperties(), ClavinTermMentionFilter.MULTI_VALUE_PROPERTY_KEY, VisalloProperties.CONCEPT_TYPE.getPropertyName(), TestOntology.CONCEPT_CITY);
        assertHasProperty(restonVertex.getProperties(), ClavinTermMentionFilter.MULTI_VALUE_PROPERTY_KEY, VisalloProperties.SOURCE.getPropertyName(), "CLAVIN");
        assertHasProperty(restonVertex.getProperties(), ClavinTermMentionFilter.MULTI_VALUE_PROPERTY_KEY, VisalloProperties.TITLE.getPropertyName(), "Reston (US, VA)");
        VisibilityJson visibilityJson = new VisibilityJson();
        visibilityJson.setSource("auth1");
        visibilityJson.addWorkspace(visalloApi.getWorkspaceId());
        assertHasProperty(restonVertex.getProperties(), ClavinTermMentionFilter.MULTI_VALUE_PROPERTY_KEY, VisalloProperties.VISIBILITY_JSON.getPropertyName(), visibilityJson);

        ClientApiElement zipCodeVertex = edges.getRelationships().get(1).getVertex();
        assertHasProperty(zipCodeVertex.getProperties(), ZipCodeResolverTermMentionFilter.MULTI_VALUE_PROPERTY_KEY, VisalloProperties.CONCEPT_TYPE.getPropertyName(), TestOntology.CONCEPT_ZIP_CODE);
        assertHasProperty(zipCodeVertex.getProperties(), ZipCodeResolverTermMentionFilter.MULTI_VALUE_PROPERTY_KEY, VisalloProperties.SOURCE.getPropertyName(), "Zip Code Resolver");
        assertHasProperty(zipCodeVertex.getProperties(), ZipCodeResolverTermMentionFilter.MULTI_VALUE_PROPERTY_KEY, VisalloProperties.TITLE.getPropertyName(), "20191 - Reston, VA");
        visibilityJson = new VisibilityJson();
        visibilityJson.setSource("auth1");
        visibilityJson.addWorkspace(visalloApi.getWorkspaceId());
        assertHasProperty(zipCodeVertex.getProperties(), ZipCodeResolverTermMentionFilter.MULTI_VALUE_PROPERTY_KEY, VisalloProperties.VISIBILITY_JSON.getPropertyName(), visibilityJson);

        visalloApi.logout();
    }

    private void undoAll() throws IOException, VisalloClientApiException {
        VisalloApi visalloApi = login(USERNAME_TEST_USER_1);
        assertUndoAll(visalloApi, 35);
        visalloApi.logout();
    }
}
