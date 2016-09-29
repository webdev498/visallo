
define([
    '../util/ajax',
    '../store'
], function(ajax, store) {
    'use strict';

    /*
            var data = {};
            if (locale) {
                if (locale.language) {
                    data.localeLanguage = locale.language;
                }
                if (locale.country) {
                    data.localeCountry = locale.country;
                }
                if (locale.variant) {
                    data.localeVariant = locale.variant;
                }
            }
    */

    const api = {
        // FIXME: use locale { language, country, variant } => camelcase
        // with locale to send to /configuration
        properties: (locale) => store.getOrWaitForNestedState(s => s.configuration.properties),
        messages: (locale) => store.getOrWaitForNestedState(s => s.configuration.messages)
    };

    return api;

});
