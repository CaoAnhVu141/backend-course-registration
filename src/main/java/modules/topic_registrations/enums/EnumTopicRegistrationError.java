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
    INVALID_TOPIC_REGISTRATION_STATUS("Trạng thái đăng ký đề tài không hợp lệ"),
    NOT_REGISTRATION_OWNER("Bạn không có quyền thao tác đăng ký đề tài này"),
    REGISTRATION_PERIOD_NOT_FOUND("Kỳ đăng ký không tồn tại"),
    TOPIC_NOT_IN_REGISTRATION_PERIOD("Đề tài không thuộc kỳ đăng ký của nhóm"),
    STUDENT_REGISTRATION_NOT_OPEN("Chưa đến thời gian hoặc đã hết hạn đăng ký đề tài");

    private final String message;

    EnumTopicRegistrationError(String message) {
        this.message = message;
    }
}
