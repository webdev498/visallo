/*globals chai:true, assert:true, expect: true, i18n: true, sinon:true*/
(function(global) {
var tests = Object.keys(global.__karma__.files).filter(function(file) {
    return (/^\/base\/test\/unit\/spec\/.*\.js$/).test(file);
});

requirejs.config({
    shim: {
        '/base/jsc/require.config.js': { exports: 'require' }
    }
});

requirejs(['/base/jsc/require.config.js'], function(cfg) {

    var requireConfig = $.extend(true, {}, cfg, unminifyForTest('react', 'react-dom', 'react-redux'), {

        // Karma serves files from '/base'
        baseUrl: '/base/jsc',

        paths: {
            chai: '../node_modules/chai/chai',
            'chai-datetime': '../node_modules/chai-datetime/chai-datetime',
            'chai-spies': '../node_modules/chai-spies/chai-spies',
            'chai-as-promised': '../node_modules/chai-as-promised/lib/chai-as-promised',
            'mocha-flight': '../test/unit/utils/mocha-flight',
            'sinon': '../node_modules/sinon/pkg/sinon',
            'sinon-chai': '../node_modules/sinon-chai/lib/sinon-chai',


            // MOCKS
            'util/service/dataPromise': '../test/unit/mocks/dataPromise',
            'util/service/messagesPromise': '../test/unit/mocks/messagePromise',
            'util/service/ontologyPromise': '../test/unit/mocks/ontologyPromise',
            'util/service/propertiesPromise': '../test/unit/mocks/propertiesPromise',
            'util/messages': '../test/unit/mocks/messages',
            'data/web-worker/store': '../test/unit/mocks/store',
            'data/web-worker/util/ajax': '../test/unit/mocks/ajax',
            testutils: '../test/unit/utils'
        },

        shim: {},

        deps: [
            'chai',
            '../libs/underscore/underscore'
        ],

        callback: function(_chai) {
            global.chai = _chai
            if (typeof Function.prototype.bind !== 'function') {
                /*eslint no-extend-native:0 */
                Function.prototype.bind = function() {
                    var args = _.toArray(arguments),
                        bindArgs = [this, args.shift()].concat(args);
                    return _.bind.apply(_, bindArgs);
                }
            }
            console.warn = _.wrap(console.warn, function() {
                var args = _.toArray(arguments),
                    fn = args.shift(),
                    isPhantomJS = navigator.userAgent.indexOf('PhantomJS') >= 0,
                    isWrongPromiseWarning = isPhantomJS && _.isString(args[0]) && args[0].indexOf('rejected with a non-error: [object Error]') >= 0;

                // Bug with phantom and bluebird
                // https://github.com/petkaantonov/bluebird/issues/990

                if (!isWrongPromiseWarning) {
                    return fn.apply(this, args);
                }
            });

            global.visalloData = {
                currentWorkspaceId: 'w1',
                currentUser: {
                    authorizations: ['a','b']
                }
            };
            global.visalloEnvironment = { dev: false, prod: true };

            _.templateSettings.escape = /\{([\s\S]+?)\}/g;
            _.templateSettings.evaluate = /<%([\s\S]+?)%>/g;
            _.templateSettings.interpolate = /\{-([\s\S]+?)\}/g;

            require([
                'chai-datetime',
                'chai-spies',
                'chai-as-promised',
                'sinon',
                'sinon-chai',
                'util/handlebars/before_auth_helpers',
                'util/handlebars/after_auth_helpers',
                'util/jquery.flight',
                'util/jquery.removePrefixedClasses',
                'mocha-flight'
            ], function(chaiDateTime, chaiSpies, chaiAsPromised, _sinon, sinonChai) {

                chai.should();
                chai.use(chaiDateTime);
                chai.use(chaiAsPromised);
                chai.use(sinonChai);

                var originalError = console.error.bind(console);
                console.error = function() {
                    if (/^Data request went unhandled$/.test(arguments[0])) {
                        /*eslint no-empty:0*/
                        // Ignore these as they can happen when tests run
                        // quickly and dataRequests not needed for test
                    } else {
                        originalError.apply(null, arguments);
                    }
                };

                sinon = _sinon;

                // Globals for assertions
                assert = chai.assert;
                expect = chai.expect;

                i18n = function(key) {
                    return key;
                };

                // Use the twitter flight interface to mocha
                mocha.ui('mocha-flight');
                mocha.timeout(10000)
                mocha.options.globals.push('ejs', 'cytoscape', 'DEBUG');

                // Run tests after loading
                if (tests.length) {
                    require(tests, function() {
                        global.__karma__.start();
                    });
                } else global.__karma__.start();
            });

        }

    });
    requireConfig.deps = requireConfig.deps.concat(cfg.deps);
    delete requireConfig.urlArgs;

    global.require = requirejs;
    requirejs.config(requireConfig);

    function unminifyForTest() {
        var override = {};
        for (var i = 0; i < arguments.length; i++) {
            var name = arguments[i];
            if (name in cfg.paths) {
                override[name] = cfg.paths[name].replace(/\.min$/, '')
            }
        }
        return { paths: override };
    }
});
})(this);

