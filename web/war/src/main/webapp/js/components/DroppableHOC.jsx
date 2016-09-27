define([
    'react',
    'react-dom'
], function(React, ReactDom) {
    'use strict';

    const Events = 'dragover dragenter dragleave drop'.split(' ');
    const { PropTypes } = React;
    const DroppableHOC = WrappedComponent => React.createClass({
        displayName: `DroppableHOC(${WrappedComponent.displayName || 'Component'})`,
        propTypes: {
            mimeTypes: PropTypes.arrayOf(PropTypes.string).isRequired,
            onDrop: PropTypes.func.isRequired,
            style: PropTypes.object
        },
        getInitialState() {
            return { cls: '' }
        },
        componentDidMount() {
            this.refs.div = ReactDom.findDOMNode(this.refs.div);
            Events.forEach(event => {
                if (event in this) {
                    this.refs.div.addEventListener(event, this[event], false)
                } else console.error('No handler for event: ' + event);
            })
        },
        componentWillUnmount() {
            Events.forEach(event => {
                if (event in this) {
                    this.refs.div.removeEventListener(event, this[event])
                } else console.error('No handler for event: ' + event);
            })
        },
        dataTransferHasValidMimeType(dataTransfer) {
            return _.any(dataTransfer.types, type => this.props.mimeTypes.includes(type));
        },
        dragover(event) {
            const { dataTransfer } = event;
            if (this.dataTransferHasValidMimeType(dataTransfer)) {
                event.preventDefault();
            }
        },
        dragenter(event) {
            if (this.dataTransferHasValidMimeType(event.dataTransfer)) {
                this.toggleClass(true);
            }
        },
        dragleave(event) {
            this.toggleClass(false);
        },
        drop(event) {
            this.toggleClass(false);

            const { clientX, clientY } = event;
            const box = event.target.getBoundingClientRect();
            const positionTransform = this.refs.wrapped.droppableTransformPosition || _.identity;
            const position = positionTransform({
                x: clientX - box.left,
                y: clientY - box.top
            });

            this.props.onDrop(event, position);
        },
        toggleClass(toggle) {
            this.setState({ cls: toggle ? 'accepts-draggable' : '' })
        },
        render() {
            const { cls } = this.state;
            const { onDrop, mimeTypes, style = {}, ...props} = this.props;
            return (
                <div ref="div" style={style} className={cls}><WrappedComponent ref="wrapped" {...props} /></div>
            )
        }
    });

    return DroppableHOC
});
