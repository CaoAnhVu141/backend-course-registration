package modules.notifications.enums;

import lombok.Getter;

@Getter
public enum EnumNotificationSuccess {
    SENT("Gửi thông báo thành công"),
    GET_ALL("Lấy danh sách thông báo thành công"),
    GET_UNREAD_COUNT("Lấy số thông báo chưa đọc thành công"),
    MARKED_READ("Đánh dấu đã đọc thành công");

    private final String message;

    EnumNotificationSuccess(String message) {
        this.message = message;
    }
}
