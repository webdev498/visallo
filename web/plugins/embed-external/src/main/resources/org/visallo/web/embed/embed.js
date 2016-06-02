define([
    'flight/lib/component'
], function(defineComponent) {
    'use strict';

    return defineComponent(Embed);

    function Embed() {

        this.attributes({
            item: null,
            extension: null
        });

        this.after('initialize', function() {
            var self = this,
                config = this.attr.item.configuration;

            this.$node.addClass('org-visallo-web-embed');
            if (config && config.content) {
                this.$node.html(
                    //$('<div>')
                        //.addClass('org-visallo-web-embed')
                        //.html(this.attr.item.configuration.content)
                        this.attr.item.configuration.content
                );
            } else {
                this.$node.html(
                    $('<a>').text('Please configure content to display')
                        .on('click', function(e) {
                            e.preventDefault();
                            self.trigger('configureItem');
                        })
                );
            }
        });

    }
});
