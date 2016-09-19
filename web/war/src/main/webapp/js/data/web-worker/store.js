(function() {
    'use strict';

    define([
        'configuration/plugins/registry',
        'fast-json-patch',
        'redux',
        './store/rootReducer',

        // Middleware
        './store/middleware/actionRouter',
        './store/middleware/thunk',
        './store/middleware/promise',
        './store/middleware/dataRequest',
        './store/middleware/logger'
    ], function(registry, jsonpatch, redux, rootReducer, ...middleware) {
        var store;

        return {
            getStore: function() {
                if (!store) {
                    store = redux.createStore(
                        rootReducer,
                        redux.applyMiddleware(...middleware)
                    );
                    store.subscribe(stateChanged(store.getState()))
                }
                return store;
            }
        };

        // Send worker state changes to main thread as JSON-patches
        function stateChanged(initialState) {
            var previousState = initialState;
            return function storeSubscription() {
                var newState = store.getState();
                if (newState !== previousState) {
                    var diff = jsonpatch.compare(previousState, newState);
                    if (diff && diff.length) {
                        previousState = newState;
                        dispatchMain('reduxStoreAction', {
                            action: {
                                type: 'STATE_APPLY_DIFF',
                                payload: diff,
                                meta: {
                                    originator: 'webworker'
                                }
                            }
                        })
                    }
                }
            }
        }
    })
})()
