define([], function() {
    'use strict';

    return function product(state, { type, payload }) {
        if (!state) return { loading: false, items: [], error: null };

        switch (type) {
            case 'product_list_dataRequestLoading': return { ...state, loading: true }
            case 'product_list_dataRequestSuccess': return { ...state, loading: false, items: sort(payload.result.products) }
            case 'product_list_dataRequestFailure': return { ...state, loading: false, items: [], error: payload.error }

            case 'product_getProduct_dataRequestSuccess': return { ...state, items: updateOrAddItem(state.items, payload.result) }


            case 'PRODUCT_SELECT': return { ...state, selected: payload.productId }
            case 'PRODUCT_UPDATE': return { ...state, items: updateOrAddItem(state.items, payload.product) }
            case 'PRODUCT_REMOVE': return { ...state, items: removeItem(state.items, payload.productId) }
        }
        return state;
    }

    function sort(products) {
        return _.sortBy(products, 'title')
    }

    function hydrateExtendedData(item) {
        if (item.extendedData && _.isString(item.extendedData)) {
            return { ...item, extendedData: JSON.parse(item.extendedData) };
        }
        return item;
    }

    function updateOrAddItem(list, item) {
        item = hydrateExtendedData(item);
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
