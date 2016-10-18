define(['util/promise'], function(Promise) {
    return {
        getOrWaitForNestedState: function(callback){
            var parsedOntology = JSON.parse(ONTOLOGY_JSON);

            var ontology = {
                ontology: {
                    concepts: _.indexBy(parsedOntology.concepts, "id"),
                    relationships: _.indexBy(parsedOntology.relationships, "title"),
                    properties: _.indexBy(parsedOntology.properties, "title")
                }
            };
            callback(ontology);
            return Promise.resolve(ontology.ontology);
        }
    }
})