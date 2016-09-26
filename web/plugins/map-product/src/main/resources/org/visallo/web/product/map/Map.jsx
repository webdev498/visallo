define([
    'react',
    './OpenLayers',
    'util/vertex/formatters'
], function(React, OpenLayers, F) {
    'use strict';

    const PropTypes = React.PropTypes;
    const Map = React.createClass({

        render() {
            return (
                <OpenLayers />
            )
        }

    });

    return Map;
});
