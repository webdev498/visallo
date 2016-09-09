define([
    'react',
    'react-redux'
], function(React, redux) {
    'use strict';

    const TestStore = React.createClass({
        componentWillMount() {
            setTimeout(() => { this.props.onTest() }, 2000)
        },
        render() {
            console.log('Prop Store is Dashboard Items?', this.props.keys)
            return null;
        }
    });

    return redux.connect(function(store) {
        return {
            keys: store.dashboard.x
        }
    }, function(dispatch) {
        return {
            onTest: (id) => {
                dispatch({
                    type: 'INC'
                })
            }
        }
    })(TestStore);
});
