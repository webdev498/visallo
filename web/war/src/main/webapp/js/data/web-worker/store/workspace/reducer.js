define([], function() {
    'use strict';

    return function workspace(state, { type, payload }) {
        if (!state) return { currentId: null, list: [] };

        switch (type) {
            case 'setCurrentWorkspace': return { ...state, currentId: payload.workspaceId }
        }

        return state
    }
})


