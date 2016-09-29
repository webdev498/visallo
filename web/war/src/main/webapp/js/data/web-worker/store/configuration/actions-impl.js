define(['../actions', '../../util/ajax'], function(actions, ajax) {
    actions.protectFromMain();

    const api = {
        get: () => {
            return ajax('GET', '/configuration')
                .then(result => api.update(result))
        },

        update: (configuration) => ({
            type: 'CONFIGURATION_UPDATE',
            payload: configuration
        })
    }

    return api;
})

