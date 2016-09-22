define(['util/undoManager'], function(UndoManager) {
    'use strict';

    return withWorkspaces;

    function withWorkspaces() {
        var workspace,
            undoManagersPerWorkspace = {};

        this.after('initialize', function() {
            var self = this;

            this.fireApplicationReadyOnce = _.once(this.trigger.bind(this, 'applicationReady'));

            this.on('loadCurrentWorkspace', this.onLoadCurrentWorkspace);
            this.on('switchWorkspace', this.onSwitchWorkspace);
            this.on('updateWorkspace', this.onUpdateWorkspace);

            visalloData.storePromise.then(store => {
                let previous = store.getState();
                const select = (s) => s.workspace.currentId;
                store.subscribe(() => {
                    const state = store.getState()
                    const previousWorkspaceId = select(previous);
                    const newWorkspaceId = select(state);
                    const newWorkspace = state.workspace.byId[newWorkspaceId];
                    const idChanged = !previousWorkspaceId || previousWorkspaceId !== newWorkspaceId;

                    if (idChanged && newWorkspace) {
                        workspace = {...newWorkspace};
                        this.setPublicApi('currentWorkspaceId', workspace.workspaceId);
                        this.setPublicApi('currentWorkspaceName', workspace.title);
                        this.setPublicApi('currentWorkspaceEditable', workspace.editable);
                        this.setPublicApi('currentWorkspaceCommentable', workspace.commentable);
                        this.trigger('workspaceLoaded', workspace);
                        this.trigger('selectObjects');
                        this.fireApplicationReadyOnce();
                        if ($('.dashboard-pane.visible').length === 0) {
                            this.trigger('menubarToggleDisplay', { name: 'dashboard' });
                        }
                        previous = state;
                    } else if (!idChanged) {
                        _.each(state.workspace.byId, (workspace, id) => {
                            const workspaceChanged = previous.workspace.byId[id] !== workspace;
                            if (workspaceChanged) {
                                this.trigger('workspaceUpdated', { workspace })
                            }
                        });
                    }
                })
            })
        });

        this.onLoadCurrentWorkspace = function(event) {
            var currentWorkspaceId = this.visalloData.currentWorkspaceId;
            this.trigger('switchWorkspace', { workspaceId: currentWorkspaceId });
        };

        this.onSwitchWorkspace = function(event, data) {
            this.setPublicApi('currentWorkspaceId', data.workspaceId);
            Promise.all([
                visalloData.storePromise,
                Promise.require('data/web-worker/store/workspace/actions')
            ]).spread(function(store, workspaceActions) {
                store.dispatch(workspaceActions.setCurrent(data.workspaceId))
            });
        };

        this.onUpdateWorkspace = function(event, data) {
            var self = this,
                triggered = false,
                buffer = _.delay(function() {
                    triggered = true;
                    self.trigger('workspaceSaving', workspace);
                }, 250),
                result;

            this.dataRequestPromise.then(function(dataRequest) {
                dataRequest('workspace', 'save', data)
                    .then(function(data) {
                        clearTimeout(buffer);
                        if (data.saved) {
                            triggered = true;
                        }
                    })
                    .catch(function(e) {
                        console.error(e);
                    })
                    .then(function() {
                        if (triggered) {
                            self.trigger('workspaceSaved', result);
                        }
                    })
            });
        };
    }
});
