define(['updeep'], function(updeep) {
    'use strict';

    return function element(state, { type, payload }) {
        if (!state) return { }

        if (payload && 'workspaceId' in payload) {
            const { workspaceId } = payload;
            switch (type) {
                case 'ELEMENT_UPDATE': return { ...state, [workspaceId]: update(state[workspaceId], payload) };
                case 'ELEMENT_DRAG': return dragging(state, payload)
                case 'ELEMENT_DRAGEND': return dragging(state, payload)
            }
        }

        return state;
    }

    function dragging(previous, payload) {
        const { workspaceId, ...dragging } = payload;
        return updeep({ [workspaceId]: { dragging } }, previous)
    }

    function update(previous, { vertices, edges }) {
        let prevVertices = previous && previous.vertices || {};
        let prevEdges = previous && previous.edges || {};

        if (vertices && vertices.length) {
            let newVertices = _.indexBy(vertices, 'id');
            prevVertices = { ...prevVertices, ...newVertices };
        }
        if (edges && edges.length) {
            let newEdges = _.indexBy(edges, 'id');
            prevEdges = { ...prevEdges, ...newEdges };
        }

        if (vertices.length || edges.length) {
            return { vertices: prevVertices, edges: prevEdges };
        }

        return previous;
    }
});

