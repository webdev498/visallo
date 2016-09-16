define(['../actions', '../../util/ajax'], function(actions, ajax) {
    actions.protectFromMain();

    return {
        get: ({productId, invalidate}, getState) => {
            var items = getState().product.items;
            var product = _.findWhere(items, { id: productId })

            if (invalidate || !product.extendedData) {
                return ajax('GET', '/product', {
                    productId,
                    includeExtended: true,
                    params: {
                        includeVertices: true,
                        includeEdges: true
                    }
                })
                    .then(function(product) {
                        return {
                            type: 'PRODUCT_UPDATE',
                            payload: {
                                product
                            }
                        }
                    })
            }
        },

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

        remove: ({ productId }) => {
            return { type: 'PRODUCT_REMOVE', payload: { productId }}
        },

        select: (payload) => ({
            type: 'PRODUCT_SELECT',
            payload
        }),

        deleteProduct: (productId) => ({
            type: 'dataRequest',
            payload: {
                service: 'product',
                name: 'deleteProduct',
                params: [productId]
            }
        }),

        removeProduct: (productId) => ({
            type: 'removeProduct',
            payload: { productId }
        }),

        getProduct: (productId, includeExtended) => ({
            type: 'dataRequest',
            payload: {
                service: 'product',
                name: 'getProduct',
                params: [productId, includeExtended]
            }
        }),

    }
})
