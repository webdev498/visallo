define(['updeep'], function(updeep) {
    'use strict';

    return function product(state, { type, payload }) {
        if (!state) return { loading: false, selected: null, types: [], items: [], viewports: {}, error: null };

        switch (type) {
            case 'product_getProduct_dataRequestSuccess': return { ...state, items: updateOrAddItem(state.items, payload.result) }


            case 'PRODUCT_LIST': return updateList(state, payload)
            case 'PRODUCT_UPDATE_TYPES': return { ...state, types: payload.types }

            case 'PRODUCT_SELECT': return { ...state, selected: payload.productId }
            case 'PRODUCT_UPDATE': return { ...state, items: updateOrAddItem(state.items, payload.product) }
            case 'PRODUCT_REMOVE': return { ...state, items: removeItem(state.items, payload.productId) }

            case 'PRODUCT_UPDATE_VIEWPORT': return { ...state, viewports: updateItemViewport(state.viewports, payload) }
        }

        return state;
    }

    function updateList(state, { loading, items }) {
        const sortedItems = sort(items);
        return updeep({ items: sortedItems, loading }, state)
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

    function updateItemViewport(viewports, { productId, ...viewport }) {
        var existingViewport = viewports[productId];

        if (existingViewport) {
            const eq = (a, b) => Math.abs(b - a) <= 0.001;
            const { x, y } = existingViewport
            if (eq(x, viewport.x) && eq(y, viewport.y)) {
                return viewports
            }
        }

        return { ...viewports, [productId]: viewport };
    }
});
