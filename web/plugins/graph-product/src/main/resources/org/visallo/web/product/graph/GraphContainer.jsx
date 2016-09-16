define([
    'react',
    'react-redux',
    'data/web-worker/store/selection/actions',
    './Graph'
], function(React, redux, selectionActions, Graph) {
    'use strict';

    return redux.connect(

        (state, props) => {
            return { ...props, selectionIds: state.selection.ids }
        },

        (dispatch) => {
            return {
                onAddSelection: (id) => dispatch(selectionActions.add(id)),
                onRemoveSelection: (id) => dispatch(selectionActions.remove(id))
            }
        }

    )(Graph);
});
