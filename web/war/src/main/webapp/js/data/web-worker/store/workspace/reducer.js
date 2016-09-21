define([], function() {
    'use strict';

    return function workspace(state, { type, payload }) {
        if (!state) return { currentId: null, byId: {} };

        switch (type) {
            case 'WORKSPACE_SETCURRENT': return { ...state, currentId: payload.workspaceId };
            case 'WORKSPACE_UPDATE': return { ...state, byId: update(state.byId, payload.workspace) };
        }

        return state
    }


    function update(previousById, workspace) {
        if (!workspace) throw new Error('Workspace must not be undefined')
        var id = workspace.workspaceId;
        if (id in previousById && _.isEqual(workspace, previousById[id])) {
            return previousById
        }
        return { ...previousById, [workspace.workspaceId]: workspace }
    }
})


