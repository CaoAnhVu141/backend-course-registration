package modules.notifications.enums;

import lombok.Getter;

@Getter
public enum EnumNotificationError {

    NOTIFICATION_NOT_FOUND("Thông báo không tồn tại"),
    NOT_NOTIFICATION_OWNER("Chỉ được thao tác trên thông báo của mình"),
    NO_RECIPIENTS("Không có người nhận phù hợp"),
    USER_NOT_FOUND("Người dùng không tồn tại");

    private final String message;

    EnumNotificationError(String message) {
        this.message = message;
    }
}