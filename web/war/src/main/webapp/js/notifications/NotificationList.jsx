define([
    'react',
    'configuration/plugins/registry',
    'components/Attacher'
], function(React, registry, Attacher) {
    'use strict';

    const { PropTypes } = React;
    const { CSSTransitionGroup } = React.addons;

    registry.documentExtensionPoint('com.visallo.notification.display',
        'Customize the displays of notification types',
        function(e) {
            return e.componentPath;
        },
        'http://docs.visallo.org/extension-points/front-end/detailToolbar' //TODO: documentation url
    );

    let displayExtensions = registry.extensionsForPoint('com.visallo.notification.display');
    const DEFAULT_COMPONENT_PATH = 'notifications/Notification';

    const NotificationList = ({ notifications, onDismissClick, allowSystemDismiss, animated, ...props}) => (
            <CSSTransitionGroup
                component="div"
                transitionName="notification"
                transitionAppear={animated}
                transitionEnter={animated}
                transitionLeave={animated}
                transitionAppearTimeout={750}
                transitionEnterTimeout={750}
                transitionLeaveTimeout={500}
            >
                {notifications.map((notification) => {
                    const { type, messageType, id } = notification;
                    const canDismiss = allowSystemDismiss || notification.type.toLowerCase() !== 'system';
                    const notificationProps = {
                        key: id,
                        notification: notification,
                        onDismissClick: (canDismiss ? onDismissClick : undefined),
                        ...props
                    };

                    return (
                        <Attacher componentPath={getComponentPath(type, messageType)} {...notificationProps} />
                    );
                })}
            </CSSTransitionGroup>
    );

    NotificationList.propTypes = {
        notifications: PropTypes.array,
        onDismissClick: PropTypes.func,
        allowSystemDismiss: PropTypes.bool,
        animated: PropTypes.bool
    };

    return NotificationList;

    function getComponentPath(type, messageType) {
        const extension = displayExtensions.find((e) => e.canHandle(type, messageType));
        if (extension) {
            return extension.componentPath;
        } else {
            return DEFAULT_COMPONENT_PATH;
        }
    }
});
