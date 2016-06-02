define([
    'flight/lib/component',
    'hbs!./configureTpl'
], function(
    defineComponent,
    template) {
    'use strict';

    return defineComponent(ConfigureEmbed);

    function ConfigureEmbed() {

        this.attributes({
            item: null,
            extension: null,
            textareaSelector: 'textarea'
        })

        this.after('initialize', function() {
            var config = this.attr.item.configuration;

            this.on('keyup', _.debounce(this.onChange.bind(this), 1000));
            this.on('paste change', this.onChange);

            this.$node.html(template({
                content: config && config.content || ''
            }));
        });

        this.onChange = function() {
            this.attr.item.configuration.content = this.select('textareaSelector').val();
            this.trigger('configurationChanged', {
                item: this.attr.item,
                extension: this.attr.extension
            });
        };
    }
});
