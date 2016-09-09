define(['../util/ajax'], function(ajax) {
    'use strict';
    return {
        list: function() {
            return ajax('GET', '/product/all')
        }
    }
})
