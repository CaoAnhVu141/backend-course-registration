package modules.topics.enums;

import lombok.Getter;

@Getter
public enum EnumTopicError {

    TOPIC_NOT_FOUND("Đề tài không tồn tại"),
    DEPARTMENT_NOT_FOUND("Khoa/phòng ban không tồn tại"),
    REGISTRATION_PERIOD_NOT_FOUND("Kỳ đăng ký không tồn tại"),
    LECTURER_NOT_FOUND("Giảng viên không tồn tại"),
    INVALID_TOPIC_STATUS("Trạng thái đề tài không hợp lệ"),
    TOPIC_ALREADY_REJECTED("Đề tài đã bị từ chối"),
    TOPIC_ALREADY_PUBLISHED("Đề tài đã được công bố"),
    ONLY_PENDING_TOPIC_CAN_BE_REJECTED("Chỉ đề tài đang chờ duyệt mới được từ chối"),
    ONLY_PENDING_TOPIC_CAN_BE_APPROVED("Chỉ đề tài đang chờ duyệt mới được chấp nhận"),
    LECTURER_REGISTRATION_NOT_OPEN("Chưa đến thời gian hoặc đã hết hạn giảng viên đăng ký đề tài"),
    REGISTRATION_PERIOD_INACTIVE("Kỳ đăng ký không hoạt động");

    private final String message;

    EnumTopicError(String message) {
        this.message = message;
    }
}
