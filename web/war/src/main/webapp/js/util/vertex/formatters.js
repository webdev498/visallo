define([
    '../formatters',
    './urlFormatters',
    './formula',
    'util/messages',
    'util/requirejs/promise!../service/ontologyPromise',
    'util/visibility/util'
], function(
    F,
    vertexUrl,
    formula,
    i18n,
    ontology,
    visibilityUtil) {
    'use strict';

    var propertiesByTitle = ontology.properties.byTitle,
        propertiesByDependentToCompound = ontology.properties.byDependentToCompound,

        /**
         * Utilities that assist in transforming vertices and edges.
         *
         * @alias module:formatters.vertex
         * @namespace
         */
        V = {

            /**
             * Check if the passed object is currently published from a case
             *
             * @param {Object} element The vertex/edge json to check
             * @returns {boolean} Whether the element is published
             */
            isPublished: function(vertex) {
                return V.sandboxStatus.apply(null, arguments) === undefined;
            },

            /**
             * Get the state of the vertex or property
             * * `published`: Unpublished in case or Published but changed in case
             * * `undefined`: Published and not changed in case
             *
             * @param {object} Vertex or property json
             * @returns {string|undefined} result
             */
            sandboxStatus: function(vertexOrProperty) {
                if (arguments.length === 3) {
                    var props = V.props.apply(null, arguments);
                    if (props.length) {
                        return _.any(props, function(p) {
                            return V.sandboxStatus(p) === undefined;
                        }) ? undefined : i18n('vertex.status.unpublished');
                    }
                    return;
                }

                return (/^(private|public_changed)$/i).test(vertexOrProperty.sandboxStatus) ?
                        i18n('vertex.status.unpublished') :
                        undefined;
            },

            getVertexAndEdgeIdsFromDataEventOrCurrentSelection: function(data) {
                return Promise.all([
                    V.getVertexIdsFromDataEventOrCurrentSelection(data, {async: true}),
                    V.getEdgeIdsFromDataEventOrCurrentSelection(data)
                ]).spread(function (vertexIds, edgeIds) {
                   return {
                       vertexIds: vertexIds,
                       edgeIds: edgeIds
                   };
                });
            },

            /**
             * Helper to get vertexIds from either an events data or the
             * current selection.
             *
             * Useful to get the vertexIds that should be transformed based on
             * context menus.
             *
             * @param {object} data Some event data
             * @param {object} [opts={}] Options
             * @param {object} [opts.async=false] Returns a promise if true
             * @returns {Array.<string>} the vertex ids
             */
            getVertexIdsFromDataEventOrCurrentSelection: function(data, opts) {
                // Normalize the vertexIds sent from a vertex menu event,
                // also checking the current object selection
                var vertexIds = [],
                    options = opts || {},
                    // Return a promise (will return more accurate list that
                    // isn't susceptible to selectObjects -> objectsSelected
                    // race condition
                    async = options.async;

                if (data && data.vertexId) {
                    vertexIds = [data.vertexId];
                } else if (data && data.vertexIds) {
                    vertexIds = data.vertexIds;
                }

                if (typeof window.visalloData !== 'undefined') {
                    if (async) {
                        return visalloData.selectedObjectsPromise()
                            .then(vertexIdsUsingSelectedObjects);
                    } else {
                        console.warn('Use { async: true } when calling getVertexIdsFromDataEventOrCurrentSelection')
                        return vertexIdsUsingSelectedObjects(visalloData.selectedObjects)
                    }
                }

                return vertexIdsUsingSelectedObjects();

                function vertexIdsUsingSelectedObjects(selectedObjects) {
                    if (selectedObjects && selectedObjects.vertices.length > 0) {
                        var selectedVertexIds = _.pluck(selectedObjects.vertices, 'id');
                        if (_.intersection(vertexIds, selectedVertexIds).length) {
                            vertexIds = vertexIds.concat(selectedVertexIds);
                        } else if (!vertexIds.length) {
                            vertexIds = selectedVertexIds;
                        }
                    }
                    return _.unique(vertexIds);
                }
            },

            getEdgeIdsFromDataEventOrCurrentSelection: function(data) {
                // Normalize the edgeIds sent from a edge menu event,
                // also checking the current object selection
                var edgeIds = [];

                if (data && data.edgeId) {
                    edgeIds = [data.edgeId];
                } else if (data && data.edgeIds) {
                    edgeIds = data.edgeIds;
                }

                if (typeof window.visalloData !== 'undefined') {
                    return visalloData.selectedObjectsPromise()
                        .then(edgeIdsUsingSelectedObjects);
                }

                return edgeIdsUsingSelectedObjects();

                function edgeIdsUsingSelectedObjects(selectedObjects) {
                    if (selectedObjects && selectedObjects.edges.length > 0) {
                        var selectedEdgeIds = _.pluck(selectedObjects.edges, 'id');
                        if (_.intersection(edgeIds, selectedEdgeIds).length) {
                            edgeIds = edgeIds.concat(selectedEdgeIds);
                        } else if (!edgeIds.length) {
                            edgeIds = selectedEdgeIds;
                        }
                    }
                    return _.unique(edgeIds);
                }
            },
            /**
             * Transformers for display of property metadata
             *
             * Define/override metadata dataType specific

             * displayTransformers here, based on values set in
             * configuration: `properties.metadata.propertyNamesType`
             * {@link http://docs.visallo.org/front-end/#property-info-metadata}
             *
             * All functions receive: function(el, value, property, vertexId)
             * set the value synchronously
             * - or -
             * append "Async" to function name and return a Promise
                }

             *

             * @namespace
             * @example
             * require(['util/vertex/formatters'], function(F) {
             *     F.vertex.metadata.customMetadataType = function(el, val) {
             *         el.textContent = transform(val)
             *     }
             * })
             */
            metadata: {

                /**
                 * Transform to date time string
                 */
                datetime: function(el, value) {
                    el.textContent = F.date.dateTimeString(value);
                    return el;
                },

                /**
                 * Transform to sandbox display
                 */
                sandboxStatus: function(el, value) {
                    el.textContent = V.sandboxStatus({ sandboxStatus: value }) || '';
                    return el;
                },

                /**
                 * Transform to percent display
                 */
                percent: function(el, value) {
                    el.textContent = F.number.percent(value);
                    return el;
                },

                /**
                 * Transform user id to display name
                 */
                userAsync: function(el, userId) {
                    return Promise.require('util/withDataRequest')
                        .then(function(withDataRequest) {
                            return withDataRequest.dataRequest('user', 'getUserNames', [userId])
                        })
                        .then(function(users) {
                            el.textContent = users && users[0] || i18n('user.unknown.displayName');
                            return el;
                        })
                }
            },

            /**
             * Define/override specific displayTransformers for
             * properties. These are used to transform property json into
             * displayed versions in the Element Inspector.
             *
             * All functions receive: `function(HtmlElement, property, element)` and
             * should populate the dom element with a value.
             *
             * The function name matches the `displayType` value in the ontology for the property.
             * Those defined below are built-in.
             *
             * @namespace
             * @example <caption>Add new displayType</caption>
             * // Add new displayType formatter
             * require(['util/vertex/formatters'], function(F) {
             *     F.vertex.properties.customDisplayType = function(htmlElement, property, element) {
             *         htmlElement.textContent = processValue(p.value)
             *     }
             * })
             *
             * // In ontology.owl
             * <visallo:displayType>customDisplayType</visallo:displayType>
             * @example <caption>Add multiple displayTypes</caption>
             * require(['util/vertex/formatters'], function(F) {
             *     Object.assign(F.vertex.properties, {
             *         streetAddress: function(el, prop) { },
             *         internationalPhone: function(el, prop) { }
             *     })
             * })
             */
            properties: {

                /**
                 * Visibility can be customized with the
                 * {@link http://docs.visallo.org/extension-points/front-end/visibility/|visibility extension point}
                 */
                visibility: function(el, property, element) {
                    visibilityUtil.attachComponent('viewer', el, {
                        property: property,
                        value: property.value && property.value.source,
                        element: element
                    })

                    return el;
                },

                'directory/entity': function(el, property) {
                    return F.directoryEntity.requestPretty(property.value)
                      .then(function(value) {
                          $(el).text(value);
                          return el;
                      });
                },

                /**
                 * Display geolocation, with description if available
                 *
                 * @example
                 * <visallo:displayType>geoLocation</visallo:displayType>
                 */
                geoLocation: function(el, property) {
                    var wrap = $('<span>'),
                        displayValue = F.geoLocation.pretty(property.value, true);

                    if (property.value.description) {
                        wrap.append(property.value.description + ' ');
                    }

                    $('<small>')
                        .css('white-space', 'nowrap')
                        .text(F.geoLocation.pretty(property.value, true))
                        .appendTo(wrap);

                    wrap.appendTo(el);

                    return el;
                },

                /**
                 * For property values with number of bytes, formats to human
                 * readable
                 *
                 * @example
                 * <visallo:displayType>bytes</visallo:displayType>
                 */
                bytes: function(el, property) {
                    el.textContent = F.bytes.pretty(property.value);
                    return el;
                },

                /**
                 * For property values that contain URLs, render as a link.
                 * If the metadata property `http://visallo.org#linkTitle`
                 * exists for property, display that as link text.
                 *
                 * Opens in a new window
                 *
                 * @example
                 * <visallo:displayType>link</visallo:displayType>
                 */
                link: function(el, property, vertex) {
                    var anchor = document.createElement('a'),
                        value = V.prop(vertex, property.name, property.key),
                        href = $.trim(value),
                        linkTitle = property.metadata['http://visallo.org#linkTitle'];

                    if (!(/^http/).test(href)) {
                        href = 'http://' + href;
                    }

                    anchor.setAttribute('href', href);
                    anchor.setAttribute('target', '_blank');
                    anchor.textContent = linkTitle || href;

                    el.appendChild(anchor);

                    return el;
                },

                /**
                 * Render the property value with whitespace preserving
                 *
                 * @example
                 * <visallo:displayType>textarea</visallo:displayType>
                 */
                textarea: function(el, property) {
                    $(el).html(_.escape(property.value || '').replace(/\r?\n+/g, '<br>'));
                    return el;
                },

                /**
                 * For property values containing degrees, will
                 * render an arrow and show human readable heading.
                 *
                 * @example
                 * <visallo:displayType>heading</visallo:displayType>
                 */
                heading: function(el, property) {
                    var div = document.createElement('div'),
                        dim = 12,
                        half = dim / 2;

                    el.textContent = F.number.heading(property.value);
                    div.style.width = div.style.height = dim + 'px';
                    div.style.display = 'inline-block';
                    div.style.marginRight = '0.25em';
                    div = el.insertBefore(div, el.childNodes[0]);

                    return Promise.require('d3')
                        .then(function(d3) {
                            d3.select(div)
                                .append('svg')
                                    .style('vertical-align', 'middle')
                                    .attr('width', dim)
                                    .attr('height', dim)
                                    .append('g')
                                        .attr('transform', 'rotate(' + property.value + ' ' + half + ' ' + half + ')')
                                        .call(function() {
                                            this.append('line')
                                                .attr('x1', half)
                                                .attr('y1', 0)
                                                .attr('x2', half)
                                                .attr('y2', dim)
                                                .call(styling)

                                            this.append('g')
                                                .attr('transform', 'rotate(30 ' + half + ' 0)')
                                                .call(createArrowLine)

                                            this.append('g')
                                                .attr('transform', 'rotate(-30 ' + half + ' 0)')
                                                .call(createArrowLine)
                                        });

                            return el;
                        });

                    function createArrowLine() {
                        this.append('line')
                            .attr('x1', half)
                            .attr('y1', 0)
                            .attr('x2', half)
                            .attr('y2', dim / 3)
                            .call(styling);
                    }
                    function styling() {
                        this.attr('stroke', '#555')
                            .attr('line-cap', 'round')
                            .attr('stroke-width', '1');
                    }
                }

            },

            /**
             * Check if the property has any of the given metadata values
             *
             * @param {object} property The property to check
             * @param {Array.<string>} metadataPropertyNames The metadata names
             * to check if there are values
             * @returns {boolean} True if any of the metadata exists
             */
            hasMetadata: function(property, metadataPropertyNames) {
                return (V.sandboxStatus(property) && metadataPropertyNames.indexOf('sandboxStatus') > -1) ||
                    _.some(metadataPropertyNames, function(name) {
                        return property.metadata && !_.isUndefined(property.metadata[name]);
                    });
            },

            /**
             * Given a vertex, get the ontology concept object.
             *
             * If not found in ontology returns the root concept
             *
             * @param {object} vertex
             * @returns {object} concept
             */
            concept: function(vertex) {
                var conceptType = vertex && V.prop(vertex, 'conceptType'), concept;

                if (!conceptType || conceptType === 'Unknown') {
                    conceptType = 'http://www.w3.org/2002/07/owl#Thing';
                }

                concept = ontology.concepts.byId[conceptType];
                if (!concept && conceptType !== 'relationship') {
                    console.warn('Concept: ' + conceptType + ' is not in ontology');
                    concept = ontology.concepts.byId['http://www.w3.org/2002/07/owl#Thing'];
                }
                return concept;
            },

            /**
             * Given a vertex get all valid properties for that vertex
             * concept and all parent concepts
             *
             * @param {object} vertex
             * @return {Array.<object>} Valid properties for concept
             */
            conceptProperties: function(vertex) {
                var concept = V.concept(vertex),
                    properties = [];
                do {
                    properties = properties.concat(concept.properties);
                    concept = concept.parentConcept && ontology.concepts.byId[concept.parentConcept];
                } while (concept);
                return properties;
            },

            /**
             * Check if the vertex concept has the property.
             *
             * _Does not check if the vertex has the property, just
             * that the concept (or ancestors) lists it as valid in
             * ontology._
             *
             * @param {object} vertex
             * @param {string} propertyName
             * @returns {boolean} if the vertex has the property
             */
            hasProperty: function(vertex, propertyName) {
                var concept = V.concept(vertex);
                do {
                    if (concept && concept.properties.indexOf(propertyName) >= 0) {
                        return true;
                    }
                    concept = concept.parentConcept && ontology.concepts.byId[concept.parentConcept];
                } while (concept);
                return false;
            },

            /**
             * Check if the vertex concept (or concept ancestors) matches
             * the filter.
             *
             * @param {object} vertex
             * @param {string} conceptTypeFilter IRI of concept
             * @returns {boolean} if the vertex concept matches the filter
             */
            isKindOfConcept: function(vertex, conceptTypeFilter) {
                var conceptType = V.prop(vertex, 'conceptType');

                do {
                    if (conceptType === conceptTypeFilter) {
                        return true;
                    }

                    conceptType = ontology.concepts.byId[conceptType].parentConcept;
                } while (conceptType)

                return false;
            },

            externalImage: function(vertex, optionalWorkspaceId, url, maxWidth, maxHeight) {
                var params = {
                        vId: vertex.id,
                        url: url,
                        workspaceId: optionalWorkspaceId || visalloData.currentWorkspaceId,
                        maxWidth: maxWidth || 400,
                        maxHeight: maxHeight || 400
                    },
                    template = _.template('{origin}/resource/external?');

                return template({
                    origin: location.origin
                }) + $.param(params);
            },

            /**
             * Get the image representing this vertex
             *
             * @param {object} vertex
             * @param {object} [optionalWorkspaceId=]
             * @param {number} [width=400]
             * @returns {string} url to image
             */
            image: function(vertex, optionalWorkspaceId, width) {
                var entityImageUrl = V.prop(vertex, 'entityImageUrl');
                if (entityImageUrl) {
                    return V.externalImage(vertex, optionalWorkspaceId, entityImageUrl, width, width);
                }

                var entityImageVertexId = V.prop(vertex, 'entityImageVertexId'),
                    concept = V.concept(vertex),
                    displayType = V.displayType(vertex),
                    isImage = displayType === 'image',
                    isVideo = displayType === 'video';

                if (entityImageVertexId || isImage) {
                    var params = {
                        workspaceId: optionalWorkspaceId || visalloData.currentWorkspaceId,
                        graphVertexId: entityImageVertexId || vertex.id,
                        width: width || 150
                    };

                    _.each(vertex.properties, function(prop) {
                        var ontologyProperty = ontology.properties.byTitle[prop.name],
                            intents = ontologyProperty ? ontologyProperty.intents : null;
                        if (intents) {
                            if (_.indexOf(intents, 'media.clockwiseRotation') >= 0) {
                                params.rotation = prop.value;
                            }
                            if (_.indexOf(intents, 'media.yAxisFlipped') >= 0) {
                                params.flip = prop.value;
                            }
                        }
                    });

                    return 'vertex/thumbnail?' + $.param(params);
                }

                if (isVideo) {
                    var posterFrame = _.any(vertex.properties, function(p) {
                        return p.name === 'http://visallo.org#rawPosterFrame';
                    });
                    if (posterFrame) {
                        return 'vertex/poster-frame?' + $.param({
                            workspaceId: optionalWorkspaceId || visalloData.currentWorkspaceId,
                            graphVertexId: vertex.id
                        });
                    }
                }

                return concept.glyphIconHref;
            },

            /**
             * Get the vertex image when selected (if available).
             * Otherwise just return main image.
             *
             * @param {object} vertex
             * @param {object} [optionalWorkspaceId=]
             * @param {number} [width=400]
             * @returns {string} url to image
             */
            selectedImage: function(vertex, optionalWorkspaceId, width) {
                var concept = V.concept(vertex),
                    conceptImage = V.image(vertex, optionalWorkspaceId, width);
                return (conceptImage === concept.glyphIconHref) ? (concept.glyphIconSelectedHref || conceptImage) : conceptImage;
            },

            /**
             * Check if this element has a custom image, or if it
             * will just display the concepts icon.
             *
             * @param {object} vertex
             * @param {object} [optionalWorkspaceId=]
             * @returns {boolean} true if the image for `vertex` is just the
             * concept icon.
             */
            imageIsFromConcept: function(vertex, optionalWorkspaceId) {
                return V.image(vertex, optionalWorkspaceId) === V.concept(vertex).glyphIconHref;
            },

            /**
             * Larger version of vertex image. 800 pixels.
             *
             * Used in the Element Inspector.
             *
             * @param {object} vertex
             * @param {object} [optionalWorkspaceId=]
             * @returns {string} url to image
             */
            imageDetail: function(vertex, optionalWorkspaceId) {
                return V.image(vertex, optionalWorkspaceId, 800);
            },

            raw: function(vertex, optionalWorkspaceId) {
                return 'vertex/raw?' + $.param({
                    workspaceId: optionalWorkspaceId || visalloData.currentWorkspaceId,
                    graphVertexId: vertex.id
                });
            },

            imageFrames: function(vertex, optionalWorkspaceId) {
                var videoPreview = _.any(vertex.properties, function(p) {
                    return p.name === 'http://visallo.org#videoPreviewImage';
                });
                if (videoPreview) {
                    return 'vertex/video-preview?' + $.param({
                        workspaceId: optionalWorkspaceId || visalloData.currentWorkspaceId,
                        graphVertexId: vertex.id
                    });
                }
            },

            sortByProperties: function(vertices, name, options) {
                var verticesWithValues = _.partition(vertices, function(vertex) {
                        var allProps = V.props(vertex, name);
                        if (allProps.length === 0) return false;
                        var prop = V.prop(vertex, name, undefined, { defaultValue: ' ' });
                        if (_.isString(prop)) {
                            prop = prop.trim();
                        }
                        if (_.isUndefined(prop)) return false;
                        if (_.isString(prop) && _.isEmpty(prop)) return false;
                        return true;
                    }),
                    sortedNoValue = _.sortBy(verticesWithValues[1], function(vertex) {
                        return V.title(vertex);
                    }),
                    sorted = _.sortBy(verticesWithValues[0], function(vertex) {
                        var ontologyProperty = propertiesByTitle[V.propName(name)],
                            propRaw = V.propRaw(vertex, name, undefined, { defaultValue: ' ' });

                        if (_.isString(propRaw)) {
                            propRaw = propRaw.trim();
                        }

                        if (ontologyProperty) {
                            if (ontologyProperty.dependentPropertyIris) {
                                propRaw = V.prop(vertex, name, undefined, { defaultValue: ' ' });
                            }
                            switch (ontologyProperty.dataType) {
                                case 'string':
                                    return propRaw.toLowerCase();

                                case 'boolean':
                                    return propRaw === true ? 1 : -1;

                                case 'date':
                                case 'integer':
                                case 'currency':
                                case 'number':
                                case 'double':
                                    return propRaw
                            }
                        }
                        return propRaw;
                    });
                if (options && options.order === 'DESC') {
                    sorted.reverse();
                }
                return sorted.concat(sortedNoValue);
            },

            propName: function(name) {
                var autoExpandedName = (/^http:\/\/visallo.org/).test(name) ?
                        name : ('http://visallo.org#' + name),
                    ontologyProperty = propertiesByTitle[name] || propertiesByTitle[autoExpandedName],

                    resolvedName = ontologyProperty && (
                        ontologyProperty.title === name ? name : autoExpandedName
                    ) || name;

                return resolvedName;
            },

            /**
             * Get the longest property value, optionally for
             * given propertyName only.
             *
             * @param {object} vertex
             * @param {string} [optionalName=] Property name
             * @returns {object} The property
             */
            longestProp: function(vertex, optionalName) {
                var properties = vertex.properties
                    .filter(function(a) {
                        var ontologyProperty = propertiesByTitle[a.name];
                        if (optionalName && optionalName !== a.name) {
                            return false;
                        }
                        return ontologyProperty && ontologyProperty.userVisible;
                    })
                    .map(function(a) {
                        var parentProperty = propertiesByDependentToCompound[a.name];
                        if (parentProperty && V.hasProperty(vertex, parentProperty)) {
                            return V.prop(vertex, parentProperty, a.key);
                        }
                        return V.prop(vertex, a.name, a.key);
                    })
                    .sort(function(a, b) {
                        return b.length - a.length;
                    });
                if (properties.length > 0) {
                    return properties[0];
                }
            },

            rollup: function(name, values) {
                name = V.propName(name);
                var ontologyProperty = propertiesByTitle[name],
                    min = Number.MAX_VALUE,
                    max = Number.MIN_VALUE,
                    sum = 0;

                if (ontologyProperty) {
                    switch (ontologyProperty.dataType) {
                        case 'date':
                            values.forEach(function(v) {
                                min = Math.min(v, min);
                                max = Math.max(v, max);
                                sum += v;
                            })

                            return {
                                span: F.date.relativeToDate(min, max),
                                average: F.date.dateString(sum / (values.length || 1))
                            }
                        case 'double':
                        case 'integer':
                        case 'currency':
                        case 'number':
                            sum = _.reduce(values, function(m, v) {
                                return m + v;
                            });
                            return {
                                sum: F.number.pretty(sum),
                                average: F.number.pretty(sum / (values.length || 1))
                            };
                    }
                }

                return {};
            },

            /**
             * Given a property name and value, convert to a displayable
             * value for a user.
             *
             * For the raw property value use
             * {@link module:util/vertex/formatters.vertex.propRaw|propRaw}.
             *
             * @param {string} name The property name IRI
             * @param {object} value The value
             * @param {object} [options=] Additional string transforms. Any
             * function defined in {@link module:util/vertex/formatters.string}
             * as the key and boolean if active
             * @returns {string} display value
             * @example
             * F.vertex.propDisplay(name, value, {
             *     prettyPrint: true
             * })
             */
            propDisplay: function(name, value, options) {
                name = V.propName(name);
                var ontologyProperty = propertiesByTitle[name];

                if (!ontologyProperty) {
                    return value;
                }

                if (ontologyProperty.possibleValues) {
                    var foundPossibleValue = ontologyProperty.possibleValues[value];
                    if (foundPossibleValue) {
                        return foundPossibleValue;
                    } else {
                        console.warn('Unknown ontology value for key', value, ontologyProperty);
                    }
                }

                if (ontologyProperty.displayType) {
                    switch (ontologyProperty.displayType) {
                        case 'phoneNumber': return F.string.phoneNumber(value);
                        case 'ssn': return F.string.ssn(value);
                        case 'byte':
                        case 'bytes': return F.bytes.pretty(value);
                        case 'heading': return F.number.heading(value);
                        case 'duration' : return F.number.duration(value);
                    }
                }

                switch (ontologyProperty.dataType) {
                    case 'boolean': return F.boolean.pretty(value);

                    case 'date': {
                        if (ontologyProperty.displayType !== 'dateOnly') {
                            return F.date.dateTimeString(value);
                        }
                        return F.date.dateStringUtc(value);
                    }

                    case 'double':
                    case 'integer':
                    case 'currency':
                    case 'number': return F.number.pretty(value);
                    case 'geoLocation': return F.geoLocation.pretty(value);

                    default:

                        if (options && _.isObject(options)) {
                            return _.reduce(options, function(val, enabled, transformName) {
                                if (enabled === true &&
                                    transformName in F.string &&
                                    _.isFunction(F.string[transformName])) {
                                    return F.string[transformName](val);
                                }
                                return val;
                            }, value)
                        }
                        return value;
                }
            },

            /**
             * Get the first property value transformed for display
             * using {@link module:util/vertex/formatters.vertex.propDisplay|propDisplay}
             *
             * @param {object} vertex
             * @param {string} name Property IRI
             * @param {string} [key=] Property key, if not given the first is
             * returned
             * @param {object} [optionalOpts=] Options
             * @param {object} [optionalOpts.ignoreDisplayFormula] Ignore any
             * formula defined in ontology
             */
            prop: function(vertex, name, optionalKey, optionalOpts) {
                checkVertexAndPropertyNameArguments(vertex, name);

                if (_.isObject(optionalKey)) {
                    optionalOpts = optionalKey;
                    optionalKey = null;
                }

                name = V.propName(name);

                var value = V.propRaw(vertex, name, optionalKey, optionalOpts),
                    ignoreDisplayFormula = optionalOpts && optionalOpts.ignoreDisplayFormula,
                    ontologyProperty = propertiesByTitle[name];

                if (!ontologyProperty) {
                    return value;
                }

                if (_.isArray(value)) {
                    if (!optionalKey) {
                        var firstMatchingProperty = _.find(vertex.properties, function(p) {
                            return ~ontologyProperty.dependentPropertyIris.indexOf(p.name);
                        });
                        optionalKey = (firstMatchingProperty && firstMatchingProperty.key);
                    }
                    if (ontologyProperty.displayFormula) {
                        return formula(ontologyProperty.displayFormula, vertex, V, optionalKey);
                    } else {
                        var dependentIris = ontologyProperty && ontologyProperty.dependentPropertyIris || [];
                        if (dependentIris.length) {
                            return _.map(
                                dependentIris,
                                _.partial(V.prop, vertex, _, optionalKey, optionalOpts)
                            ).join(' ');
                        } else {
                            return value.join(' ');
                        }
                    }
                }

                if (!ignoreDisplayFormula && ontologyProperty.displayFormula) {
                    return formula(ontologyProperty.displayFormula, vertex, V, optionalKey, optionalOpts);
                }

                return V.propDisplay(name, value, optionalOpts);
            },

            /**
             * Return list of all matching properties.
             *
             * @param {object} element
             * @param {string} name Property IRI
             * @param {string} [key=] Property key
             * @returns {Array.<object>} All properties matching
             */
            props: function(vertex, name, optionalKey) {
                checkVertexAndPropertyNameArguments(vertex, name);

                var hasKey = !_.isUndefined(optionalKey);

                if (arguments.length === 3 && !hasKey) {
                    throw new Error('Undefined key is not allowed. Remove parameter to return all named properties');
                }

                name = V.propName(name);

                var ontologyProperty = ontology.properties.byTitle[name],
                    dependentIris = ontologyProperty && ontologyProperty.dependentPropertyIris,
                    foundProperties = _.filter(vertex.properties, function(p) {
                        if (dependentIris) {
                            return ~dependentIris.indexOf(p.name) && (
                                hasKey ? optionalKey === p.key : true
                            );
                        }

                        return name === p.name && (
                            hasKey ? optionalKey === p.key : true
                        );
                    });

                if (name === 'http://visallo.org#visibilityJson' && foundProperties.length === 0) {
                    // Protect against no visibility, just set to empty
                    return [{
                        key: '',
                        sandboxStatus: 'PUBLIC',
                        name: 'http://visallo.org#visibilityJson',
                        metadata: {},
                        value: {
                            source: ''
                        }
                    }];
                }

                return foundProperties;
            },

            singlePropValid: function(value, propertyName, propertyKey) {
                var property = {
                        name: propertyName,
                        key: propertyKey,
                        value: value
                    },
                    vertex = {
                        id: 'singlePropValid',
                        properties: [property]
                    },
                    ontologyProperty = ontology.properties.byTitle[propertyName],
                    formulaString = ontologyProperty.validationFormula,
                    result = true;
                if (formulaString) {
                    result = formula(formulaString, vertex, V, propertyKey);
                }
                return Boolean(result);
            },

            propValid: function(vertex, values, propertyName, propertyKey) {
                checkVertexAndPropertyNameArguments(vertex, propertyName);
                if (!_.isArray(values)) {
                    throw new Error('Unable to validate without values array')
                }

                var ontologyProperty = ontology.properties.byTitle[propertyName],
                    dependentIris = ontologyProperty.dependentPropertyIris,
                    formulaString = ontologyProperty.validationFormula,
                    result,
                    isEveryPropertyValid = function(vertex) {
                        return _.every(vertex.properties, function(property) {
                            return V.singlePropValid(property.value, property.name, property.key);
                        });
                    };

                if (values.length) {
                    var properties = [];
                    if (dependentIris) {
                        dependentIris.forEach(function(iri, i) {
                            var property = _.findWhere(vertex.properties, {
                                    name: iri,
                                    key: propertyKey
                                }),
                                value = _.isArray(values[i]) && values[i].length === 1 ? values[i][0] : values[i];

                            if (property) {
                                property = _.extend({}, property, { value: value });
                                if (_.isUndefined(values[i])) {
                                    property.value = undefined;
                                }
                            } else {
                                property = {
                                    name: iri,
                                    key: propertyKey,
                                    value: value
                                };
                            }
                            properties.push(property);
                        })
                    }
                    vertex = _.extend({}, vertex, { properties: properties });
                }

                result = isEveryPropertyValid(vertex) &&
                    (formulaString ? Boolean(formula(formulaString, vertex, V, propertyKey)) : true);
                return Boolean(result);
            },

            /**
             * Get the `title` of the element, using order:
             *
             * 1. Use the title formula in the ontology
             * 2. Use the `title` property
             *
             * @param {object} vertex
             * @returns {string} The display title for vertex
             */
            title: function(vertex, accessedPropertyNames) {
                var title = formulaResultForElement(vertex, 'titleFormula', undefined, accessedPropertyNames)

                if (!title) {
                    title = V.prop(vertex, 'title', undefined, {
                        ignoreErrorIfTitle: true
                    });
                }

                return title;
            },

            /**
             * Get the `subtitle` of the element, using subtitle formula
             *
             * @function
             * @param {object} vertex
             * @returns {string} The display subtitle for vertex
             */
            subtitle: _.partial(formulaResultForElement, _, 'subtitleFormula', ''),

            /**
             * Get the `time` of the element, using subtitle formula.
             *
             * Nothing enforces that this is time value. Just returns
             * a string.
             *
             * @function
             * @param {object} vertex
             * @returns {string} The display time for vertex
             */
            time: _.partial(formulaResultForElement, _, 'timeFormula', ''),

            heading: function(vertex) {
                var headingProp = _.find(vertex.properties, function(p) {
                  return p.name.indexOf('heading') > 0;
                });
                if (headingProp) {
                    return headingProp.value;
                }
                return 0;
            },

            /**
             * Get the first property value as raw value. For a transformed
             * value use {@link module:util/vertex/formatters.vertex.prop|prop}
             *
             * @param {object} vertex
             * @param {string} name Property IRI
             * @param {string} [key=] Property key, if not given the first is returned
             * @param {object} [options=] Options
             * @param {object} options.defaultValue=undefined] If no value
             * @returns {object} The raw property value
             */
            propRaw: function(vertex, name, optionalKey, optionalOpts) {
                checkVertexAndPropertyNameArguments(vertex, name);

                if (_.isObject(optionalKey)) {
                    optionalOpts = optionalKey;
                    optionalKey = null;
                }

                var hasKey = !_.isUndefined(optionalKey),
                    options = _.extend({
                        defaultValue: undefined,
                        ignoreErrorIfTitle: false
                    }, optionalOpts || {});

                if (options.ignoreErrorIfTitle !== true && name === 'title') {
                    throw new Error('Use title function, not generic prop');
                }

                name = V.propName(name);

                var ontologyProperty = propertiesByTitle[name],
                    dependentIris = ontologyProperty && ontologyProperty.dependentPropertyIris || [],
                    iris = dependentIris.length ? dependentIris : [name],
                    properties = transformMatchingVertexProperties(vertex, iris);

                if (dependentIris.length) {
                    if (options.throwErrorIfCompoundProperty) {
                        throw new Error('Compound properties that depend on compound properties are not allowed');
                    }

                    if (!hasKey && properties.length) {
                        optionalKey = properties[0].key;
                    }

                    options.throwErrorIfCompoundProperty = true;

                    return _.map(dependentIris, _.partial(V.propRaw, vertex, _, optionalKey, options));
                } else {
                    var firstFoundProp = properties[0];
                    if (hasKey) {
                        firstFoundProp = _.findWhere(properties, { key: optionalKey });
                    }

                    var hasValue = firstFoundProp && !_.isUndefined(firstFoundProp.value);

                    if (!hasValue &&
                        name !== 'http://visallo.org#title' &&
                        _.isUndefined(options.defaultValue)) {
                        return undefined;
                    }

                    return hasValue ? firstFoundProp.value :
                        (
                            options.defaultValue ||
                            i18n('vertex.property.not_available',
                                (ontologyProperty && ontologyProperty.displayName || '').toLowerCase() || name)
                        )
                }
            },

            /**
             * Check if the element is vertex
             *
             * @param {object} element
             * @returns {boolean} If element is a vertex
             */
            isVertex: function(vertex) { return vertex && vertex.type && vertex.type === 'vertex'; },

            /**
             * Check if the element is edge
             *
             * @param {object} element
             * @returns {boolean} If element is a edge
             */
            isEdge: function(vertex) { return vertex && vertex.type && vertex.type === 'edge'; },

            isArtifact: function(vertex) {
                return _.contains(_.pluck(vertex.properties, 'name'), V.propName('raw'));
            },

            displayType: function(vertex) {
                if (!V.isArtifact(vertex)) {
                    return V.isEdge(vertex) ? 'edge' : 'entity';
                }

                var propNames = _.pluck(vertex.properties, 'name');
                if (_.some(propNames, function(propName) { return propName.indexOf('http://visallo.org#video-') === 0; })) {
                    return 'video';
                } else if (_.some(propNames, function(propName) { return propName.indexOf('http://visallo.org#audio-') === 0; })) {
                    return 'audio';
                } else {
                    var rawProp = V.props(vertex, V.propName('raw')),
                        rawPropMimeType = rawProp && rawProp.length && rawProp[0].metadata && rawProp[0].metadata[V.propName('mimeType')];
                    if (rawPropMimeType && rawPropMimeType.indexOf('image/') === 0) {
                        return 'image';
                    } else {
                        return 'document';
                    }
                }
            }
        };

    var E = {
        title: V.title,
        subtitle: V.subtitle,
        time: V.time
    };

    // Legacy
    V.properties.byte = V.properties.bytes;

    return $.extend({}, F, { vertex: V, vertexUrl: vertexUrl.vertexUrl, edge: E });

    function treeLookupForConceptProperty(conceptId, propertyName, additionalScope) {
        var ontologyConcept = conceptId && ontology.concepts.byId[conceptId],
            formulaString = ontologyConcept && ontologyConcept[propertyName];

        if (ontologyConcept && !additionalScope.ontology) {
            additionalScope.ontology = ontologyConcept
        }

        if (formulaString) {
            return formulaString;
        }

        if (ontologyConcept && ontologyConcept.parentConcept) {
            return treeLookupForConceptProperty(ontologyConcept.parentConcept, propertyName, additionalScope);
        }
    }

    function formulaResultForElement(vertexOrEdge, formulaKey, defaultValue, accessedPropertyNames) {
        var isEdge = V.isEdge(vertexOrEdge),
            isVertex = V.isVertex(vertexOrEdge),
            result = defaultValue,
            formulaString,
            additionalScope = {};

        if (isEdge) {
            var edge = vertexOrEdge,
                ontologyRelation = ontology.relationships.byTitle[edge.label],
                label = ontologyRelation.displayName;
            additionalScope.label = label;
            additionalScope.ontology = ontologyRelation;
            formulaString = ontologyRelation[formulaKey];
        } else if (isVertex) {
            var vertex = vertexOrEdge,
                conceptId = V.prop(vertex, 'conceptType');
            formulaString = treeLookupForConceptProperty(conceptId, formulaKey, additionalScope);
        } else {
            if (formulaKey === 'titleFormula') {
                return i18n('element.unauthorized').toUpperCase();
            } else {
                return '';
            }
        }

        if (formulaString) {
            var capture = function(fn, vertex, name) {
                    var result = fn.apply(this, _.rest(arguments))
                    if (_.isArray(accessedPropertyNames) &&
                        (!_.isUndefined(result) || (_.isString(result) && result))) {
                        accessedPropertyNames.push(name);
                    }
                    return result;
                };
            result = formula(formulaString, vertexOrEdge, {
                prop: _.wrap(V.prop, capture),
                propRaw: _.wrap(V.propRaw, capture),
                longestProp: _.wrap(V.longestProp, capture),
                isEdge: V.isEdge
            }, undefined, { additionalScope: additionalScope });
        }

        return result;
    }

    function transformMatchingVertexProperties(vertex, propertyNames) {
        var CONFIDENCE = 'http://visallo.org#confidence';

        return vertex.properties.filter(function(p) {
            return _.contains(propertyNames, p.name);
        })
            // Use native sort for performance
            .sort(function(p1, p2) {
                const c1 = p1.metadata && p1.metadata[CONFIDENCE];
                const c2 = p2.metadata && p2.metadata[CONFIDENCE];
                const p1HasCon = !_.isUndefined(c1);
                const p2HasCon = !_.isUndefined(c2);
                const compareConf = (p1HasCon && p2HasCon) ? (c2 - c1) :
                    p1HasCon ? -1 :
                    p2HasCon ? 1 : 0;

                if (compareConf === 0) {
                    var v1 = V.propDisplay(p1.name, p1.value);
                    var v2 = V.propDisplay(p2.name, p2.value);
                    if (_.isString(v1) && _.isString(v2)) {
                        return v1.toLowerCase().localeCompare(v2.toLowerCase());
                    }
                    return 0;
                }

                return compareConf;
            })
    }

    function checkVertexAndPropertyNameArguments(vertex, propertyName) {
        if (!vertex || !vertex.id || !_.isArray(vertex.properties)) {
            throw new Error('Vertex is invalid', vertex);
        }
        if (!propertyName || !_.isString(propertyName)) {
            throw new Error('Property name is invalid', propertyName);
        }
    }
});
