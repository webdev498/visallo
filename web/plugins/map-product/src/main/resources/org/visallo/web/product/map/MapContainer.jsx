define([
    'react',
    'react-redux',
    'react-dom',
    'data/web-worker/store/selection/actions',
    'data/web-worker/store/product/actions',
    'components/DroppableHOC',
    './Map'
], function(React, redux, ReactDom, selectionActions, productActions, DroppableHOC, Map) {
    'use strict';

    const mimeTypes = [VISALLO_MIMETYPES.ELEMENTS];

    return redux.connect(

        (state, props) => {
            var //viewport = state.product.viewports[props.product.id] || { zoom: 1, pan: {x: 0, y: 0 }},
                selection = state.selection.idsByType,
                workspaceId = state.workspace.currentId,
                //pixelRatio = state.screen.pixelRatio,
                elements = state.element[workspaceId] || {};

            return {
                ...props,
                selection,
                //viewport,
                //pixelRatio,
                elements,
                mimeTypes
            }
        },

        (dispatch, props) => {
            return {
                onAddSelection: (selection) => dispatch(selectionActions.add(selection)),
                onRemoveSelection: (selection) => dispatch(selectionActions.remove(selection)),
                onClearSelection: () => dispatch(selectionActions.clear),

                // TODO: these should be mapActions
                onUpdateViewport: (id, { pan, zoom }) => dispatch(productActions.updateViewport(id, { pan, zoom })),

                // For DroppableHOC
                onDrop: (event) => {
                    const dataStr = event.dataTransfer.getData(VISALLO_MIMETYPES.ELEMENTS);
                    if (dataStr) {
                        event.preventDefault();
                        event.stopPropagation();

                        const data = JSON.parse(dataStr);
                        dispatch(productActions.dropElements(props.product.id, data.elements))
                    }
                }
            }
        }

    )(DroppableHOC(Map));
});
