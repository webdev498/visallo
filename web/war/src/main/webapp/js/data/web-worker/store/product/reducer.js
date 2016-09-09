define([], function() {
    'use strict';

    return function product(state, { type, payload }) {
        if (!state) return { loading: false, items: [], error: null };

        console.log(type, payload)
        switch (type) {
            case 'product_list_dataRequestLoading': return { ...state, loading: true }
            case 'product_list_dataRequestSuccess': return { ...state, loading: false, items: payload.result.products }
            case 'product_list_dataRequestFailure': return { ...state, loading: false, items: [], error: payload.error }
        }
        return state;
    }
});
