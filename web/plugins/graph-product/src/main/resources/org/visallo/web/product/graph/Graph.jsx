define([
    'react',
    './Cytoscape'
], function(React, Cytoscape) {
    'use strict';

    const PropTypes = React.PropTypes;
    const Graph = React.createClass({
        render() {
            var selectedById = _.indexBy(this.props.selectionIds),
                data = this.props.product.extendedData,
                elements = {
                    nodes: data.vertices.map(({ id, pos }) => ({
                        data: { id },
                        position: { x: pos[0], y: pos[1] },
                        selected: (id in selectedById)
                    })),
                    edges: data.edges.map(({ edgeId, outVertexId, inVertexId }) => ({
                        data: {
                            id: edgeId,
                            source: outVertexId,
                            target: inVertexId
                        },
                        selected: (edgeId in selectedById)
                    }))
                },
                config = {
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
                        }
                    ]
                };
            return (
                <Cytoscape
                    onSelect={this.onSelect}
                    onUnselect={this.onUnselect}
                    config={config}
                    elements={elements}></Cytoscape>
            )
        },
        onSelect({ cy, cyTarget }) {
            this.props.onAddSelection(cyTarget.id())
        },
        onUnselect({ cy, cyTarget }) {
            this.props.onRemoveSelection(cyTarget.id())
        }
    });

    return Graph;

    /*
    layout: {
        name: 'breadthfirst',
        directed: true,
        padding: 10
    }
    */
});
