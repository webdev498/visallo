define(['../actions', '../../util/ajax'], function(actions, ajax) {
    actions.protectFromMain();

    const api = {
        get: ({ vertexIds, edgeIds }) => (dispatch, getState) => {
            if (vertexIds && vertexIds.length) {
                // TODO: don't request if in state
                ajax('POST', '/vertex/multiple', { vertexIds })
                    .then((result) => {
                        const { vertices } = result;
                        const edges = [];
                        dispatch(api.update({ vertices, edges, workspaceId: getState().workspace.currentId }))
                    })
            }
        },

        update: ({ vertices, edges, workspaceId }) => ({
            type: 'ELEMENT_UPDATE',
            payload: { vertices, edges, workspaceId }
        }),

        dragEnd: (payload) => (dispatch, getState) => {
            dispatch({ type: 'ELEMENT_DRAGEND', payload })
        }
    }

    return api;
})

