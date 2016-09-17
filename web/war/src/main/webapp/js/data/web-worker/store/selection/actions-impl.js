define(['../actions', '../../util/ajax'], function(actions, ajax) {
    actions.protectFromMain();

    return {
        add: ({ selection }) => ({
            type: 'SELECTION_ADD',
            payload: { selection }
        }),

        remove: ({ selection }) => ({
            type: 'SELECTION_REMOVE',
            payload: { selection }
        }),

        clear: () => ({ type: 'SELECTION_CLEAR' })
    }
})
