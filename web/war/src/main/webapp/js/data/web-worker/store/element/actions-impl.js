define(['../actions', '../../util/ajax'], function(actions, ajax) {
    actions.protectFromMain();

    const api = {
        get: ({ vertexIds, edgeIds }) => (dispatch, getState) => {
            if (vertexIds && vertexIds.length) {
                const state = getState();
                const workspaceId = state.workspace.currentId;
                const elements = state.element[workspaceId]
                const toRequest = { vertexIds, edgeIds };

                // TODO: edgeIds
                if (elements && elements.vertices) {
                    toRequest.vertexIds = _.reject(toRequest.vertexIds, (vId) => vId in elements.vertices);
                }

                if (toRequest.vertexIds.length) {
                    ajax('POST', '/vertex/multiple', { vertexIds: toRequest.vertexIds })
                        .then((result) => {
                            const { vertices } = result;
                            const edges = [];
                            dispatch(api.update({ vertices, edges, workspaceId: workspaceId }))
                        })
                }
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

