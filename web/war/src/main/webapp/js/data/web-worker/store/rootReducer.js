/*eslint strict:0 */
define([
    'configuration/plugins/registry',
    'redux',

    // Reducers
    './dashboard/reducer',
    './product/reducer'

    // Add reducers above, the name of the function will be used as the key
], function(registry, redux, ...reducers) {

    var reducerMap = redux.combineReducers(
        _.object(
            reducers.map(reducerFn => [reducerFn.name, reducerFn])
        )
    );

    return reducerMap;
    //function(state, action) {
        //console.log(action.type)
        //return reducerMap.apply(null, arguments);
    //};
});
