define([], function() {
    'use strict';

    return function dashboard(state, { type, payload }) {
        if (!state) return { x: 0 };

        switch (type) {
            case 'INC': return { ...state, x: state.x + 1 }
        }
        return state;
    }
});
