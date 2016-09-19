define(['../actions', '../../util/ajax'], function(actions, ajax) {
    actions.protectFromMain();

    const api = {
        get: ({productId, invalidate}) => (dispatch, getState) => {
            var items = getState().product.items;
            var product = _.findWhere(items, { id: productId })

            if (invalidate || !product || !product.extendedData) {
                return ajax('GET', '/product', {
                    productId,
                    includeExtended: true,
                    params: {
                        includeVertices: true,
                        includeEdges: true
                    }
                })
                    .then(function(product) {
                        dispatch({
                            type: 'PRODUCT_UPDATE',
                            payload: {
                                product
                            }
                        })
                    })
            }
        },

        changedOnServer: (productId) => (dispatch, getState) => {
            // TODO: Should check current workspace
            // not current ? just mark it invalid
            // is current  ? is it selected ? get : load list
            dispatch(api.get({ productId, invalidate: true }));
        },

        updatePositions: ({ productId, updateVertices }) => (dispatch, getState) => {
            var params = { updateVertices },
                product = _.findWhere(getState().product.items, { id: productId }),
                { kind } = product;

            if (!_.isEmpty(updateVertices)) {
                return ajax('POST', '/product', { productId, kind, params })
            }
        },

        updateViewport: ({ productId, pan, zoom }) => ({
            type: 'PRODUCT_UPDATE_VIEWPORT',
            payload: { productId, pan, zoom }
        }),

        list: {
            type: 'dataRequest',
            payload: {
                service: 'product',
                name: 'list'
            }
        },

        create: ({title, kind}) => {
            return ajax('POST', '/product', { title, kind })
                .then((product) => ({ type: 'PRODUCT_UPDATE', payload: { product } }))
        },

        delete: ({ productId }) => {
            return ajax('DELETE', '/product', { productId })
                .then(() => ({ type: 'PRODUCT_REMOVE', payload: { productId } }))
        },

        remove: (productId) => {
            return { type: 'PRODUCT_REMOVE', payload: { productId }}
        },

        select: (payload) => ({
            type: 'PRODUCT_SELECT',
            payload
        })

    }

    return api;
})
