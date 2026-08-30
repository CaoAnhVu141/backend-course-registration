package modules.topics.enums;

import lombok.Getter;

@Getter
public enum EnumTopicSuccess {

    CREATED("Tạo đề tài thành công"),
    UPDATED("Cập nhật đề tài thành công"),
    APPROVED("Duyệt đề tài thành công"),
    REJECTED("Từ chối đề tài thành công"),
    PUBLISHED("Công bố đề tài thành công"),
    DELETED("Xóa đề tài thành công"),
    GET_ALL("Lấy danh sách đề tài thành công"),
    GET_DETAIL("Lấy thông tin đề tài thành công"),
    GET_MY_TOPICS("Lấy danh sách đề tài của tôi thành công");

    private final String message;

    EnumTopicSuccess(String message) {
        this.message = message;
    }
}
