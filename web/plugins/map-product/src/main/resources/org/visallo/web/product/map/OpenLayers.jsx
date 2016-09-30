define([
    'react',
    'openlayers',
    'fast-json-patch',
    './multiPointCluster'
], function(
    React,
    ol,
    jsonpatch,
    MultiPointCluster) {

    const isEdge = data => (data.source !== undefined)
    const isNode = _.negate(isEdge)

    const MODE_NORMAL = 0,
        FEATURE_SIZE = [22, 40],
        FEATURE_HEIGHT = 40,
        FEATURE_CLUSTER_HEIGHT = 24,
        ANIMATION_DURATION = 200,
        MODE_REGION_SELECTION_MODE_POINT = 1,
        MODE_REGION_SELECTION_MODE_RADIUS = 2,
        MODE_REGION_SELECTION_MODE_LOADING = 3;

    const { PropTypes } = React;

    const OpenLayers = React.createClass({
        propTypes: {
            source: PropTypes.string.isRequired,
            sourceOptions: PropTypes.object,
            onSelectElements: PropTypes.func.isRequired
        },

        componentDidUpdate() {
            if (this.state.cluster) {
                const existingFeatures = _.indexBy(this.state.cluster.source.getFeatures(), f => f.getId());
                const newFeatures = [];
                const { source } = this.state.cluster;
                this.props.features.forEach(data => {
                    const { id, geoLocations, element, ...rest } = data;
                    const featureValues = {
                        ...rest,
                        element,
                        geoLocations,
                        geometry: new ol.geom.MultiPoint(geoLocations.map(function(geo) {
                            return ol.proj.fromLonLat(geo);
                        }))
                    };

                    if (id in existingFeatures) {
                        const existingFeature = existingFeatures[id];
                        const existingValues = _.omit(data, 'geometry', 'element')
                        const newValues = _.omit(featureValues, 'geometry', 'element')
                        if (!_.isEqual(existingValues, newValues)) {
                            existingFeature.setProperties(featureValues)
                        }
                        delete existingFeatures[id];
                    } else {
                        var feature = new ol.Feature(featureValues);
                        feature.setId(data.id);
                        newFeatures.push(feature);
                    }
                })

                if (newFeatures.length) {
                    source.addFeatures(newFeatures);
                }
                if (!_.isEmpty(existingFeatures)) {
                    _.forEach(existingFeatures, feature => source.removeFeature(feature));
                }
            }
        },

        componentDidMount() {
            this.olEvents = [];
            this.domEvents = [];
            const { map, cluster } = this.configureMap();
            this.setState({ map, cluster })
        },

        componentWillUnmount() {
            if (this.state.cluster) {
                this.olEvents.forEach(key => ol.Observable.unByKey(key));
                this.olEvents = null;

                this.domEvents.forEach(fn => fn());
                this.domEvents = null;
            }
        },

        render() {
            return (
                <div style={{height: '100%'}} ref="map"></div>
            )
        },

        configureMap() {
            const { source, sourceOptions = {} } = this.props;
            const cluster = this.configureCluster()
            const map = new ol.Map({
                loadTilesWhileInteracting: true,
                keyboardEventTarget: document,
                controls: [],
                layers: [],
                target: this.refs.map
            });

            this.configureEvents({ map, cluster });

            var baseLayerSource;

            if (source in ol.source && _.isFunction(ol.source[source])) {
                baseLayerSource = new ol.source[source](sourceOptions)
            } else {
                console.error('Unknown map provider type: ', source);
                throw new Error('map.provider is invalid')
            }

            map.addLayer(new ol.layer.Tile({ source: baseLayerSource }));
            map.addLayer(cluster.layer)

            map.setView(new ol.View({
                zoom: 2,
                center: [0, 0]
            }));

            return { map, cluster }
        },

        configureCluster() {
            const source = new ol.source.Vector({ features: [] });
            const clusterSource = new MultiPointCluster({
                distance: Math.max(FEATURE_CLUSTER_HEIGHT, FEATURE_HEIGHT),
                source
            });
            const layer = new ol.layer.Vector({
                id: 'elementsLayer',
                style: cluster => this.clusterStyle(cluster),
                source: clusterSource
            });

            return { source, clusterSource, layer }
        },

        clusterStyle() {
            if (!this._clusterStyleWithCache) {
                this._clusterStyleWithCache = _.memoize(
                    this._clusterStyle,
                    function clusterStateHash(cluster, options = { selected: false }) {
                        var count = cluster.get('count'),
                            selectionState = cluster.get('selectionState') || 'none',
                            key = [count, selectionState, JSON.stringify(options)];

                        if (count === 1) {
                            const feature = cluster.get('features')[0];
                            const { geoLocations, geometry, element, ...compare } = feature.getProperties();
                            key.push(JSON.stringify(compare, null, 0))
                        }
                        return key.join('')
                    }
                );
            }

            return this._clusterStyleWithCache.apply(this, arguments);
        },

        _clusterStyle(cluster, options = { selected: false }) {
            var count = cluster.get('count'),
                selectionState = cluster.get('selectionState') || 'none',
                selected = options.selected || selectionState !== 'none';

            if (count === 1) {
                const feature = cluster.get('features')[0];
                const isSelected = options.selected || feature.get('selected');

                return [new ol.style.Style({
                    image: new ol.style.Icon({
                        src: feature.get(isSelected ? 'iconUrlSelected' : 'iconUrl'),
                        imgSize: feature.get('iconSize'),
                        scale: 1 / feature.get('pixelRatio'),
                        anchor: feature.get('iconAnchor')
                    })
                })]
            } else {
                var radius = Math.min(count || 0, FEATURE_CLUSTER_HEIGHT / 2) + 10,
                    unselectedFill = 'rgba(241,59,60, 0.8)',
                    unselectedStroke = '#AD2E2E',
                    stroke = selected ? '#08538B' : unselectedStroke,
                    strokeWidth = Math.round(radius * 0.1),
                    textStroke = stroke,
                    fill = selected ? 'rgba(0,112,195, 0.8)' : unselectedFill;

                if (selected && selectionState === 'some') {
                    fill = unselectedFill;
                    textStroke = unselectedStroke;
                    strokeWidth *= 2;
                }

                return [new ol.style.Style({
                    image: new ol.style.Circle({
                        radius: radius,
                        stroke: new ol.style.Stroke({
                            color: stroke,
                            width: strokeWidth
                        }),
                        fill: new ol.style.Fill({
                            color: fill
                        })
                    }),
                    text: new ol.style.Text({
                        text: count.toString(),
                        font: `bold condensed ${radius}px sans-serif`,
                        textAlign: 'center',
                        fill: new ol.style.Fill({
                            color: '#fff',
                        }),
                        stroke: new ol.style.Stroke({
                            color: textStroke,
                            width: 2
                        })
                    })
                })];
            }
        },

        configureEvents({ map, cluster }) {
            var self = this;

            // Feature Selection
            const selectInteraction = new ol.interaction.Select({
                condition: ol.events.condition.click,
                layers: [cluster.layer],
                style: cluster => this.clusterStyle(cluster, { selected: true })
            });

            this.olEvents.push(selectInteraction.on('select', function(e) {
                var clusters = e.target.getFeatures().getArray(),
                    elements = { vertices: [], edges: [] };

                clusters.forEach(cluster => {
                    cluster.get('features').forEach(feature => {
                        const el = feature.get('element');
                        const key = el.type === 'vertex' ? 'vertices' : 'edges';
                        elements[key].push(el.id);
                    })
                })
                self.props.onSelectElements(elements);
            }));

            this.olEvents.push(map.on('click', function(event) {
                // TODO:
                //self.closeMenu();
                //self.onMapClicked(event, map);
            }));

            this.olEvents.push(cluster.clusterSource.on(ol.events.EventType.CHANGE, _.debounce(function() {
                var selected = selectInteraction.getFeatures(),
                    clusters = this.getFeatures(),
                    newSelection = [],
                    isSelected = feature => feature.get('selected');

                clusters.forEach(cluster => {
                    var innerFeatures = cluster.get('features');
                    if (_.any(innerFeatures, isSelected)) {
                        newSelection.push(cluster);
                        if (_.all(innerFeatures, isSelected)) {
                            cluster.set('selectionState', 'all');
                        } else {
                            cluster.set('selectionState', 'some');
                        }
                    } else {
                        cluster.unset('selectionState');
                    }
                })

                selected.clear()
                if (newSelection.length) {
                    selected.extend(newSelection)
                }
            }, 100)));

            map.addInteraction(selectInteraction);

            const viewport = map.getViewport();
            this.domEvent(viewport, 'contextmenu', function(event) {
                event.preventDefault();
            })
            this.domEvent(viewport, 'mouseup', function(event) {
                event.preventDefault();
                if (event.button === 2 || event.ctrlKey) {
                    // TODO
                    //self.handleContextMenu(event);
                }
            });
            this.domEvent(viewport, 'mousemove', function(event) {
                const pixel = map.getEventPixel(event);
                const hit = map.forEachFeatureAtPixel(pixel, () => true);
                if (hit) {
                    map.getTarget().style.cursor = 'pointer';
                } else {
                    map.getTarget().style.cursor = '';
                }
            });
        },

        domEvent(el, type, handler) {
            this.domEvents.push(() => el.removeEventListener(type, handler));
            el.addEventListener(type, handler, false);
        }
    })

    return OpenLayers;
})

