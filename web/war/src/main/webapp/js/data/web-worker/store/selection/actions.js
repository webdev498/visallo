define(['../actions'], function(actions) {
    actions.protectFromWorker();

    return actions.createActions({
        workerImpl: 'data/web-worker/store/selection/actions-impl',
        actions: {
            add: (ids) => Array.isArray(ids) ? { ids } : { ids: [ids] },
            remove: (ids) => Array.isArray(ids) ? { ids } : { ids: [ids] },
            clear: null
        }
    })
})
