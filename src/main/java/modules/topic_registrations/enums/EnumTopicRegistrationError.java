package modules.topic_registrations.enums;

import lombok.Getter;

@Getter
public enum EnumTopicRegistrationError {

    TOPIC_REGISTRATION_NOT_FOUND("Đăng ký đề tài không tồn tại"),
    STUDENT_GROUP_NOT_FOUND("Nhóm sinh viên không tồn tại"),
    TOPIC_NOT_FOUND("Đề tài không tồn tại"),
    USER_NOT_FOUND("Người dùng không tồn tại"),
    TOPIC_NOT_PUBLISHED("Đề tài chưa được công bố"),
    STUDENT_GROUP_INACTIVE("Nhóm sinh viên không hoạt động"),
    STUDENT_GROUP_ALREADY_REGISTERED("Nhóm sinh viên đã đăng ký đề tài trong kỳ này"),
    TOPIC_ALREADY_APPROVED("Đề tài đã có nhóm được duyệt"),
    INVALID_TOPIC_REGISTRATION_STATUS("Trạng thái đăng ký đề tài không hợp lệ");

    private final String message;

    EnumTopicRegistrationError(String message) {
        this.message = message;
    }
}
