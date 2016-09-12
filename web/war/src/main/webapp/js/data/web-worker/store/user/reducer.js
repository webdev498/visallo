define([], function() {
    'use strict';

    return function user(state, { type, payload }) {
        if (!state) return { current: null };

        switch (type) {
            case 'setCurrentUser': return { ...state, current: payload.user }
        }

        return state
    }
})

