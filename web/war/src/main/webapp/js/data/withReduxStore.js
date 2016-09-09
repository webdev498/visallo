define([
    'redux',
    'fast-json-patch'
], function(
    redux,
    jsonpatch) {
    'use strict';

    return withReduxStore;

    function withReduxStore() {

        this.before('initialize', function() {
            visalloData.storePromise = new Promise(done => { this.storeReady = done });
        })

        this.reduxStoreInit = function(message) {
            var initialState = message.state,
                store = redux.createStore(
                    rootReducer(initialState),
                    redux.compose(
                        redux.applyMiddleware(webworkerMiddleware(this.worker)),
                        window.devToolsExtension && window.devToolsExtension()
                    )
                );

            this._reduxStore = store;
            this.storeReady(store);
        };

        this.reduxStoreAction = function(message) {
            // TODO: check flux standard action
            this._reduxStore.dispatch(message.action);
        };
    }

    function rootReducer(initialState) {
        return (state, action) => {
            if (!state) {
                return initialState
            }

            var { type, payload } = action;

            switch (type) {
                case 'STATE_APPLY_DIFF': return applyDiff(state, payload);
            }

            console.warn('Unknown action type: ' + type, 'action:', action)
            return state;
        }
    }

    function webworkerMiddleware(webWorker) {
        return () => (next) => (action) => {
            if (action.meta && action.meta.originator && action.meta.originator === 'webworker') {
                return next(action);
            }

            webWorker.postMessage({
                type: 'reduxStoreActions',
                data: {
                    action: action
                }
            });
        };
    }

    /*
     * Apply diff in a redux safe manner by not mutating existing objects.
     */
    function applyDiff(state, diff) {
        // Need to copy all changed paths, since jsonpatch mutates
        var copied = copyChangedPaths(state, diff);

        jsonpatch.apply(copied, diff)

        return copied;

        function copyChangedPaths(tree, patches) {
            var alreadyCopiedObjs = [];

            if (patches.length) {
                tree = copyIfNeeded(tree);
            }
            patches.forEach(function(patch) {
                var obj = tree,
                    keys = (patch.path || '').split('/');
                for (var i = 1; i < keys.length; i++) {
                    var key = keys[i];
                    if (key in obj) {
                        obj[key] = copyIfNeeded(obj[key]);
                        obj = obj[key]
                    }
                }
            });

            return tree;

            function copyIfNeeded(obj) {
                var cloned = obj;
                if (_.isArray(obj)) {
                    if (!alreadyCopied(obj)) {
                        cloned = obj.concat([]);
                        alreadyCopiedObjs.push(cloned);
                    }
                } else if (_.isObject(obj)) {
                    if (!alreadyCopied(obj)) {
                        cloned = Object.assign({}, obj);
                        alreadyCopiedObjs.push(cloned);
                    }
                }
                return cloned;
            }

            function alreadyCopied(obj) {
                return _.contains(alreadyCopiedObjs, obj);
            }
        }

    }
});

