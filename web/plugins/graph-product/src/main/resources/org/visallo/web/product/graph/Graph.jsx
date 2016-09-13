define([
    'react'
], function(React) {
    'use strict';

    const PropTypes = React.PropTypes;
    const Graph = React.createClass({
        componentWillMount() {
            console.log('mount', this.props.product.id)
        },
        componentWillReceiveProps() {
            console.log('props', this.props.product.id)
        },
        componentWillUnmount() {
            console.log('unmount', this.props.product.id)
        },
        render() {
            return (
                <div style={{textAlign: 'right'}}>
                    GRAPH
                    <p>{this.props.product.id}</p>
                </div>
            )
        }
    });

    return Graph;
});
