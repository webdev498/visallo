define([], function() {
    'use strict';

    const validActionKeys = 'type payload error meta'.split(' ');

    return {

        // Similar to https://github.com/acdlite/flux-standard-action
        isValidAction: function(action) {
            return (
                _.isObject(action) &&
                _.isString(action.type) &&
                Object.keys(action).every(isValidKey)
            );

            function isValidKey(key) { return validActionKeys.indexOf(key) > -1; }
        }
    }
})
