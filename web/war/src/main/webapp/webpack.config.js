var path = require('path');
var webpack = require('webpack');

//todo remove requirejs plugin npm modules

module.exports = {
    //todo entry object for code splitting
    entry: path.resolve(__dirname, 'js', 'visallo'),
    output: {
        path: __dirname + '/jsc',
        publicPath: '/jsc/',
        filename: 'bundle.js'
    },
    module: {
        loaders: [
            { test: /\.hbs$/, loader: 'handlebars-loader' },
            { test: /\.ejs$/, loader: 'ejs-compiled?_with=false' }
        ]
    },
    plugins: [
        new webpack.NoErrorsPlugin(),
        //todo remove globals
        new webpack.ProvidePlugin({
            $: 'jquery',
            _: 'underscore'
        })
    ],
    //todo use unminfied 3rd party deps?
    resolve: {
        //todo use lib instead of node_modules?
        modules: [path.resolve(__dirname, 'js'), 'node_modules'],
        alias: {
            arbor: 'cytoscape-arbor/arbor',
            async: 'requirejs-plugins/src/async',
            beautify: 'js-beautify/js/lib/beautify',
            bootstrap: 'bootstrap/docs/assets/js/bootstrap.min',
            'bootstrap-datepicker': 'bootstrap-datepicker/js/bootstrap-datepicker',
            'bootstrap-timepicker': 'bootstrap-timepicker/js/bootstrap-timepicker',
            bluebird: 'bluebird/js/browser/bluebird',
            chrono: 'chrono-node/chrono.min',
            colorjs: 'color-js/color',
            cytoscape: 'cytoscape/dist/cytoscape.min',
            d3: 'd3/d3.min',
            'd3-tip': 'd3-tip/index',
            'd3-plugins': 'd3-plugins-dist/dist/mbostock',
            'deep-freeze-strict': 'amd-wrap/deep-freeze-strict/index',
            'duration-js': 'duration-js/duration',
            easing: 'jquery.easing/jquery.easing.1.3',
            ejs: 'ejs/ejs',
            //'flight/lib': 'util/flight/compat',
            'flight/lib/advice': path.resolve(__dirname, 'js/util/flight/compat/advice'),
            'flight/lib/component': path.resolve(__dirname, 'js/util/flight/compat/component'),
            'flight/lib/compose': path.resolve(__dirname, 'js/util/flight/compat/compose'),
            'flight/lib/debug': path.resolve(__dirname, 'js/util/flight/compat/debug'),
            'flight/lib/logger': path.resolve(__dirname, 'js/util/flight/compat/logger'),
            'flight/lib/registry': path.resolve(__dirname, 'js/util/flight/compat/registry'),
            flight: 'flightjs/build/flight',
            gremlins: 'gremlins.js/gremlins.min',
            gridstack: 'gridstack/dist/gridstack.min',
            //hbs: 'require-handlebars-plugin/hbs',
            //handlebars: 'require-handlebars-plugin/hbs/handlebars',
            jstz: 'jstimezonedetect/dist/jstz.min',
            jquery: 'jquery/dist/jquery.min',
            //'jquery-ui-bundle': 'jquery-ui-bundle/jquery-ui.min',
            'jquery-ui/droppable': 'jquery-ui-bundle/jquery-ui.min',
            'jquery-ui/core': 'jquery-ui-bundle/jquery-ui.min',
            'jquery-ui/widget': 'jquery-ui-bundle/jquery-ui.min',
            'jquery-ui/mouse': 'jquery-ui-bundle/jquery-ui.min',
            'jquery-ui/resizable': 'jquery-ui-bundle/jquery-ui.min',
            'jquery-ui/draggable': 'jquery-ui-bundle/jquery-ui.min',
            'jquery-ui': 'jquery-ui-bundle/jquery-ui.min',
            'jquery-scrollstop': 'jquery-scrollstop/jquery.scrollstop',
            jscache: 'jscache-lru/cache',
            less: 'requirejs-less/less',
            lessc: 'requirejs-less/lessc',
            moment: 'moment/min/moment-with-locales.min',
            'moment-timezone': 'moment-timezone/builds/moment-timezone-with-data.min',
            normalize: 'requirejs-less/normalize',
            openlayers: 'openlayers2/build/OpenLayers',
            pathfinding: 'pathfinding/lib/pathfinding-browser.min',
            propertyParser: 'requirejs-plugins/src/propertyParser',
            'rangy-core': 'rangy/lib/rangy-core',
            'rangy-text': 'rangy/lib/rangy-textrange',
            'rangy-highlighter': 'rangy/lib/rangy-highlighter',
            'rangy-cssclassapplier': 'rangy/lib/rangy-classapplier',
            'rangy-serializer': 'rangy/lib/rangy-serializer',
            sf: 'sf/sf',
            text: 'text/text',
            tpl: 'requirejs-ejs-plugin/rejs',
            underscore: 'underscore/underscore-min',
            'underscore.inflection': 'underscore.inflection/lib/underscore.inflection',
            videojs: 'video.js/dist/video'
        }
    },
    stats: {
        colors: true
    },
    devtool: 'source-map'
};
