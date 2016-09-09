define(['data/web-worker/store'], function(store) {
    'use strict';

    const validActionKeys = 'type payload error meta'.split(' ');

    return function(message) {
        var action = message.data.action;
        if (isValidAction(action)) {
            store.getStore().dispatch(action);
        } else {
            console.error('Store action is invalid: ', action);
        }
    };

    // Similar to https://github.com/acdlite/flux-standard-action
    function isValidAction(action) {
        return (
            _.isObject(action) &&
            _.isString(action.type) &&
            Object.keys(action).every(isValidKey)
        );

        function isValidKey(key) { return validActionKeys.indexOf(key) > -1; }
    }
});
