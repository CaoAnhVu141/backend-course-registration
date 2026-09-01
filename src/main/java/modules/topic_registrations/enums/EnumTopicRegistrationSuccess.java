package modules.topic_registrations.enums;

import lombok.Getter;

@Getter
public enum EnumTopicRegistrationSuccess {

    CREATED("Đăng ký đề tài thành công"),
    APPROVED("Duyệt đăng ký đề tài thành công"),
    REJECTED("Từ chối đăng ký đề tài thành công"),
    CANCELLED("Hủy đăng ký đề tài thành công"),
    GET_ALL("Lấy danh sách đăng ký đề tài thành công"),
    GET_DETAIL("Lấy thông tin đăng ký đề tài thành công");

    private final String message;

    EnumTopicRegistrationSuccess(String message) {
        this.message = message;
    }
}
