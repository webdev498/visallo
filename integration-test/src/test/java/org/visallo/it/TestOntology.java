package org.visallo.it;

import org.visallo.core.model.properties.types.*;

public class TestOntology {
    public static final String IRI = "http://visallo.org/test";

    public static final String EDGE_LABEL_ARTIFACT_HAS_ENTITY = "http://visallo.org/test#artifactHasEntity";
    public static final String EDGE_LABEL_HAS_IMAGE = "http://visallo.org/test#entityHasImageRaw";
    public static final String EDGE_LABEL_RAW_CONTAINS_IMAGE_OF_ENTITY = "http://visallo.org/test#rawContainsImageOfEntity";
    public static final String EDGE_LABEL_SIBLING = "http://visallo.org/test#sibling";
    public static final String EDGE_LABEL_WORKS_FOR = "http://visallo.org/test#worksFor";

    public static final String CONCEPT_TYPE_ARTIFACT = "http://visallo.org/test#artifact";
    public static final String CONCEPT_TYPE_AUDIO = "http://visallo.org/test#audio";
    public static final String CONCEPT_TYPE_CITY = "http://visallo.org/test#city";
    public static final String CONCEPT_TYPE_COMPANY = "http://visallo.org/test#company";
    public static final String CONCEPT_TYPE_CONTACT_INFORMATION = "http://visallo.org/test#contactInformation";
    public static final String CONCEPT_TYPE_COUNTRY = "http://visallo.org/test#country";
    public static final String CONCEPT_TYPE_DOCUMENT = "http://visallo.org/test#document";
    public static final String CONCEPT_TYPE_EMAIL_ADDRESS = "http://visallo.org/test#emailAddress";
    public static final String CONCEPT_TYPE_IMAGE = "http://visallo.org/test#image";
    public static final String CONCEPT_TYPE_LOCATION = "http://visallo.org/test#location";
    public static final String CONCEPT_TYPE_ORGANIZATION = "http://visallo.org/test#organization";
    public static final String CONCEPT_TYPE_PERSON = "http://visallo.org/test#person";
    public static final String CONCEPT_TYPE_PHONE_NUMBER = "http://visallo.org/test#phoneNumber";
    public static final String CONCEPT_TYPE_STATE = "http://visallo.org/test#state";
    public static final String CONCEPT_TYPE_VIDEO = "http://visallo.org/test#video";
    public static final String CONCEPT_TYPE_ZIP_CODE = "http://visallo.org/test#zipCode";

    public static final DateVisalloProperty BIRTH_DATE = new DateVisalloProperty("http://visallo.org/test#birthDate");
    public static final StringVisalloProperty CITY_PROP = new StringVisalloProperty("http://visallo.org/test#cityProp");
    public static final StringVisalloProperty CSV_MAPPING_JSON = new StringVisalloProperty("http://visallo.org/csv#mappingJson");
    public static final StringVisalloProperty FIRST_NAME = new StringVisalloProperty("http://visallo.org/test#firstName");
    public static final GeoPointVisalloProperty GEOLOCATION = new GeoPointVisalloProperty("http://visallo.org/test#geolocation");
    public static final StringVisalloProperty LAST_NAME = new StringVisalloProperty("http://visallo.org/test#lastName");
    public static final IntegerVisalloProperty MEDIA_CLOCKWISE_ROTATION = new IntegerVisalloProperty("http://visallo.org/media#clockwiseRotation");
    public static final BooleanVisalloProperty MEDIA_Y_AXIS_FLIPPED = new BooleanVisalloProperty("http://visallo.org/media#yAxisFlipped");
    public static final StringVisalloProperty NAME = new StringVisalloProperty("http://visallo.org/test#name");
    public static final StringVisalloProperty VISALLO_ORG_SUBRIP = new StringVisalloProperty("http://visallo.org#subrip");

    public static final String INTENT_ARTIFACT_CONTAINS_IMAGE = "artifactContainsImage";
    public static final String INTENT_ARTIFACT_CONTAINS_IMAGE_OF_ENTITY = "artifactContainsImageOfEntity";
    public static final String INTENT_ARTIFACT_HAS_ENTITY = "artifactHasEntity";
    public static final String INTENT_AUDIO = "audio";
    public static final String INTENT_CITY = "city";
    public static final String INTENT_COUNTRY = "country";
    public static final String INTENT_CSV = "csv";
    public static final String INTENT_DOCUMENT = "document";
    public static final String INTENT_EMAIL = "email";
    public static final String INTENT_ENTITY_HAS_IMAGE = "entityHasImage";
    public static final String INTENT_ENTITY_IMAGE = "entityImage";
    public static final String INTENT_FACE = "face";
    public static final String INTENT_GEO_LOCATION = "geoLocation";
    public static final String INTENT_IMAGE = "image";
    public static final String INTENT_LOCATION = "location";
    public static final String INTENT_MEDIA_CLOCKWISE_ROTATION = "media.clockwiseRotation";
    public static final String INTENT_MEDIA_Y_AXIS_FLIPPED = "media.yAxisFlipped";
    public static final String INTENT_ORGANIZATION = "organization";
    public static final String INTENT_PERSON = "person";
    public static final String INTENT_PHONE_NUMBER = "phoneNumber";
    public static final String INTENT_RDF = "rdf";
    public static final String INTENT_STATE = "state";
    public static final String INTENT_VIDEO = "video";
    public static final String INTENT_ZIP_CODE = "zipCode";
}
