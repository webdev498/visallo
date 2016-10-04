define([], function() {
    'use strict';

    var api = {
        properties: function(locale) { return Promise.resolve(CONFIG_JSON.properties) },
        messages: function(locale) { return Promise.resolve(CONFIG_JSON.messages) }
    };

    return api;
});
