package org.visallo.it;

import com.google.common.collect.ImmutableList;
import org.junit.Test;
import org.visallo.web.clientapi.VisalloClientApiException;
import org.visallo.web.clientapi.model.ClientApiVertex;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.google.common.collect.Iterables.toArray;
import static org.junit.Assert.*;

public class VertexTest extends IntegrationTestBase {
    @Test
    public void testFindMultiple() throws VisalloClientApiException {
        List<String> allVertexIds = createVertices(
                3, ImmutableList.of("a", "b", "c"),
                3, ImmutableList.of("x", "y", "z")
        );

        setAuths(USERNAME_TEST_USER_1, "a", "b", "x", "y");

        List<ClientApiVertex> vertices = api.getVertex().postMultiple(toArray(allVertexIds, String.class), false).getVertices();

        assertEquals(2, vertices.size());
        for (ClientApiVertex vertex : vertices) {
            assertEquals(3 + NUM_DEFAULT_PROPERTIES, vertex.getProperties().size());
        }

        final List<String> allVertexIdsIncludingBadOne = new ArrayList<>();
        allVertexIdsIncludingBadOne.addAll(allVertexIds);
        allVertexIdsIncludingBadOne.add("bad");
        Map<String, Boolean> exists = api.getVertex().getExists(toArray(allVertexIdsIncludingBadOne, String.class)).getExists();
        assertEquals(4, exists.size()); // should include 2 you can see, one you can't see, and one with a bad id
        for (ClientApiVertex vertex : vertices) {
            assertTrue(vertex.getId() + " should exist", exists.get(vertex.getId()));
        }
        assertFalse("bad should not exist", exists.get("bad"));
    }
}
