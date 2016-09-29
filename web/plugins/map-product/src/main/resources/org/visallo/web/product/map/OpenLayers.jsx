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
            sourceOptions: PropTypes.object
        },

        componentDidUpdate() {
            if (this.state.cluster) {
                const maybeRemove = _.indexBy(this.state.cluster.source.getFeatures(), f => f.getId());
                this.state.cluster.source.addFeatures(this.props.features.map(data => {
                    const { id, geoLocations, element, ...rest } = data;
                    var feature = new ol.Feature({
                        ...rest,
                        element,
                        geometry: new ol.geom.MultiPoint(geoLocations.map(function(geo) {
                            return ol.proj.fromLonLat(geo);
                        })),
                    })
                    feature.setId(data.id);
                    maybeRemove[data.id] = false;
                    return feature;
                }))

                _.forEach(maybeRemove, feature => {
                    if (feature !== false) this.state.cluster.source.removeFeature(feature);
                });
            }
        },

        componentDidMount() {
            const { map, cluster } = this.configureMap();
            this.setState({ map, cluster })
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
            const cluster = new MultiPointCluster({
                distance: Math.max(FEATURE_CLUSTER_HEIGHT, FEATURE_HEIGHT),
                source
            });
            const layer = new ol.layer.Vector({
                id: 'elementsLayer',
                style: cluster => this.clusterStyle(cluster),
                source: cluster
            });

            return { source, cluster, layer }
        },

        clusterStyle() {
            if (!this._clusterStyleWithCache) {
                this._clusterStyleWithCache = _.memoize(
                    this._clusterStyle,
                    function clusterStateHash(cluster) {
                        var count = cluster.get('count'),
                            selectionState = cluster.get('selectionState') || 'none',
                            key = [count, selectionState];

                        if (count === 1) {
                            // TODO
                            //var vertex = cluster.get('features')[0].get('vertex'),
                                //conceptType = F.vertex.prop(vertex, 'conceptType');
                            //key.push('type=' + conceptType);
                        }
                        return key.join('')
                    }
                );
            }

            return this._clusterStyleWithCache.apply(this, arguments);
        },

        _clusterStyle(cluster) {
            var count = cluster.get('count'),
                selectionState = cluster.get('selectionState') || 'none',
                selected = selectionState !== 'none';

            //return [new ol.style.Style({
                //image: new ol.style.Circle({
                    //radius: count * 25,
                    //stroke: new ol.style.Stroke({
                        //color: 'red',
                        //width: 2
                    //}),
                    //fill: new ol.style.Fill({
                        //color: 'blue'
                    //})
                //})
            //})]
            if (count === 1) {
                var feature = cluster.get('features')[0];
                return [new ol.style.Style({
                    image: new ol.style.Icon({
                        src: feature.get('iconUrl'),
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
        }
    })

    return OpenLayers;
})

