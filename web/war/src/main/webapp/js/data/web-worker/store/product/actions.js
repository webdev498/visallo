define([], {
    list: {
        type: 'dataRequest',
        payload: {
            service: 'product',
            name: 'list'
        }
    },

    tempCreateGraph: {
        type: 'dataRequest',
        payload: {
            service: 'product',
            name: 'createGraph'
        }
    },

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

    selectProduct: (productId) => ({
        type: 'selectProduct',
        payload: { productId }
    })
})
