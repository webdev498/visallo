define([
    'react',
    'react-redux',
    './ProductsPane',
    'data/web-worker/store/product/actions'
], function(React, redux, ProductsPane, productActions) {
    'use strict';

    var requestProducts = _.once(function(dispatch) {
        dispatch(productActions.list)
    });


    return redux.connect(

        (state, props) => ({ product: state.product }),

        (dispatch) => {

            // TODO: manage pane state in store, requires major changes to
            // app.js
            $(document).on('didToggleDisplay', function(event, data) {
                if (data.name === 'products' && data.visible) {
                    requestProducts(dispatch);
                }
            })

            return {
                onTest: () => { dispatch({ type: 'INC' }) }
            }
        }

    )(ProductsPane);
});
