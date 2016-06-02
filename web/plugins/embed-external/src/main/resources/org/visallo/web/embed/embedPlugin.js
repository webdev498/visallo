define(['configuration/plugins/registry'], function(registry) {
    'use strict';

    registry.registerExtension('org.visallo.web.dashboard.item', {
        title: 'Embed External',
        description: 'Embed External Content',
        identifier: 'org-visallo-web-embed-external',
        componentPath: 'org/visallo/web/embed/embed',
        configurationPath: 'org/visallo/web/embed/configure',
        options: {
            flushContent: true
        }
    });
})
