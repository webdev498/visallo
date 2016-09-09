/*eslint strict:0 */
define([
    'configuration/plugins/registry',
    'redux',

    // Reducers
    './dashboard/reducer'

    // Add reducers above, the name of the function will be used as the key
], function(registry, redux, ...reducers) {

    return redux.combineReducers(
        _.object(
            reducers.map(reducerFn => [reducerFn.name, reducerFn])
        )
    );
});
