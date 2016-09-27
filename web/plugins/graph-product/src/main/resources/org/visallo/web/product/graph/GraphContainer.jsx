define([
    'react',
    'react-redux',
    'react-dom',
    'data/web-worker/store/selection/actions',
    'data/web-worker/store/product/actions',
    'components/DroppableHOC',
    './Graph'
], function(React, redux, ReactDom, selectionActions, productActions, DroppableHOC, Graph) {
    'use strict';

    const mimeTypes = [VISALLO_MIMETYPES.ELEMENTS];
    //this.refs.graph.renderedPositionToPosition(position)

    const GraphContainer = redux.connect(

        (state, props) => {
            var viewport = state.product.viewports[props.product.id] || { zoom: 1, pan: {x: 0, y: 0 }},
                selection = state.selection.idsByType,
                workspaceId = state.workspace.currentId,
                pixelRatio = state.screen.pixelRatio,
                elements = state.element[workspaceId] || {};

            return {
                ...props,
                selection,
                viewport,
                pixelRatio,
                elements,
                mimeTypes,
                style: { height: '100%' }
            }
        },

        function(dispatch, props) {
            return {
                onAddSelection: (selection) => dispatch(selectionActions.add(selection)),
                onRemoveSelection: (selection) => dispatch(selectionActions.remove(selection)),
                onClearSelection: () => dispatch(selectionActions.clear),

                // TODO: these should be graphActions
                onUpdatePositions: (id, positions) => dispatch(productActions.updatePositions(id, positions)),
                onUpdateViewport: (id, { pan, zoom }) => dispatch(productActions.updateViewport(id, { pan, zoom })),
                onDrop: (event, position) => {
                    const dataStr = event.dataTransfer.getData(VISALLO_MIMETYPES.ELEMENTS);
                    if (dataStr) {
                        event.preventDefault();
                        event.stopPropagation();

                        const data = JSON.parse(dataStr);
                        dispatch(productActions.dropElements(props.product.id, data.elements, position))
                    }
                }
            }
        },
        null,
        { withRef: true }
    )(DroppableHOC(Graph));

    return GraphContainer;
});
