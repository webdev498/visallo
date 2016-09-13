define([
    'react',
    'react-redux',
    'configuration/plugins/registry',
    './ProductDetail'
], function(React, redux, registry, ProductDetail) {
    'use strict';

    return redux.connect(
        (state, props) => {
            var product = _.findWhere(state.product.items, { id: state.product.selected }),
                extensions = _.where(registry.extensionsForPoint('org.visallo.workproduct'), { identifier: product.kind })

            console.log(registry.extensionsForPoint('org.visallo.workproduct'))

            if (extensions.length === 0) throw Error('No org.visallo.workproduct extensions registered for: ' + product.kind)
            if (extensions.length !== 1) throw Error('Multiple org.visallo.workproduct extensions registered for: ' + product.kind)

            return { product, extension: extensions[0] }
        },
        (dispatch) => {
            return { }
        }
    )(ProductDetail);
});
