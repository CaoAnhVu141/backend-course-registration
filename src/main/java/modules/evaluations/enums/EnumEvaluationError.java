package modules.evaluations.enums;

import lombok.Getter;

@Getter
public enum EnumEvaluationError {

    LECTURER_NOT_FOUND("Giảng viên không tồn tại"),
    STUDENT_NOT_FOUND("Sinh viên không tồn tại"),
    USER_NOT_FOUND("Người dùng không tồn tại"),

    ASSIGNMENT_NOT_FOUND("Phân công hội đồng không tồn tại"),
    INVALID_ASSIGNMENT_STATUS("Trạng thái phân công không hợp lệ"),
    NOT_COMMITTEE_MEMBER("Giảng viên không thuộc hội đồng chấm đề tài này"),
    ALREADY_EVALUATED("Giảng viên đã chấm đề tài này"),
    EVALUATION_NOT_FOUND("Điểm đánh giá không tồn tại"),
    NOT_CHAIRMAN("Chỉ chủ tịch hội đồng mới được tổng hợp và công bố kết quả"),
    NO_EVALUATIONS_YET("Chưa có điểm thành phần nào để tổng hợp"),
    NOT_ALL_MEMBERS_EVALUATED("Chưa đủ tất cả thành viên hội đồng chấm điểm"),
    RESULT_ALREADY_PUBLISHED("Kết quả đã được công bố"),
    RESULT_NOT_FOUND("Chưa có kết quả cho đề tài này");

    private final String message;

    EnumEvaluationError(String message) {
        this.message = message;
    }
}
