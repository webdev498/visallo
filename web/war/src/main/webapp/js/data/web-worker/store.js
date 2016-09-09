define([
    'configuration/plugins/registry',
    'fast-json-patch',
    'redux',
    './store/rootReducer',
    './store/actions'
], function(registry, jsonpatch, redux, rootReducer, storeActions) {
    'use strict';

    var store;

    return {
        getStore: function() {
            if (!store) {
                store = redux.createStore(
                    rootReducer,
                    redux.applyMiddleware(dataRequest, reduxPromise)
                );
                store.subscribe(stateChanged(store.getState()))
            }
            return store;
        }
    };

    function stateChanged(initialState) {
        var previousState = initialState;
        return () => {
            var newState = store.getState(),
                diff = jsonpatch.compare(previousState, newState);

            previousState = newState;
            if (diff) {
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

    // Similar to redux-promise but without es6 deps
    // (lodash, flux-standard-action)
    function reduxPromise({ dispatch }) {
        return function (next) {
            return function (action) {
                if (!storeActions.isValidAction(action)) {
                    return isPromise(action) ? action.then(dispatch) : next(action);
                }

                return isPromise(action.payload) ? action.payload.then(function(result) {
                    return dispatch({ ...action, payload: result });
                }, function (error) {
                    return dispatch({ ...action, payload: error, error: true });
                }) : next(action);
            };
        };

        function isPromise(obj) {
            return obj && _.isFunction(obj.then);
        }
    }

    // Transforms dataRequest actions into promise chain which
    // dispatches multiple actions about the state of request
    function dataRequest({ dispatch }) {
        return function (next) {
            return function (action) {
                if (storeActions.isValidAction(action)) {
                    if (action.type === 'dataRequest') {
                        const { service, name, params = [] } = action.payload;
                        const prefix = [service, name, 'dataRequest'].join('_');
                        return Promise.require(`data/web-worker/services/${service}`)
                            .then(function(service) {
                                dispatch({
                                    type: prefix + 'Loading',
                                    payload: { ...action.payload }
                                })
                                return service[name].apply(null, params)
                            })
                            .then(function(result) {
                                dispatch({
                                    type: prefix + 'Success',
                                    payload: { ...action.payload, result }
                                })
                            })
                            .catch(function(error) {
                                dispatch({
                                    type: prefix + 'Failure',
                                    payload: { ...action.payload, error }
                                })
                            })
                    }
                }

                next(action);
            };
        };

        function isPromise(obj) {
            return obj && _.isFunction(obj.then);
        }
    }
})
