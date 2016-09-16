/*eslint strict:0 */
define([
    'configuration/plugins/registry',
    'redux',

    // Reducers
    './dashboard/reducer',
    './product/reducer',
    './workspace/reducer',
    './user/reducer',
    './selection/reducer'

    // Add reducers above, the name of the function will be used as the key
], function(registry, redux, ...reducers) {

    // TODO: add all from an undocumented extension point (for internal use
    // only for this release)

    var reducerMap = redux.combineReducers(
        _.object(
            reducers.map(reducerFn => [reducerFn.name, reducerFn])
        )
    );

    return reducerMap;
});
