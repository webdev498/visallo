define([], function() {
    'use strict';

    return function product(state, { type, payload }) {
        if (!state) return { loading: false, items: [], error: null };

        switch (type) {
            case 'product_list_dataRequestLoading': return { ...state, loading: true }
            case 'product_list_dataRequestSuccess': return { ...state, loading: false, items: sort(payload.result.products) }
            case 'product_list_dataRequestFailure': return { ...state, loading: false, items: [], error: payload.error }

            case 'product_getProduct_dataRequestSuccess': return { ...state, items: updateOrAddItem(state.items, payload.result) }

            case 'removeProduct': return { ...state, items: removeItem(state.items, payload.productId) }

            case 'selectProduct': return { ...state, selected: payload.productId }
        }
        return state;
    }

    function sort(products) {
        return _.sortBy(products, 'title')
    }

    function updateOrAddItem(list, item) {
        var updated = false,
            items = list.map(i => {
                if (i.id === item.id) {
                    updated = true
                    return item;
                }
                return i
            });

        if (!updated) items.push(item)
        return sort(items)
    }

    function removeItem(list, id) {
        return _.reject(list, (item) => item.id === id)
    }
});
