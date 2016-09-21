define(['../actions'], function(actions) {
    actions.protectFromWorker();

    return actions.createActions({
        workerImpl: 'data/web-worker/store/element/actions-impl',
        actions: {
            get: ({ vertices, edges }) => ({ vertices, edges })
        }
    })
})

