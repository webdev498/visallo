define([
    'react',
    'react-redux'
], function(React, redux) {
    'use strict';

    const TestStore = React.createClass({
        render() {
            console.log('Prop Test = ', this.props.test)
            return null;
        }
    });

    return redux.connect(function(store) {
        console.log('store=', store)
        return {
            test: store.test
        }
    }, function(dispatch) {
        return {
            onTest: (id) => {
                console.log('dispatching')
                dispatch({
                    type: 'TEST',
                    id: id,
                    text: 'test action',
                    meta: { WebWorker: true }
                })
            }
        }
    })(TestStore);
});
