define([
    'react',
    'react-redux',
    'react-dom',
    'data/web-worker/store/selection/actions',
    'data/web-worker/store/product/actions',
    './Map'
], function(React, redux, ReactDom, selectionActions, productActions, Map) {
    'use strict';

    const Events = 'dragover dragenter dragleave drop'.split(' ')
    const MapContainer = React.createClass({
        componentDidMount() {
            this._map = ReactDom.findDOMNode(this.refs.map);
            Events.forEach(event => {
                if (event in this) {
                    this._map.addEventListener(event, this[event], false)
                } else console.error('No handler for event: ' + event);
            })
        },
        componentWillUnmount() {
            Events.forEach(event => {
                if (event in this) {
                    this._map.removeEventListener(event, this[event])
                } else console.error('No handler for event: ' + event);
            })
            this._map = null;
        },
        dragover(event) {
            const { dataTransfer } = event;
            if (VISALLO_MIMETYPES._DataTransferHasVisallo(dataTransfer, VISALLO_MIMETYPES.ELEMENTS)) {
                event.preventDefault();
            }
        },
        dragenter(event) {
            if (VISALLO_MIMETYPES._DataTransferHasVisallo(event.dataTransfer, VISALLO_MIMETYPES.ELEMENTS)) {
                this.toggleClass(true);
            }
        },
        dragleave(event) {
            this.toggleClass(false);
        },
        drop(event) {
            this.toggleClass(false);

            const { dataTransfer, clientX, clientY } = event
            const dataStr = dataTransfer.getData(VISALLO_MIMETYPES.ELEMENTS);
            if (dataStr) {
                event.preventDefault();
                event.stopPropagation();

                const data = JSON.parse(dataStr);
                const map = { vertex: 'vertexIds', edge: 'edgeIds' };
                const mapBox = this._map.getBoundingClientRect();
                const position = {
                    x: clientX - mapBox.left,
                    y: clientY - mapBox.top
                }
                this.props.onDrop(this.props.product.id, data.elements);
            }
        },
        toggleClass(toggle) {
            if (this._map) {
                // Manually change class as to not trigger render
                this._map.classList.toggle('accepts-draggable', Boolean(toggle));
            }
        },
        render() {
            const { onDrop, ...props } = this.props
            return (<Map ref="map" {...props} />)
        }
    });

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
                elements
            }
        },

        (dispatch) => {
            return {
                onAddSelection: (selection) => dispatch(selectionActions.add(selection)),
                onRemoveSelection: (selection) => dispatch(selectionActions.remove(selection)),
                onClearSelection: () => dispatch(selectionActions.clear),

                // TODO: these should be mapActions
                onUpdateViewport: (id, { pan, zoom }) => dispatch(productActions.updateViewport(id, { pan, zoom })),
                onDrop: (productId, elements) => dispatch(productActions.dropElements(productId, elements))
            }
        }

    )(MapContainer);
});
