define([
    'react',
    'classnames'
], function(
    React,
    classNames) {
    'use strict';
    const { PropTypes } = React;

    const Notification = ({ notification, onNotificationClick, onDismissClick }) => {
        const { type, messageType, severity, title, message, actionPayload } = notification;
        const notificationClass = classNames('notification', {
            critical: (/CRITICAL/i).test(severity),
            warning: (/WARNING/i).test(severity),
            informational: !severity || (/INFO/i).test(severity)
        });
        const textClass = classNames({ action: actionPayload });

        return (
            <div
                className={notificationClass}
                onClick={() => onNotificationClick(notification)}
            >
               <h1 className={textClass}>{title}</h1>
               <h2 className={textClass}>{message}</h2>
               {onDismissClick ? (
                    <button
                        onClick={(e) => {
                            e.stopPropagation();
                            onDismissClick(notification);
                        }}
                    ></button>
               ) : null}
            </div>
        );
    };

    Notification.propTypes = {
        notification: PropTypes.object.isRequired,
        onNotificationClick: PropTypes.func,
        onDismissClick: PropTypes.func
    };

    return Notification;
});
