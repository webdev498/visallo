define([
    'react',
    'react-redux'
], function(React, redux) {
    'use strict';

    const ProductDetailContainer = React.createClass({
        render() {
            return (
                <div style={{textAlign: 'center', fontSize: '50pt', background: 'red', top: 0, left: 0, right: 0, bottom: 0}}>{this.props.product.id}</div>
            )
        }
    });

    return redux.connect(

        (state, props) => ({ product: _.findWhere(state.product.items, { id: state.product.selected })}),

        (dispatch) => {
            return { }
        }

    )(ProductDetailContainer);
});
