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
    }
})
