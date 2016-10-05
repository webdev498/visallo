define(['../actions', '../../util/ajax',
    '../element/actions-impl',
    '../selection/actions-impl'
], function(actions, ajax, elementActions, selectionActions) {
    actions.protectFromMain();

    const api = {
        get: ({productId, invalidate}) => (dispatch, getState) => {
            var items = getState().product.items;
            var product = _.findWhere(items, { id: productId })

            if (invalidate || !product || !product.extendedData) {
                return ajax('GET', '/product', {
                    productId,
                    includeExtended: true,
                    params: {
                        includeVertices: true,
                        includeEdges: true
                    }
                })
                    .then(function(product) {
                        dispatch(api.update(product))

                        const { vertices, edges } = JSON.parse(product.extendedData)
                        const vertexIds = _.pluck(vertices, 'id');
                        const edgeIds = _.pluck(edges, 'id');
                        dispatch(elementActions.get({ vertexIds, edgeIds }));
                    })
            }
        },

        changedOnServer: (productId) => (dispatch, getState) => {
            // TODO: Should check current workspace
            // not current ? just mark it invalid
            // is current  ? is it selected ? get : load list
            dispatch(api.get({ productId, invalidate: true }));
        },

        update: (product) => ({
            type: 'PRODUCT_UPDATE',
            payload: {
                product
            }
        }),

        updatePositions: ({ productId, updateVertices }) => (dispatch, getState) => {
            var params = { updateVertices },
                product = _.findWhere(getState().product.items, { id: productId }),
                { kind } = product;

            if (!_.isEmpty(updateVertices)) {
                return ajax('POST', '/product', { productId, kind, params })
            }
        },

        updateViewport: ({ productId, pan, zoom }) => ({
            type: 'PRODUCT_UPDATE_VIEWPORT',
            payload: { productId, pan, zoom }
        }),

        dropElements: ({ productId, elements, position }) => (dispatch, getState) => {
            const { vertexIds, edgeIds } = elements;
            var edges = (edgeIds && edgeIds.length) ? (
                ajax('POST', '/edge/multiple', { edgeIds })
                    .then(function({ edges }) {
                        return _.flatten(edges.map(e => [e.inVertexId, e.outVertexId]));
                    })
                ) : Promise.resolve([]);

            edges.then(function(edgeVertexIds) {
                const combined = _.uniq(edgeVertexIds.concat(vertexIds));
                dispatch(api.updatePositions({
                    productId,
                    updateVertices: _.object(combined.map(id => [id, position || {}]))
                }))
            })
        },

        removeElements: ({ productId, elements }) => (dispatch, getState) => {
            var params = { removeVertices: elements },
                state = getState(),
                product = _.findWhere(state.product.items, { id: productId }),
                workspace = state.workspace.byId[state.workspace.currentId],
                { kind } = product;

            // TODO: combine with updatePositions
            if (workspace.editable && !_.isEmpty(params.removeVertices)) {
                ajax('POST', '/product', { productId, kind, params })
                    .then(function() {
                        dispatch(selectionActions.clear())
                    })
            }
        },

        list: () => (dispatch, getState) => {
            dispatch({type: 'PRODUCT_LIST', payload: { loading: true }})
            ajax('GET', '/product/all').then(({types, products}) => {
                dispatch({type: 'PRODUCT_UPDATE_TYPES', payload: { types }})
                dispatch({type: 'PRODUCT_LIST', payload: { loading: false, items: products }})
            })
        },

        create: ({title, kind}) => {
            return ajax('POST', '/product', { title, kind })
                .then(product => api.update(product))
        },

        delete: ({ productId }) => {
            return ajax('DELETE', '/product', { productId })
                .then(() => ({ type: 'PRODUCT_REMOVE', payload: { productId } }))
        },

        remove: (productId) => {
            return { type: 'PRODUCT_REMOVE', payload: { productId }}
        },

        select: (payload) => ({
            type: 'PRODUCT_SELECT',
            payload
        })

    }

    return api;
})
