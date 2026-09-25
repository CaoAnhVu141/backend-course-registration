package modules.committee_topic_assignments.enums;

import lombok.Getter;

@Getter
public enum EnumCommitteeTopicAssignmentError {

    ASSIGNMENT_NOT_FOUND("Phân công hội đồng không tồn tại"),
    ASSIGNMENT_ALREADY_EXISTS("Nhóm này đã được phân công hội đồng"),
    INVALID_ASSIGNMENT_STATUS("Trạng thái phân công không hợp lệ"),

    DEFENSE_COMMITTEE_NOT_FOUND("Hội đồng không tồn tại"),
    DEFENSE_COMMITTEE_CLOSED("Hội đồng đã đóng"),
    DEFENSE_COMMITTEE_NO_CHAIRMAN("Hội đồng chưa có chủ tịch"),

    TOPIC_REGISTRATION_NOT_FOUND("Đăng ký đề tài không tồn tại"),
    TOPIC_REGISTRATION_NOT_APPROVED("Đăng ký đề tài chưa được duyệt"),

    REGISTRATION_PERIOD_NOT_FOUND("Kỳ đăng ký không tồn tại"),
    REGISTRATION_PERIOD_MISMATCH("Hội đồng và đề tài không cùng kỳ đăng ký"),

    SUPERVISOR_CONFLICT("Giảng viên hướng dẫn không được chấm đề tài mình hướng dẫn"),
    REVIEW_LOAD_EXCEEDED("Giảng viên trong hội đồng đã đạt giới hạn số đề tài chấm"),

    DEFENSE_ORDER_DUPLICATED("Thứ tự bảo vệ đã tồn tại trong hội đồng này"),
    DEFENSE_ROOM_TIME_CONFLICT("Phòng đã có lịch bảo vệ vào thời điểm này"),
    DEFENSE_TIME_INVALID("Thời gian bảo vệ không được trước ngày bảo vệ của kỳ đăng ký"),

    LECTURER_NOT_FOUND("Giảng viên không tồn tại"),
    STUDENT_NOT_FOUND("Sinh viên không tồn tại"),
    USER_NOT_FOUND("Người dùng không tồn tại");

    private final String message;

    EnumCommitteeTopicAssignmentError(String message) {
        this.message = message;
    }
}
