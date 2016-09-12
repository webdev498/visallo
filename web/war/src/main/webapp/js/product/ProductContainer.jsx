define([
    'react',
    'react-redux',
    './ProductsPane',
    'data/web-worker/store/product/actions'
], function(React, redux, ProductsPane, productActions) {
    'use strict';

    var requestProducts = _.once(function(dispatch) {
            dispatch(productActions.list)
        }),
        openProductDetail = function() {
            if (!$(document).find('.products-full-pane.visible').length) {
                $(document).trigger('menubarToggleDisplay', {
                    name: 'products',
                    nameFull: 'products-full',
                    action: { type: 'full', componentPath: 'product/ProductDetailContainer' }
                })
            }
        };

    return redux.connect(

        (state, props) => ({ product: state.product, selected: state.product.selected }),

        (dispatch) => {

            // TODO: manage pane state in store, requires major changes to
            // app.js
            $(document).on('didToggleDisplay', function(event, data) {
                if (data.name === 'products' && data.visible) {
                    requestProducts(dispatch);
                }
            })

            return {
                onCreateGraph: () => { dispatch(productActions.tempCreateGraph) },
                onDeleteProduct: (productId) => { dispatch(productActions.deleteProduct(productId)) },
                onSelectProduct: (productId) => { openProductDetail(); dispatch(productActions.selectProduct(productId)) }
            }
        }

    )(ProductsPane);
});
