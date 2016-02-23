define([
    'flight/lib/component',
    './template.hbs'
], function(
    defineComponent,
    template) {
    'use strict';

    return defineComponent(AuthenticationMissing);

    function AuthenticationMissing() {

        this.after('initialize', function() {
            this.$node.html(template({}));
        });

    }
});
