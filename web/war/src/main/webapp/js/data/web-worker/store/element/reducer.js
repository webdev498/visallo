define([], function() {
    'use strict';

    return function element(state, { type, payload }) {
        if (!state) return { vertices: {}, edges: {} }

        switch (type) {
            case 'ELEMENT_UPDATE': return { ...state, ...(update(state, payload)) };
        }

        return state;
    }

    function update(previous, { vertices, edges }) {
        let prevVertices = previous.vertices;
        let prevEdges = previous.edges;

        if (vertices.length) {
            let newVertices = _.indexBy(vertices, 'id');
            prevVertices = { ...prevVertices, ...newVertices };
        }
        if (edges.length) {
            let newEdges = _.indexBy(edges, 'id');
            prevEdges = { ...prevEdges, ...newEdges };
        }

        if (vertices.length || edges.length) {
            return { vertices: prevVertices, edges: prevEdges };
        }

        return previous;
    }
});

