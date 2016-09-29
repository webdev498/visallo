define(['updeep'], function(updeep) {
    'use strict';

    return function configuration(state, { type, payload }) {
        if (!state) return { properties: {}, messages: {} }

            //const { workspaceId } = payload;
            switch (type) {
                case 'CONFIGURATION_UPDATE': return update(state, payload);
                //case 'ELEMENT_DRAG': return dragging(state, payload)
                //case 'ELEMENT_DRAGEND': return dragging(state, payload)
            }
        //}

        return state;
    }

    function update(state, payload) {
        return updeep(payload, state)
    }
});

