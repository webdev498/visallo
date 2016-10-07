
define([
    '../util/ajax',
    './storeHelper',
    '../store',
    'require'
], function(ajax, storeHelper, store, require) {
    'use strict';

    var api = {

        create: function(options) {
            return ajax('POST', '/edge/create', options);
        },

        'delete': function(edgeId) {
            return ajax('DELETE', '/edge', {
                edgeId: edgeId
            });
        },

        exists: function(edgeIds) {
            return ajax(edgeIds.length > 1 ? 'POST' : 'GET', '/edge/exists', {
                edgeIds: edgeIds
            });
        },

        properties: function(edgeId) {
            return ajax('GET', '/edge/properties', {
                graphEdgeId: edgeId
            });
        },

        setPropertyVisibility: function(edgeId, property) {
            return ajax('POST', '/edge/property/visibility', {
                graphEdgeId: edgeId,
                newVisibilitySource: property.visibilitySource,
                oldVisibilitySource: property.oldVisibilitySource,
                propertyKey: property.key,
                propertyName: property.name
            })
        },

        setProperty: function(edgeId, property, optionalWorkspaceId) {
            var url = storeHelper.edgePropertyUrl(property);
            return ajax('POST', url, _.tap({
                 edgeId: edgeId,
                 propertyName: property.name,
                 value: property.value,
                 visibilitySource: property.visibilitySource,
                 justificationText: property.justificationText
            }, function(params) {
                if (property.sourceInfo) {
                    params.sourceInfo = JSON.stringify(property.sourceInfo);
                }
                if (property.key) {
                    params.propertyKey = property.key;
                }
                if (property.metadata) {
                    params.metadata = JSON.stringify(property.metadata)
                }
                if (optionalWorkspaceId) {
                    params.workspaceId = optionalWorkspaceId;
                }
            }));
        },

        deleteProperty: function(edgeId, property) {
            var url = storeHelper.edgePropertyUrl(property);
            return ajax('DELETE', url, {
                edgeId: edgeId,
                propertyName: property.name,
                propertyKey: property.key
            })
        },

        details: function(edgeId) {
            return ajax('GET', '/edge/details', { edgeId: edgeId });
        },

        history: function(edgeId) {
            return ajax('GET', '/edge/history', {
                graphEdgeId: edgeId
            });
        },

        propertyHistory: function(edgeId, property, options) {
            return ajax('GET', '/edge/property/history', _.extend(
                {},
                options || {},
                {
                    graphEdgeId: edgeId,
                    propertyName: property.name,
                    propertyKey: property.key
                }
            ));
        },

        multiple: function(options) {
            const state = store.getStore().getState();
            const workspaceId = state.workspace.currentId;
            const elements = state.element[workspaceId];
            const returnSingular = !_.isArray(options.edgeIds);
            const edgeIds = returnSingular ? [options.edgeIds] : options.edgeIds;
            var toRequest = edgeIds;
            if (elements) {
                toRequest = _.reject(toRequest, id => id in elements.edges);
            }

            return (
                toRequest.length ?
                ajax('POST', '/edge/multiple', { edgeIds: toRequest }) :
                Promise.resolve({edges: []})
            ).then(function({edges}) {
                if (edges.length) {
                    require(['../store/element/actions-impl'], function(actions) {
                        store.getStore().dispatch(actions.update({ edges, workspaceId }));
                    });
                }

                if (elements) {
                    const existing = _.pick(elements.edges, edgeIds)
                    return Object.values(existing).concat(edges)
                }
                return edges;
            }).then(function(ret) {
                return returnSingular && ret.length ? ret[0] : ret;
            })
        },

        store: function(options) {
            return api.multiple(options)
        },

        setVisibility: function(edgeId, visibilitySource) {
            return ajax('POST', '/edge/visibility', {
                graphEdgeId: edgeId,
                visibilitySource: visibilitySource
            });
        }
    };

    return api;
});
