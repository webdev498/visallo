define(['../actions', '../../util/ajax'], function(actions, ajax) {
    actions.protectFromMain();

    return {
        add: ({ ids }) => ({
            type: 'SELECTION_ADD',
            payload: { ids }
        }),

        remove: ({ ids }) => ({
            type: 'SELECTION_REMOVE',
            payload: { ids }
        }),

        clear: () => ({ type: 'SELECTION_CLEAR' })
    }
})
