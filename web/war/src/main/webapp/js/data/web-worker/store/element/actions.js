define(['../actions'], function(actions) {
    actions.protectFromWorker();

    return actions.createActions({
        workerImpl: 'data/web-worker/store/element/actions-impl',
        actions: {
            get: ({ workspaceId, vertices, edges }) => ({ vertices, edges }),
            dragEnd: ({ workspaceId, position, ids }) => ({ workspaceId, position, ids })
        }
    })
})

