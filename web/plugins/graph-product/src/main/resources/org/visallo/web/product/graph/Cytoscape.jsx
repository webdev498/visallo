define([
    'react',
    'cytoscape',
    'fast-json-patch'
], function(
    React,
    cytoscape,
    jsonpatch) {

    const events = {
        drag: 'onDrag',
        tap: 'onTap',
        pan: 'onPan',
        change: 'onChange',
        select: 'onSelect',
        unselect: 'onUnselect'
    };

    const Cytoscape = React.createClass({
        getDefaultProps() {
            const eventProps = _.mapObject(_.invert(events), () => () => {})
            return {
                ...eventProps,
                onReady() {},
                config: {},
                elements: { nodes: [], edges: [] },
            }
        },
        componentDidMount() {
            this.setState({ cy: this.createCy() })
        },
        componentWillUnmount() {
            if (this.state.cy) {
                this.state.cy.destroy();
            }
        },
        componentDidUpdate() {
            const { cy } = this.state;
            const newData = {elements: this.props.elements}
            const oldData = cy.json()

            const getAllData = nodes => nodes.map(({data, selected, position}) => ({
                data,
                selected,
                position
            }))

            const getTypeData = elementType => [oldData, newData].map(n => getAllData(n.elements[elementType] || []) )
            const [oldNodes, newNodes] = getTypeData('nodes')
            const [oldEdges, newEdges] = getTypeData('edges')

            cy.batch(() => {
                this.makeChanges(oldNodes, newNodes)
                this.makeChanges(oldEdges, newEdges)
            })
        },
        prepareConfig() {
            const defaults = {
                container: this.refs.cytoscape,
                boxSelectionEnabled: true,
                ready: (event) => {
                    var { cy } = event,
                        eventMap = _.mapObject(events, (name, key) => (e) => {
                            if (this[key + 'Disabled'] !== true) {
                                console.log(key, name)
                                this.props[name](e)
                            }
                        });
                    cy.on(eventMap)
                    this.props.onReady(event)
                }
            }
            var { config } = this.props;
            if (config) {
                return { ...defaults, ...config }
            }
            return defaults;
        },
        createCy() {
            const config = this.prepareConfig()
            return cytoscape(config)
        },
        render() {
            return (
                <div style={{height: '100%'}} ref="cytoscape"></div>
            )
        },

        disableEvent(name, fn) {
            var names = name.split(/\s+/);
            names.forEach(name => (this[name + 'Disabled'] = true))
            fn.apply(this)
            names.forEach(name => (this[name + 'Disabled'] = false))
        },

        /*
        cytoChange(older, newer, change) {
            const cy = cytoscapeGraph;
            const { op, path, value } = change;
            const pathParts = _.compact(path.replace(/^\//, '').split('/'))
            const actions = {
                replace: () => {
                    const index = parseInt(pathParts[0])
                    const element = newer[index];
                    const el = cy.getElementById(element.data.id);
                    if (pathParts[1] === 'data') {
                        if (pathParts[2] === 'id') {
                            cy.remove(cy.getElementById(older[index].data.id))
                        } else {
                            el.removeData()
                            el.data(element.data)
                        }
                    } else if (pathParts[1] === 'selected') {
                        if (el.selected() !== element.selected) {
                            this.disableEvent('select unselect', () => el[element.selected ? 'select' : 'unselect']());
                        }
                    } else if (pathParts[1] === 'position') {
                        el.position(element.position);
                    }
                },
                remove: () => {
                    const index = parseInt(pathParts[0])
                    const element = older[index];
                    const el = cy.getElementById(element.data.id);
                    if (el.length && !el.removed()) {
                        cy.remove(el)
                    }
                },
                add: () => {
                    const { data, selected, position, id } = value
                    if (isNode(data)) {
                        cy.add({
                            group:'nodes',
                            data,
                            selected,
                            position
                        })
                    } else if (isEdge(data)) {
                        cy.add({ group:'edges', data, selected })
                    }
                }
            }[op]();
        },
        */

        makeChanges(older, newer) {
            const cy = this.state.cy
            const add = [];
            const remove = [...older];
            const modify = [];
            const oldById = _.indexBy(older, o => o.data.id);

            newer.forEach(item => {
                var id = item.data.id;
                var existing = oldById[id];
                if (existing) {
                    modify.push({ item, diffs: jsonpatch.compare(existing, item) })
                    var index = _.findIndex(remove, i => i.data.id === id);
                    if (index >= 0) remove.splice(index, 1)
                } else {
                    add.push(item)
                }
            })

            console.log('add', add, 'remove', remove, 'modify', modify)

            modify.forEach(({ item, diffs }) => {
                const topLevelChanges = _.indexBy(diffs, d => d.path.replace(/^\/([^\/]+).*$/, '$1'))
                Object.keys(topLevelChanges).forEach(change => {
                    const cyNode = cy.getElementById(item.data.id);

                    switch (change) {
                        case 'data':
                            cyNode.removeData()
                            cyNode.data(item.data)
                            break;
                        case 'selected':
                            if (cyNode.selected() !== item.selected) {
                                this.disableEvent('select unselect', () => cyNode[item.selected ? 'select' : 'unselect']());
                            }
                            break;
                        case 'position':
                            cyNode.position(item.position)
                            break;
                        default:
                            throw new Error('Change not handled: ' + change)
                    }
                })
            })
            add.forEach(item => {
                var { data } = item;

                if (isNode(data)) {
                    cy.add({ ...item, group: 'nodes' })
                } else if (isEdge(data)) {
                    cy.add({ ...item, group:'edges' })
                }
            })
            remove.forEach(item => {
                const cyNode = cy.getElementById(item.data.id);
                if (cyNode.length && !cyNode.removed()) {
                    cy.remove(cyNode)
                }
            })


            /*
            if (diff) {
                const byOperation = _.groupBy(diff, 'op');
                const process = this.cytoChange.bind(this, older, newer)

                cytoscapeGraph.batch(() => {
                    if (byOperation.replace) {
                        byOperation.replace.forEach(process)
                    }
                    if (byOperation.add) {
                        byOperation.add.forEach(process)
                    }
                    if (byOperation.remove) {
                        byOperation.remove.forEach(process)
                    }
                })
            }
            */
        }
    })

    const isEdge = data => (data.source !== undefined)
    const isNode = _.negate(isEdge)

    return Cytoscape;
})

