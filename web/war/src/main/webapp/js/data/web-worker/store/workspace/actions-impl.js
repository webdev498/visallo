define(['../actions', '../../util/ajax'], function(actions, ajax) {
    actions.protectFromMain();

    const api = {

        setCurrent: ({ workspaceId }) => (dispatch, getState) => {

            dispatch({ type: 'WORKSPACE_SETCURRENT', payload: { workspaceId } })
            dispatch(api.get({ workspaceId }))
        },

        get: ({ workspaceId, invalidate }) => (dispatch, getState) => {
            var workspace = _.findWhere(getState().workspace.list, { workspaceId });
            if (!workspace || invalidate) {
                ajax('GET', '/workspace', { workspaceId })
                    .then(workspace => dispatch(api.update({ workspace })))
            }
        },

        update: ({ workspace }) => ({
            type: 'WORKSPACE_UPDATE',
            payload: { workspace }
        })
    }

    return api;
})

