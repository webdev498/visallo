
define([
    'flight/lib/component',
    './loginTpl.hbs',
    'configuration/plugins/registry',
    'util/withDataRequest',
    './util/alert.ejs',
    'util/service/propertiesPromise'
], function(
    defineComponent,
    template,
    registry,
    withDataRequest,
    alertTemplate,
    configPromise) {
    'use strict';

    return defineComponent(Login, withDataRequest);

    function Login() {

        this.defaultAttrs({
            authenticationSelector: '.authentication'
        });

        this.before('teardown', function() {
            this.$node.remove();
        });

        this.after('initialize', function() {
            var self = this;

            configPromise.then(function(configProperties) {

                registry.documentExtensionPoint('org.visallo.authentication',
                    'Provides interface for authentication',
                    function(e) {
                        return _.isString(e.componentPath);
                    }
                );

                self.$node.html(template({ showPoweredBy: configProperties['login.showPoweredBy'] === 'true' }));
                var authPlugins = registry.extensionsForPoint('org.visallo.authentication'),
                    authNode = self.select('authenticationSelector'),
                    error = '',
                    componentPath = '';

                self.on('showErrorMessage', function(event, data) {
                    authNode.html(alertTemplate({ error: data.message }));
                })

                if (authPlugins.length === 0) {
                    console.warn('No authentication extension registered, Falling back to old plugin');
                    componentPath = 'configuration/plugins/authentication/authentication';
                } else if (authPlugins.length > 1) {
                    error = 'Multiple authentication extensions registered. (See console for more)';
                    console.error('Authentication plugins:', authPlugins);
                } else {
                    componentPath = authPlugins[0].componentPath;
                }

                if (error) {
                    authNode.html(alertTemplate({ error: error }));
                } else if (componentPath) {
                    require([componentPath], function(AuthenticationPlugin) {
                        AuthenticationPlugin.attachTo(authNode, {
                            errorMessage: self.attr.errorMessage || ''
                        });
                    });
                }
            });
        });

    }

});
