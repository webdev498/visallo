define([], function() {
    'use strict';

    return function selection(state, { type, payload }) {
        if (!state) return { ids: [] };

        switch (type) {
            case 'SELECTION_ADD': return { ...state, ids: add(state.ids, payload) };
            case 'SELECTION_REMOVE': return { ...state, ids: remove(state.ids, payload) };

            case 'SELECTION_CLEAR': return { ...state, ids: clear(state.ids) }
        }

        return state
    }


    function add(list, { ids }) {
        if (_.any(ids, id => !list.includes(id))) {
            return _.unique(list.concat(ids)).sort()
        }
        return list;
    }

    function remove(list, { ids }) {
        if (_.any(ids, id => list.includes(id))) {
            return _.without(list, ...ids)
        }
        return list;
    }

    function clear(list) {
        return list.length === 0 ? list : [];
    }

})


