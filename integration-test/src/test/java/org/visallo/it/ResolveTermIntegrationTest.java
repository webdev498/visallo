package org.visallo.it;

import org.junit.Test;
import org.visallo.core.model.properties.VisalloProperties;
import org.visallo.tikaTextExtractor.TikaTextExtractorGraphPropertyWorker;
import org.visallo.web.clientapi.VisalloApi;
import org.visallo.web.clientapi.VisalloClientApiException;
import org.visallo.web.clientapi.model.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import static org.junit.Assert.*;

public class ResolveTermIntegrationTest extends TestBase {
    private String artifactVertexId;
    private ClientApiElement joeFernerVertex;

    @Test
    public void testResolveTerm() throws IOException, VisalloClientApiException {
        setupData();

        VisalloApi visalloApi = login(USERNAME_TEST_USER_1);
        resolveTerm(visalloApi);
        assertHighlightedTextUpdatedWithResolvedEntity(visalloApi);
        assertDiff(visalloApi);
        visalloApi.logout();

        visalloApi = login(USERNAME_TEST_USER_2);
        addUserAuths(visalloApi, USERNAME_TEST_USER_2, "auth1");
        assertHighlightedTextDoesNotContainResolvedEntityForOtherUser(visalloApi);
        visalloApi.logout();

        visalloApi = login(USERNAME_TEST_USER_1);
        assertPublishAll(visalloApi, 2);
        visalloTestCluster.processGraphPropertyQueue();
        visalloApi.logout();

        visalloApi = login(USERNAME_TEST_USER_2);
        assertHighlightedTextContainResolvedEntityForOtherUser(visalloApi);
        visalloApi.logout();

        visalloApi = login(USERNAME_TEST_USER_1);
        resolveAndUnresolveTerm(visalloApi);
        visalloApi.logout();
    }

    public void setupData() throws VisalloClientApiException, IOException {
        VisalloApi visalloApi = login(USERNAME_TEST_USER_1);
        addUserAuths(visalloApi, USERNAME_TEST_USER_1, "auth1");

        ClientApiArtifactImportResponse artifact = visalloApi.getVertex().postImport("auth1", "test.txt", new ByteArrayInputStream("Joe Ferner knows David Singley.".getBytes()));
        assertEquals(1, artifact.getVertexIds().size());
        artifactVertexId = artifact.getVertexIds().get(0);
        assertNotNull(artifactVertexId);

        visalloTestCluster.processGraphPropertyQueue();

        joeFernerVertex = visalloApi.getVertex().postNew(null, TestOntology.CONCEPT_PERSON, "auth1", null, "justification");
        visalloApi.getVertex().postProperty(joeFernerVertex.getId(), VisalloProperties.TITLE.getPropertyName(), TEST_MULTI_VALUE_KEY, "Joe Ferner", null, "auth1", "test", null, null, null);

        visalloTestCluster.processGraphPropertyQueue();

        assertPublishAll(visalloApi, 14);

        visalloApi.logout();
    }

    public void resolveTerm(VisalloApi visalloApi) throws VisalloClientApiException {
        int entityStartOffset = "".length();
        int entityEndOffset = entityStartOffset + "Joe Ferner".length();
        visalloApi.getVertex().postResolveTerm(
                artifactVertexId,
                TikaTextExtractorGraphPropertyWorker.MULTI_VALUE_KEY,
                entityStartOffset, entityEndOffset,
                "Joe Ferner",
                TestOntology.CONCEPT_PERSON,
                "auth1",
                joeFernerVertex.getId(),
                "test",
                null
        );
    }

    public void assertHighlightedTextUpdatedWithResolvedEntity(VisalloApi visalloApi) throws VisalloClientApiException {
        String highlightedText = visalloApi.getVertex().getHighlightedText(artifactVertexId, TikaTextExtractorGraphPropertyWorker.MULTI_VALUE_KEY);
        LOGGER.info("%s", highlightedText);
        assertTrue("highlightedText did not contain string: " + highlightedText, highlightedText.contains("resolvedToVertexId&quot;:&quot;" + joeFernerVertex.getId() + "&quot;"));
    }

    public void assertDiff(VisalloApi visalloApi) throws VisalloClientApiException {
        ClientApiWorkspaceDiff diff;
        diff = visalloApi.getWorkspace().getDiff();
        LOGGER.info("assertDiff: %s", diff.toString());
        assertEquals(2, diff.getDiffs().size());
        String edgeId = null;
        boolean foundEdgeDiffItem = false;
        boolean foundEdgeVisibilityJsonDiffItem = false;
        for (ClientApiWorkspaceDiff.Item workspaceDiffItem : diff.getDiffs()) {
            if (workspaceDiffItem instanceof ClientApiWorkspaceDiff.EdgeItem) {
                foundEdgeDiffItem = true;
                edgeId = ((ClientApiWorkspaceDiff.EdgeItem) workspaceDiffItem).getEdgeId();
            }
        }
        for (ClientApiWorkspaceDiff.Item workspaceDiffItem : diff.getDiffs()) {
            if (workspaceDiffItem instanceof ClientApiWorkspaceDiff.PropertyItem &&
                    ((ClientApiWorkspaceDiff.PropertyItem) workspaceDiffItem).getElementId().equals(edgeId) &&
                    ((ClientApiWorkspaceDiff.PropertyItem) workspaceDiffItem).getElementType().equals("edge")) {
                foundEdgeVisibilityJsonDiffItem = true;
            }
        }
        assertTrue("foundEdgeDiffItem", foundEdgeDiffItem);
        assertTrue("foundEdgeVisibilityJsonDiffItem", foundEdgeVisibilityJsonDiffItem);
    }

