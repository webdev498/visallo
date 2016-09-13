define(['../util/ajax', '../store', '../store/product/actions'], function(ajax, store, actions) {
    'use strict';
    return {

        list() {
            return ajax('GET', '/product/all')
        },

        createGraph() {
            return ajax('POST', '/product', {
                title: 'My Created Graph: ' + new Date(),
                kind: 'org.visallo.web.product.graph.GraphWorkProduct'
            })
        },

        deleteProduct(productId) {
            return ajax('DELETE', '/product', { productId })
        },

        getProduct(productId, includeExtended) {
            return ajax('GET', '/product', { productId, includeExtended })
        }
    }
})
