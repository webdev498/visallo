define(['../actions', '../../util/ajax'], function(actions, ajax) {
    actions.protectFromMain();

    const api = {
        get: ({ vertexIds, edgeIds }) => (dispatch, getState) => {
            if (vertexIds && vertexIds.length) {
                ajax('POST', '/vertex/multiple', { vertexIds })
                    .then((result) => {
                        const { vertices } = result;
                        const edges = [];
                        dispatch(api.update({ vertices, edges }))
                    })
            }
        },

        update: ({ vertices, edges }) => ({
            type: 'ELEMENT_UPDATE',
            payload: { vertices, edges }
        })
    }

    return api;
})

