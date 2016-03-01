define([
    'hbs/handlebars',
    'util/service/messagesPromise'
], function(Handlebars, i18nPromise) {
    'use strict';

    i18nPromise
        .then(function(i18n) {
            Handlebars.registerHelper('i18n', function(str) {
                return i18n.apply(null, arguments);
            });
        });


});