    private void assertHighlightedTextDoesNotContainResolvedEntityForOtherUser(VisalloApi visalloApi) throws VisalloClientApiException {
        String highlightedText = visalloApi.getVertex().getHighlightedText(artifactVertexId, TikaTextExtractorGraphPropertyWorker.MULTI_VALUE_KEY);
        LOGGER.info("%s", highlightedText);
        assertFalse("highlightedText contained string: " + highlightedText, highlightedText.contains("resolvedToVertexId&quot;:&quot;" + joeFernerVertex.getId() + "&quot;"));
    }

    private void assertHighlightedTextContainResolvedEntityForOtherUser(VisalloApi visalloApi) throws VisalloClientApiException {
        String highlightedText = visalloApi.getVertex().getHighlightedText(artifactVertexId, TikaTextExtractorGraphPropertyWorker.MULTI_VALUE_KEY);
        LOGGER.info("%s", highlightedText);
        assertTrue("highlightedText does not contain string: " + highlightedText, highlightedText.contains("resolvedToVertexId&quot;:&quot;" + joeFernerVertex.getId() + "&quot;"));
    }

    private void resolveAndUnresolveTerm(VisalloApi visalloApi) throws VisalloClientApiException {
        int entityStartOffset = "Joe Ferner knows ".length();
        int entityEndOffset = entityStartOffset + "David Singley".length();
        String sign = "David Singley";
        visalloApi.getVertex().postResolveTerm(
                artifactVertexId,
                TikaTextExtractorGraphPropertyWorker.MULTI_VALUE_KEY,
                entityStartOffset, entityEndOffset,
                sign,
                TestOntology.CONCEPT_PERSON,
                "auth1",
                joeFernerVertex.getId(),
                "test",
                null
        );

        ClientApiTermMentionsResponse termMentions = visalloApi.getVertex().getTermMentions(artifactVertexId, TikaTextExtractorGraphPropertyWorker.MULTI_VALUE_KEY, VisalloProperties.TEXT.getPropertyName());
        LOGGER.info("termMentions: %s", termMentions.toString());
        assertEquals(4, termMentions.getTermMentions().size());
        ClientApiElement davidSingleyTermMention = findDavidSingleyTermMention(termMentions);
        LOGGER.info("termMention: %s", davidSingleyTermMention.toString());

        String highlightedText = visalloApi.getVertex().getHighlightedText(artifactVertexId, TikaTextExtractorGraphPropertyWorker.MULTI_VALUE_KEY);
        LOGGER.info("highlightedText: %s", highlightedText);
        ClientApiProperty davidSingleyEdgeId = getProperty(davidSingleyTermMention.getProperties(), "", "http://visallo.org/termMention#resolvedEdgeId");
        String davidSingleyEdgeIdValue = (String) davidSingleyEdgeId.getValue();
        assertTrue("highlightedText invalid: " + highlightedText, highlightedText.contains(">David Singley<") && highlightedText.contains(davidSingleyEdgeIdValue));

        visalloApi.getVertex().postUnresolveTerm(davidSingleyTermMention.getId());

        termMentions = visalloApi.getVertex().getTermMentions(artifactVertexId, TikaTextExtractorGraphPropertyWorker.MULTI_VALUE_KEY, VisalloProperties.TEXT.getPropertyName());
        LOGGER.info("termMentions: %s", termMentions.toString());
        assertEquals(3, termMentions.getTermMentions().size());

        highlightedText = visalloApi.getVertex().getHighlightedText(artifactVertexId, TikaTextExtractorGraphPropertyWorker.MULTI_VALUE_KEY);
        LOGGER.info("highlightedText: %s", highlightedText);
        assertTrue("highlightedText invalid: " + highlightedText, highlightedText.contains(">David Singley<") && !highlightedText.contains(davidSingleyEdgeIdValue));
    }

    private ClientApiElement findDavidSingleyTermMention(ClientApiTermMentionsResponse termMentions) {
        for (ClientApiElement termMention : termMentions.getTermMentions()) {
            for (ClientApiProperty property : termMention.getProperties()) {
                if (property.getName().equals(VisalloProperties.TERM_MENTION_TITLE.getPropertyName())) {
                    if ("David Singley".equals(property.getValue())) {
                        return termMention;
                    }
                }
            }
        }
        throw new RuntimeException("Could not find 'David Singley' in term mentions");
    }
}
