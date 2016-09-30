define([
    'react'
], function(React) {
    'use strict';

    const PAN_INACTIVE_AREA = 8;
    const PAN_AREA_DRAG_SIZE = 75;
    const PAN_SPEED = 10;
	const PAN_MIN_PERCENT_SPEED = 0.25;
	const PAN_DISTANCE = 10;
    const STATE_PANNING = { state: 'panning' };
    const STATE_START = { state: 'panningStart' };
    const STATE_END = { state: 'panningEnd' };
    const EMPTY = { x: 0, y: 0 };

    const PropTypes = React.PropTypes;

    const NavigationControls = React.createClass({

        propTypes: {
            zoom: PropTypes.bool,
            pan: PropTypes.bool,
            tools: PropTypes.bool,
            onZoom: PropTypes.func,
            onPan: PropTypes.func
        },

        getDefaultProps() {
            const noop = () => {}
            return {
                zoom: true,
                pan: true,
                tools: false,
                onZoom: noop,
                onPan: noop
            }
        },

        getInitialState() {
            return {
                panning: false
            }
        },

        render() {
            const options = this.props.tools ?
                (
                    <div className="options">
                        <button title={i18n('controls.options.toggle')}>Options</button>
                        <div style="display:none" className="options-container"></div>
                    </div>
                ) : '',
                panningCls = 'panner' + (this.state.panning ? ' active' : '');

            return (
                <div className="controls">
                    <div className="tools">{options}</div>
                    <button onClick={this.onFit}>{i18n('controls.fit')}</button>
                    <div ref="panner" className={panningCls} onMouseDown={this.onPanMouseDown}><div className="arrow-bottom"/><div className="arrow-right"/><div className="arrow-top"/><div className="arrow-left"/></div>
                    <button
                        onMouseDown={this.onZoom}
                        onMouseUp={this.onZoom}
                        className="zoom" data-type="out">-</button>
                    <button
                        onMouseDown={this.onZoom}
                        onMouseUp={this.onZoom}
                        className="zoom" data-type="in">+</button>
                </div>
            );
        },

        onPanMouseDown(event) {
            this.props.onPan(EMPTY, STATE_START);
            this._pannerClientBounds = this.refs.panner.getBoundingClientRect();
            this._handlePanMove(event.nativeEvent);
            window.addEventListener('mousemove', this._handlePanMove, false);
            window.addEventListener('mouseup', this._handlePanUp, false);

            this.setState({ panning: true })
        },

        onFit(event) {
            console.log('fit')
        },

        onZoom(event) {
            const e = event.nativeEvent;
            const zoomType = event.target.dataset.type;
            switch (e.type) {
                case 'mousedown':
                    this.zoomTimer = setInterval(() => {
                        this.props.onZoom(zoomType);
                    }, PAN_SPEED);
                    break;
                case 'mouseup':
                    clearInterval(this.zoomTimer);
                    break;
            }
        },

        _handlePanMove(event) {
            event.preventDefault();
            event.stopPropagation();
            clearInterval(this.panInterval);

            var pan = eventToPan(this._pannerClientBounds, event);
            if (isNaN(pan.x) || isNaN(pan.y)) {
                return;
            }

            var self = this;
            this.panInterval = setInterval(() => {
                this.props.onPan(pan, STATE_PANNING);
            }, PAN_SPEED);
        },

        _handlePanUp(event) {
            this.props.onPan(EMPTY, STATE_END);
            clearInterval(this.panInterval);
            window.removeEventListener('mousemove', this._handlePanMove);
            window.removeEventListener('mouseup', this._handlePanUp);
            this.setState({ panning: false })
        }
    });

    return NavigationControls;

    // Ported from jquery.cytoscape-panzoom plugin
    function eventToPan(bounds, e) {
        var v = {
                x: Math.round(e.pageX - bounds.left - bounds.width / 2),
                y: Math.round(e.pageY - bounds.top - bounds.height / 2)
            },
            r = PAN_AREA_DRAG_SIZE,
            d = Math.sqrt(v.x * v.x + v.y * v.y),
            percent = Math.min(d / r, 1);

        if (d < PAN_INACTIVE_AREA) {
            return {
                x: NaN,
                y: NaN
            };
        }

        v = {
            x: v.x / d,
            y: v.y / d
        };

        percent = Math.max(PAN_MIN_PERCENT_SPEED, percent);

        var vnorm = {
            x: -1 * v.x * (percent * PAN_DISTANCE),
            y: -1 * v.y * (percent * PAN_DISTANCE)
        };

        return vnorm;
    }
});
