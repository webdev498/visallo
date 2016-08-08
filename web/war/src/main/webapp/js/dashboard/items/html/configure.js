define([
    'flight/lib/component',
    'util/withDataRequest',
    'hbs!./configureTpl',
    'quill'
], function(defineComponent,
            withDataRequest,
            template,
            Quill) {
    'use strict';

    return defineComponent(HtmlDashboardItemConfiguration, withDataRequest);

    function HtmlDashboardItemConfiguration() {
        this.defaultAttrs({
            editContentsSelector: 'button.edit-contents'
        });

        this.after('initialize', function() {
            $('#edit-contents-modal').remove();
            this.$node.html(template({html: this.attr.item.configuration.sanitizedHtml}));

            var $modal = $('#edit-contents-modal');
            var $editor = $('.editor', $modal);
            var $toolbar = $('.toolbar', $modal);
            var $saveButton = $('button.save', $modal);

            $modal.appendTo(document.body);

            this.on('click', {
                editContentsSelector: this.onEditContents
            });
            this.on($saveButton, 'click', this.onSaveClick);
            this.preventConfigureClosing($modal);

            this.quill = new Quill($editor.get(0), {
                theme: 'snow'
            });
            this.quill.addModule('toolbar', {container: $toolbar.get(0)});
        });

        this.preventConfigureClosing = function($modal) {
            this.on($modal, 'mousedown', function(e) {
                e.stopPropagation();
            })
        };

        this.onSaveClick = function() {
            var html = this.quill.getHTML();
            if ($(html).text() === '') {
                html = '';
            }
            this.attr.item.configuration.sanitizedHtml = html;
            this.trigger('configurationChanged', {
                extension: this.attr.extension,
                item: this.attr.item
            });
            this.trigger('closePopover');
            $('#edit-contents-modal').modal('hide');
        };

        this.onEditContents = function() {
            $('#edit-contents-modal').modal('show');
        };
    }
});
