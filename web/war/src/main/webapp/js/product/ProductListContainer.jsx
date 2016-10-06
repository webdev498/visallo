define([
    'react',
    'react-redux',
    './ProductList',
    'data/web-worker/store/product/actions',
    'data/web-worker/store/product/selectors'
], function(React, redux, ProductList, productActions, productSelectors) {
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

        (state, props) => {
            const { items, ...rest } = state.product;

            return {
                ...rest,
                products: productSelectors.getProductsInWorkspace(state)
            }
        },

        (dispatch) => {

            // TODO: manage pane state in store, requires major changes to
            // app.js
            $(document).on('didToggleDisplay', function(event, data) {
                if (data.name === 'products' && data.visible) {
                    requestProducts(dispatch);
                }
            })

            return {
                onCreate: (type) => { dispatch(productActions.create('Untitled', type)) },
                onDeleteProduct: (productId) => { dispatch(productActions.delete(productId)) },
                onSelectProduct: (productId) => { openProductDetail(); dispatch(productActions.select(productId)) }
            }
        }

    )(ProductList);
});
