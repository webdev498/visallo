define([
    'util/withDataRequest'
], function(withDataRequest) {
    'use strict';
    console.log("inmessagespromise", arguments);
    return withDataRequest.dataRequest('config', 'messages')
});
