define([
    'react',
    'openlayers',
    'fast-json-patch'
], function(
    React,
    ol,
    jsonpatch) {

    const isEdge = data => (data.source !== undefined)
    const isNode = _.negate(isEdge)

    const OpenLayers = React.createClass({

        componentDidUpdate() {
        },

        componentDidMount() {
            this._map = this.configureMap();
        },

        render() {
            return (
                <div style={{height: '100%'}} ref="map"></div>
            )
        },

        configureMap() {
            const map = new ol.Map({
                loadTilesWhileInteracting: true,
                keyboardEventTarget: document,
                controls: [],
                layers: [],
                target: this.refs.map
            });

            const baseLayerSource = new ol.source.OSM()
            map.addLayer(new ol.layer.Tile({ source: baseLayerSource }));
            map.setView(new ol.View({
                zoom: 2,
                center: [0, 0]
            }));

            return map
        }

    })

    return OpenLayers;
})

