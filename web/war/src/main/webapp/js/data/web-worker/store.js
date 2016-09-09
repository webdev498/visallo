define([
    'configuration/plugins/registry',
    'fast-json-patch',
    'redux',
    './store/rootReducer'
], function(registry, jsonpatch, redux, rootReducer) {
    'use strict';

    var store;

    return {
        getStore: function() {
            if (!store) {
                store = redux.createStore(rootReducer);
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

    function rootReducerOld(state, action) {
        if (!state) return { initialState: true }
        var payload = action.payload;
        switch (action.type) {
            case 'CHANGE_INITIAL_STATE': return { ...state, ...payload };
        }
        return state;
    }
})
