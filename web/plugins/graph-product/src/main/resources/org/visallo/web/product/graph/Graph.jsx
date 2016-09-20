define([
    'react',
    './Cytoscape'
], function(React, Cytoscape) {
    'use strict';

    const PropTypes = React.PropTypes;
    const Graph = React.createClass({

        getInitialState() {
            return { viewport: this.props.viewport || {} }
        },

        saveViewport(props) {
            var productId = this.props.product.id;
            if (this.currentViewport && productId in this.currentViewport) {
                var viewport = this.currentViewport[productId];
                props.onUpdateViewport(productId, viewport);
            }
        },

        componentWillReceiveProps(nextProps) {
            if (nextProps.product.id === this.props.product.id) {
                this.setState({ viewport: {} })
            } else {
                this.saveViewport(nextProps)
                this.setState({ viewport: nextProps.viewport || {} })
            }
        },

        componentWillUnmount() {
            this.saveViewport(this.props)
        },

        render() {
            var { viewport } = this.state,
                config = {...CONFIGURATION, ...viewport},
                events = {
                    onSelect: this.onSelect,
                    onUnselect: this.onUnselect,
                    onGrab: this.onGrab,
                    onFree: this.onFree,
                    onPosition: this.onPosition,
                    onPan: this.onViewport,
                    onZoom: this.onViewport
                };
            return (
                <Cytoscape
                    {...events}
                    config={config}
                    elements={this.mapPropsToElements()}></Cytoscape>
            )
        },

        onSelect({ cy, cyTarget }) {
            this.props.onAddSelection({
                [cyTarget.isNode() ? 'vertices' : 'edges']: [cyTarget.id()]
            });
        },

        onUnselect({ cy, cyTarget }) {
            this.props.onRemoveSelection({
                [cyTarget.isNode() ? 'vertices' : 'edges']: [cyTarget.id()]
            });
        },

        onGrab({ cy, cyTarget }) {
            this.cyNodeIdsWithPositionChanges = {};
        },

        onFree({ cyTarget }) {
            this.props.onUpdatePositions(
                this.props.product.id,
                _.mapObject(this.cyNodeIdsWithPositionChanges, (cyNode, id) => cyNode.position())
            );
            this.cyNodeIdsWithPositionChanges = null;
        },

        onPosition({ cyTarget }) {
            if (this.cyNodeIdsWithPositionChanges) {
                var id = cyTarget.id();
                this.cyNodeIdsWithPositionChanges[id] = cyTarget;
            } else {
                throw new Error('Position change without grab: ' + cyTarget.id())
            }
        },

        onViewport({ cy }) {
            var zoom = cy.zoom(), pan = cy.pan();
            if (!this.currentViewport) this.currentViewport = {};
            this.currentViewport[this.props.product.id] = { zoom, pan: {...pan} };
        },

        mapPropsToElements() {
            var { selection } = this.props,
                verticesSelectedById = _.indexBy(selection.vertices),
                edgesSelectedById = _.indexBy(selection.edges),
                data = this.props.product.extendedData,
                { vertices, edges } = data,
                elements = {
                    nodes: vertices.map(({ id, pos }) => ({
                        data: { id },
                        position: pos,
                        selected: (id in verticesSelectedById)
                    })),
                    edges: edges.map(({ edgeId, outVertexId, inVertexId }) => ({
                        data: {
                            id: edgeId,
                            source: outVertexId,
                            target: inVertexId
                        },
                        selected: (edgeId in edgesSelectedById)
                    }))
                };

            return elements;
        }
    });

    const CONFIGURATION = {
        style: [
            {
                selector: 'node',
                style: {
                    'height': 80,
                    'width': 80,
                    'background-fit': 'cover',
                    'border-color': '#000',
                    'border-width': 3,
                    'border-opacity': 0.5,
                    'content': 'data(name)',
                    'text-valign': 'center',
                }
            },
            {
                selector: 'node:selected',
                style: {
                    'border-color': '#0088cc'
                }
            },
            {
                selector: 'edge',
                style: {
                    'width': 6,
                    'target-arrow-shape': 'triangle',
                    'line-color': '#ffaaaa',
                    'target-arrow-color': '#ffaaaa',
                    'curve-style': 'bezier'
                }
            },
            {
                selector: 'edge:selected',
                style: {
                    'line-color': '#0088cc'
                }
            }
        ]
    };

    return Graph;
});
