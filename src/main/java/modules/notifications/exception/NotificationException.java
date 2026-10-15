package modules.notifications.exception;

import modules.notifications.enums.EnumNotificationError;

public class NotificationException extends RuntimeException {

    public NotificationException(EnumNotificationError error) {
        super(error.getMessage());
    }
}
