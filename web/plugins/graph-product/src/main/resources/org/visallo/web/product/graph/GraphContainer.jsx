define([
    'react',
    'react-redux',
    'data/web-worker/store/selection/actions',
    'data/web-worker/store/product/actions',
    './Graph'
], function(React, redux, selectionActions, productActions, Graph) {
    'use strict';

    return redux.connect(

        (state, props) => {
            var viewport = state.product.viewports[props.product.id] || { zoom: 1, pan: {x: 0, y: 0 }},
                selection = state.selection.idsByType;

            return { ...props, selection, viewport }
        },

        (dispatch) => {
            return {
                onAddSelection: (selection) => dispatch(selectionActions.add(selection)),
                onRemoveSelection: (selection) => dispatch(selectionActions.remove(selection)),
                onClearSelection: () => dispatch(selectionActions.clear),

                // TODO: these should be graphActions
                onUpdatePositions: (id, positions) => dispatch(productActions.updatePositions(id, positions)),
                onUpdateViewport: (id, { pan, zoom }) => dispatch(productActions.updateViewport(id, { pan, zoom }))
            }
        }

    )(Graph);
});
