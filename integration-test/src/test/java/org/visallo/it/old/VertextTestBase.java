package org.visallo.it.old;

public abstract class VertextTestBase extends TestBase {
//    protected static final int NUM_DEFAULT_PROPERTIES = 2;
//    protected static final List<String> PUBLIC_VISIBILITY = ImmutableList.of("");
//    protected static final String PROPERTY_NAME = "http://visallo.org/test#firstName";
//    protected static final String PROPERTY_KEY_PREFIX = "key-firstName-";
//    protected static final String PROPERTY_VALUE_PREFIX = "First Name ";
//    protected static final String EDGE_LABEL1 = "http://visallo.org/test#worksFor";
//    protected static final String EDGE_LABEL2 = "http://visallo.org/test#sibling";
//
//    protected VisalloApi setupVisalloApi;
//    protected VertexApiExt setupVertexApi;
//    protected EdgeApi setupEdgeApi;
//
//    @Before
//    public void setUp() throws VisalloClientApiException {
//        setupVisalloApi = login(USERNAME_TEST_USER_1);
//        setupVertexApi = setupVisalloApi.getVertex();
//        setupEdgeApi = setupVisalloApi.getEdgeApi();
//        addUserAuths(setupVisalloApi, USERNAME_TEST_USER_1, "a", "b", "c", "d", "e", "f", "x", "y", "z");
//    }
//
//    protected List<String> createVertices(int numVertices, List<String> vertexVisibilities,
//                                          int numPropertiesPerVertex, List<String> propertyVisibilities)
//            throws ApiException {
//        LoopingIterator vertexVizIterator = new LoopingIterator(vertexVisibilities);
//        LoopingIterator propertyVizIterator = new LoopingIterator(propertyVisibilities);
//        List<String> vertexIds = new ArrayList<>();
//        for (int i = 0; i < numVertices; i++) {
//            ClientApiElement vertex = setupVertexApi.create(TestOntology.CONCEPT_PERSON, (String) vertexVizIterator.next(), "justification");
//            String vertexId = vertex.getId();
//            setVertexProperties(numPropertiesPerVertex, propertyVizIterator, vertexId);
//            vertexIds.add(vertexId);
//        }
//        return vertexIds;
//    }
//
//    protected List<String> createPublicVertices(int numVertices, int numPropertiesPerVertex) throws VisalloClientApiException {
//        return createVertices(numVertices, PUBLIC_VISIBILITY, numPropertiesPerVertex, PUBLIC_VISIBILITY);
//    }
//
//    protected void setVertexProperties(int numPropertiesPerVertex, LoopingIterator propertyAuthIterator, String vertexId)
//            throws VisalloClientApiException {
//        for (int j = 0; j < numPropertiesPerVertex; j++) {
//            setupVertexApi.setProperty(vertexId, PROPERTY_KEY_PREFIX + j, PROPERTY_NAME, PROPERTY_VALUE_PREFIX + j,
//                    (String) propertyAuthIterator.next(), "", null, null);
//
//        }
//    }
//
//    protected void createEdge(String sourceVertexId, String destVertexId, String edgeLabel) throws VisalloClientApiException {
//        setupEdgeApi.create(sourceVertexId, destVertexId, edgeLabel, "", "ok", "{}", null);
//    }
//
//    protected void assertVertexIds(List<String> expectedVertexIds, List<ClientApiVertex> actualVertices) {
//        List<String> expectedIds = new ArrayList<>(expectedVertexIds);
//        List<String> actualIds = new ArrayList<>(Lists.transform(actualVertices, new Function<ClientApiVertex, String>() {
//            public String apply(ClientApiVertex vertex) {
//                return vertex.getId();
//            }
//        }));
//        Collections.sort(expectedIds);
//        Collections.sort(actualIds);
//
//        assertEquals(expectedIds, actualIds);
//    }
}
