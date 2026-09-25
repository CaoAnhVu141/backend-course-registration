package modules.committee_topic_assignments.enums;

import lombok.Getter;

@Getter
public enum EnumCommitteeTopicAssignmentSuccess {

    CREATED("Phân công đề tài cho hội đồng thành công"),
    SCHEDULE_UPDATED("Cập nhật lịch bảo vệ thành công"),
    COMPLETED("Xác nhận hoàn thành bảo vệ thành công"),
    CANCELLED("Hủy phân công thành công"),
    GET_ALL("Lấy danh sách phân công thành công"),
    GET_DETAIL("Lấy thông tin phân công thành công");

    private final String message;

    EnumCommitteeTopicAssignmentSuccess(String message) {
        this.message = message;
    }
}
