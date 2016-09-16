define([
    'react',
    'react-redux',
    'configuration/plugins/registry',
    './ProductDetail',
    'data/web-worker/store/product/actions'
], function(React, redux, registry, ProductDetail, productActions) {
    'use strict';

    const ProductDetailContainer = React.createClass({
        render() {
            var { props } = this;

            if (props.product) {
                return (
                    <ProductDetail {...props} />
                )
            }

            return null
        }
    })

    return redux.connect(

        (state, props) => {
            var product = _.findWhere(state.product.items, { id: state.product.selected });

            if (product) {
                var extensions = _.where(registry.extensionsForPoint('org.visallo.workproduct'), { identifier: product.kind });

                if (extensions.length === 0) {
                    throw Error('No org.visallo.workproduct extensions registered for: ' + product.kind)
                }
                if (extensions.length !== 1) {
                    throw Error('Multiple org.visallo.workproduct extensions registered for: ' + product.kind)
                }

                return { product, extension: extensions[0] }
            }
            return {}
        },

        (dispatch) => {
            return {
                onGetProduct: (id) => dispatch(productActions.get(id))
            }
        }

    )(ProductDetailContainer);
});
