define(['../actions'], function(actions) {
    actions.protectFromWorker();

    return actions.createActions({
        workerImpl: 'data/web-worker/store/product/actions-impl',
        actions: {
            list: null,
            get: (productId) => ({ productId }),
            create: (title, kind, params) => ({ title, kind, params }),
            select: (productId) => ({ productId }),
            delete: (productId) => ({ productId }),
            updatePositions: (productId, updateVertices) => ({ productId, updateVertices }),
            updateViewport: (productId, { pan, zoom }) => ({ productId, pan, zoom }),
            dropElements: (productId, elements, position) => ({ productId, elements, position })
        }
    })
})
