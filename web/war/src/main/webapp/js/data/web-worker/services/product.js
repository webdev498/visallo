define(['../util/ajax', '../store', '../store/product/actions'], function(ajax, store, actions) {
    'use strict';
    return {
        list: function() {
            return ajax('GET', '/product/all')
        },
        createGraph: function() {
            return ajax('POST', '/product', {
                title: 'My Created Graph: ' + new Date(),
                kind: 'org.visallo.web.product.GraphWorkProduct'
            }).then(function() {
                store.getStore().dispatch(actions.list)
            })
        }
    }
})
