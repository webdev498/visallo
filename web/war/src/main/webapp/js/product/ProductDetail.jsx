define([
    'react'
], function(React) {
    'use strict';

    const PropTypes = React.PropTypes;
    const ProductDetail = React.createClass({
        propTypes: {
            product: PropTypes.shape({
                id: PropTypes.string.isRequired
            }).isRequired,
            extension: PropTypes.shape({
                componentPath: PropTypes.string.isRequired
            }).isRequired
        },
        getInitialState: () => ({ Component: null }),
        requestComponent(props) {
            props.onGetProduct(props.product.id)
            if (props.extension.componentPath !== this.props.extension.componentPath || !this.state.Component) {
                this.setState({ Component: null })
                Promise.require(props.extension.componentPath).then((C) => this.setState({ Component: C }))
            }
        },
        componentDidMount() {
            this.requestComponent(this.props);
        },
        componentWillReceiveProps(nextProps) {
            this.requestComponent(nextProps);
        },
        render() {
            var { Component } = this.state;
            var { extendedData } = this.props.product;

            return (
                Component && extendedData ?
                    (<Component product={this.props.product}></Component>) :
                    (
                        <div style={{textAlign: 'right', lineHeight: '50pt', fontSize: '50pt', background: 'red', top: 0, left: 0, right: 0, bottom: 0}}>Loading...</div>
                    )
            )
        }
    });

    return ProductDetail;
});
