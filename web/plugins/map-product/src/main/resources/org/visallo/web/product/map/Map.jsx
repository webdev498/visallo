define([
    'react',
    './OpenLayers',
    'util/vertex/formatters'
], function(React, OpenLayers, F) {
    'use strict';

    const PropTypes = React.PropTypes;
    const Map = React.createClass({
        propTypes: {
            configProperties: PropTypes.object.isRequired,
            onSelectElements: PropTypes.func.isRequired
        },

        render() {
            return (
                <OpenLayers
                    features={this.mapElementsToFeatures()}
                    onSelectElements={this.props.onSelectElements}
                    {...this.getTilePropsFromConfiguration()}
                />
            )
        },

        mapElementsToFeatures() {
            const extended = this.props.product.extendedData;
            const { vertices, edges } = extended;
            const elementsSelectedById = { ..._.indexBy(this.props.selection.vertices), ..._.indexBy(this.props.selection.edges) };
            const productVertices = _.pick(this.props.elements.vertices, _.pluck(vertices, 'id'));
            const productEdges = _.pick(this.props.elements.edges, _.pluck(edges, 'id'));
            const elements = Object.values(productVertices).concat(Object.values(productEdges));
            const geoLocationProperties = _.groupBy(this.props.ontologyProperties, 'dataType').geoLocation;

            return elements.map(el => {
                const geoLocations = geoLocationProperties &&
                    _.chain(geoLocationProperties)
                        .map(function(geoLocationProperty) {
                            return F.vertex.props(el, geoLocationProperty.title);
                        })
                        .compact()
                        .flatten()
                        .filter(function(g) {
                            return g.value && g.value.latitude && g.value.longitude;
                        })
                        .map(function(g) {
                            return [g.value.longitude, g.value.latitude];
                        })
                        .value(),
                    // TODO: check with edges
                    conceptType = F.vertex.prop(el, 'conceptType'),
                    selected = el.id in elementsSelectedById,
                    iconUrl = 'map/marker/image?' + $.param({
                        type: conceptType,
                        scale: this.props.pixelRatio > 1 ? '2' : '1',
                    }),
                    iconUrlSelected = `${iconUrl}&selected=true`;

                return {
                    id: el.id,
                    element: el,
                    selected,
                    iconUrl,
                    iconUrlSelected,
                    iconSize: [22, 40].map(v => v * this.props.pixelRatio),
                    iconAnchor: [0.5, 1.0],
                    pixelRatio: this.props.pixelRatio,
                    geoLocations
                }
            })
        },

        getTilePropsFromConfiguration() {
            const config = {...this.props.configProperties};
            const getOptions = function(providerName) {
                try {
                    var obj,
                        prefix = `map.provider.${providerName}.`,
                        options = _.chain(config)
                        .pick((val, key) => key.indexOf(`map.provider.${providerName}.`) === 0)
                        .tap(o => { obj = o })
                        .pairs()
                        .map(([key, value]) => {
                            if (/^[\d.-]+$/.test(value)) {
                                value = parseFloat(value, 10);
                            } else if ((/^(true|false)$/).test(value)) {
                                value = value === 'true'
                            } else if ((/^\[[^\]]+\]$/).test(value) || (/^\{[^\}]+\}$/).test(value)) {
                                value = JSON.parse(value)
                            }
                            return [key.replace(prefix, ''), value]
                        })
                        .object()
                        .value()
                    return options;
                } catch(e) {
                    console.error(`${prefix} options could not be parsed. input:`, obj)
                    throw e;
                }
            };

            var source = config['map.provider'] || 'osm';
            var sourceOptions;

            if (source === 'google') {
                console.warn('google map.provider is no longer supported, switching to OpenStreetMap provider');
                source = 'osm';
            }

            if (source === 'osm') {
                // Legacy configs accepted csv urls, warn and pick first
                var osmURL = config['map.provider.osm.url'];
                if (osmURL && osmURL.indexOf(',') >= 0) {
                    console.warn('Comma-separated Urls not supported, using first url. Use urls with {a-c} for multiple CDNS');
                    console.warn('For Example: https://{a-c}.tile.openstreetmap.org/{z}/{x}/{y}.png');
                    config['map.provider.osm.url'] = osmURL.split(',')[0].trim().replace(/[$][{]/g, '{');
                }
                sourceOptions = getOptions('osm');
                source = 'OSM';
            } else if (source === 'ArcGIS93Rest') {
                var urlKey = 'map.provider.ArcGIS93Rest.url';
                // New OL3 ArcGIS Source will throw an error if url doesn't end
                // with [Map|Image]Server
                if (config[urlKey]) {
                    config[urlKey] = config[urlKey].replace(/\/export(Image)?\/?\s*$/, '');
                }
                sourceOptions = { params: { layers: 'show:0,1,2' }, ...getOptions(source) };
                source = 'TileArcGISRest'
            } else {
                sourceOptions = getOptions(source)
            }

            return { source, sourceOptions };
        }

    });

    return Map;
});
