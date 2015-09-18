package org.visallo.it.old;

import org.junit.experimental.categories.Category;

@Category(BenchmarkCategory.class)
public class VertexBenchmarkTest extends VertextTestBase {
//    private TimedExecution timedExecution;
//
//    @Rule
//    public TestClassAndMethod testClassAndMethod = new TestClassAndMethod();
//
//    @Before
//    public void setUp() throws VisalloClientApiException {
//        super.setUp();
//        timedExecution = new TimedExecution(testClassAndMethod);
//    }
//
//    @Test
//    public void benchmarkFindMultiple10Vertices10Properties() {
//        benchmarkFindMultipleVertices(10, 10, 60);
//    }
//
//    @Test
//    public void benchmarkFindMultiple100Vertices10Properties() {
//        benchmarkFindMultipleVertices(100, 10, 250);
//    }
//
//    private void benchmarkFindMultipleVertices(int numVertices, int numPropertiesPerVertex, long maxTimeMillis) {
//        try {
//            final List<String> allVertexVisibilities = ImmutableList.of("a", "b", "c");
//            final List<String> allPropertyVisibilities = ImmutableList.of("x", "y", "z");
//            final List<String> allVertexIds = createVertices(
//                    numVertices, allVertexVisibilities,
//                    numPropertiesPerVertex, allPropertyVisibilities);
//            String setupWorkspaceId = setupVisalloApi.getWorkspaceId();
//            VisalloApi visalloApi = login(USERNAME_TEST_USER_2);
//            List<String> userVertexAuthorizations = allVertexVisibilities.subList(0, 2);
//            List<String> userPropertyAuthorizations = allPropertyVisibilities.subList(0, 2);
//            List<String> allUserAuthorizations = new ArrayList<>();
//            allUserAuthorizations.addAll(userVertexAuthorizations);
//            allUserAuthorizations.addAll(userPropertyAuthorizations);
//            allUserAuthorizations.add(setupWorkspaceId);
//            addUserAuths(visalloApi, USERNAME_TEST_USER_2,
//                    allUserAuthorizations.toArray(new String[allUserAuthorizations.size()]));
//            final VertexApi vertexApi = visalloApi.getVertex();
//
//            TimedExecution.Result<ClientApiVertexMultipleResponse> timedResponse = timedExecution.call(
//                    new Callable<ClientApiVertexMultipleResponse>() {
//                        public ClientApiVertexMultipleResponse call() throws Exception {
//                            return vertexApi.findMultiple(allVertexIds, false);
//                        }
//                    });
//            List<ClientApiVertex> vertices = timedResponse.result.getVertices();
//
//            int expectedVertexCount = (numVertices * userVertexAuthorizations.size() / allVertexVisibilities.size()) +
//                    numVertices % allVertexVisibilities.size();
//            assertEquals(expectedVertexCount, vertices.size());
//            assertThat(timedResponse.timeMillis, lessThanOrEqualTo(maxTimeMillis));
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }
}
