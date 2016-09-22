
define([], function() {
    'use strict';

    return withWorkspaceVertexDrop;

    function withWorkspaceVertexDrop() {

        this.after('initialize', function() {
            var self = this;
            this.on('applicationReady currentUserVisalloDataUpdated', _.once(function() {
                var enabled = false,
                    droppable = $(document.body);

                // Prevent dragging any context menu items to graph/map
                $(document.body).on('dragstart', '.dropdown-menu', function(e) {
                    e.preventDefault();
                });

                // Other droppables might be on top of graph, listen to
                // their over/out events and ignore drops if the user hasn't
                // dragged outside of them. Can't use greedy option since they are
                // absolutely positioned
                $(document.body).on('dropover dropout', function(e, ui) {
                    var target = $(e.target),
                        appDroppable = target.is(droppable),
                        parentDroppables = target.parents('.ui-droppable');

                    if (appDroppable) {
                        enabled = true;
                        return;
                    }

                    // If this droppable has no parent droppables
                    if (parentDroppables.length === 1 && parentDroppables.is(droppable)) {
                        enabled = e.type === 'dropout';
                    }
                });

                require(['jquery-ui'], function() {
                    droppable.droppable({
                        tolerance: 'pointer',
                        accept: function(item) {
                            return true;
                        },
                        over: function(event, ui) {
                            var draggable = ui.draggable,
                                start = true,
                                ids,
                                started = false,
                                wrapper = $('.draggable-wrapper');

                            // Prevent map from swallowing mousemove events by adding
                            // this transparent full screen div
                            if (wrapper.length === 0) {
                                wrapper = $('<div class="draggable-wrapper"/>').appendTo(document.body);
                            }

                            draggable.off('drag.droppable-tracking');
                            draggable.on('drag.droppable-tracking', function handler(event, draggableUI) {
                                if (!ids) {
                                    ids = elementIdsFromDraggable(ui.draggable)
                                }

                                /*
                                if (graphVisible) {
                                    ui.helper.toggleClass('draggable-invisible', enabled);
                                } else if (dashboardVisible) {
                                    $(event.target).closest('.dialog-popover').data('preventTeardown', true);

                                    var count = 0;
                                    self.on(document, 'didToggleDisplay', function didToggle(event, data) {
                                        count++;
                                        if (count >= 2) {
                                            self.off(document, 'didToggleDisplay', didToggle);
                                            dashboardVisible = false;
                                            graphVisible = true;
                                            handler(event, draggableUI);
                                        }
                                    })
                                    self.trigger('menubarToggleDisplay', { name: 'graph' });
                                    return;
                                }
                                */

                                self.trigger('toggleWorkspaceFilter', { enabled: !enabled });
                                if (enabled) {
                                    //const position = { x: event.pageX, y: event.pageY }
                                    //visalloData.storePromise.then(store => {
                                        //store.dispatch({
                                            //type: 'ELEMENT_DRAG',
                                            //payload: {
                                                //workspaceId: store.getState().workspace.currentId,
                                                //position,
                                                //...ids
                                            //}
                                        //})
                                    //})
                                    //self.trigger('verticesHovering', {
                                        //vertices: vertices,
                                        //start: start,
                                        //position: { x: event.pageX, y: event.pageY }
                                    //});
                                    start = false;
                                } else {
                                    //self.trigger('verticesHoveringEnded', { vertices: vertices });
                                }
                            });
                        },
                        drop: function(event, ui) {
                            $('.draggable-wrapper').remove();

                            // Early exit if should leave to a different droppable
                            if (!enabled) return;

                            const position = { x: event.pageX, y: event.pageY }
                            const ids = elementIdsFromDraggable(ui.draggable)
                            visalloData.storePromise.then(store => {
                                require(['data/web-worker/store/element/actions'], function({ dragEnd }) {
                                    store.dispatch(dragEnd({
                                        workspaceId: store.getState().workspace.currentId,
                                        position, ids
                                    }))
                                })
                            });
                            //const ids = elementIdsFromDraggable(ui.draggable)
                            //if (visalloData.currentWorkspaceEditable && ids.length) {
                                //self.trigger('clearWorkspaceFilter');
                                //self.trigger('verticesDropped', {
                                    //vertices: vertices,
                                    //dropPosition: { x: event.clientX, y: event.clientY }
                                //});
                            //}
                        }
                    });
                });
            }));

            function elementIdsFromDraggable(draggable) {
                var draggableData = draggable.data('ui-draggable');
                if (!draggableData) return Promise.resolve([]);

                var alsoDragging = draggableData.alsoDragging,
                    anchors = draggable;

                if (alsoDragging && alsoDragging.length) {
                    anchors = draggable.add(alsoDragging.map(function(i, a) {
                        return a.data('original');
                    }));
                }

                var ids = { vertexIds: [], edgeIds: [] };
                anchors.each(function(i, a) {
                    a = $(a);
                    var vertexId = a.data('vertexId');
                    var edgeId = a.data('edgeId');
                    if (!vertexId && !edgeId) {
                        vertexId = a.closest('li').data('vertexId');
                        if (!vertexId) edgeId = a.closest('li').data('vertexId');
                    }
                    if (a.is('.facebox')) return;

                    if (!vertexId && !edgeId) {

                        // Highlighted entities (legacy info)
                        var info = a.data('info') || a.closest('li').data('info');

                        vertexId = info && (info.resolvedToVertexId || info.graphVertexId || info.id);

                        // Detected objects
                        if (info && info.entityVertex) {
                            self.updateCacheWithVertex(info.entityVertex);
                            vertexId = info.entityVertex.id;
                        }
                    }

                    if (vertexId) ids.vertexIds.push(vertexId);
                    if (edgeId) ids.edgeIds.push(edgeId);
                });

                return ids;
            }
        })
    }
});
