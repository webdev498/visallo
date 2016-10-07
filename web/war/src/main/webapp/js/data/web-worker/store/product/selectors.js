define(['reselect', '../element/selectors'], function(reselect, elementSelectors) {
    const { createSelector } = reselect;

    const getWorkspaceId = (state) => state.workspace.currentId;

    const getProducts = (state) => state.product.items;

    const getCurrentProductId = (state) => state.product.selected;

    const getSelection = (state) => state.selection.idsByType;

    const getProduct = createSelector([getProducts, getCurrentProductId], (items, productId) => {
        return _.findWhere(items, { id: productId });
    })

    const getProductsInWorkspace = createSelector([getWorkspaceId, getProducts], (workspaceId, products) => {
        return _.chain(products)
            .where({ workspaceId })
            .sortBy('id')
            .sortBy('title')
            .sortBy('kind')
            .value()
    });

    const getElementIdsInProduct = createSelector([getProduct], (product) => {
        if (!product.extendedData) return { vertices: [], edges: [] };

        const { vertices, edges } = product.extendedData

        return { vertices, edges }
    });

    const getElementsInProduct = createSelector([getElementIdsInProduct, elementSelectors.getElements], (elementIds, elements) => {
        console.log('getElementsInProduct', elementIds, elements)
        return {
            vertices: _.pick(elements.vertices, _.pluck(elementIds.vertices, 'id')),
            edges: _.pick(elements.edges, _.pluck(elementIds.edges, 'edgeId'))
        };
    })

    const getSelectedElementsInProduct = createSelector([getSelection, getElementIdsInProduct], (selection, elementIds) => {
        const { vertices, edges } = elementIds;
        return {
            vertices: _.indexBy(_.intersection(selection.vertices, _.pluck(vertices, 'id'))),
            edges: _.indexBy(_.intersection(selection.edges, _.pluck(edges, 'id')))
        };
    });

    return {
        getProducts,
        getProduct,
        getProductsInWorkspace,
        getElementsInProduct,
        getSelectedElementsInProduct
    };
})

