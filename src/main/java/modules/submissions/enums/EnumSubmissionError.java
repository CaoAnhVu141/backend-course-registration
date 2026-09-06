package modules.submissions.enums;

import lombok.Getter;

@Getter
public enum EnumSubmissionError {
    SUBMISSION_NOT_FOUND("Bài nộp không tồn tại"),
    SUBMISSION_ALREADY_EXISTS("Đăng ký đề tài này đã có bài nộp"),
    TOPIC_REGISTRATION_NOT_FOUND("Đăng ký đề tài không tồn tại"),
    TOPIC_REGISTRATION_NOT_APPROVED("Đăng ký đề tài chưa được duyệt"),
    USER_NOT_FOUND("Người dùng không tồn tại"),
    SUBMISSION_VERSION_NOT_FOUND("Phiên bản bài nộp không tồn tại"),
    LECTURER_NOT_FOUND("Giảng viên không tồn tại");

    private final String message;

    EnumSubmissionError(String message) {
        this.message = message;
    }
}
