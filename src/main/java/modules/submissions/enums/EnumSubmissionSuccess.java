package modules.submissions.enums;

import lombok.Getter;

@Getter
public enum EnumSubmissionSuccess {
    CREATED("Tạo bài nộp thành công"),
    VERSION_CREATED("Nộp phiên bản báo cáo thành công"),
    GET_ALL("Lấy danh sách bài nộp thành công"),
    GET_DETAIL("Lấy thông tin bài nộp thành công");

    private final String message;

    EnumSubmissionSuccess(String message) {
        this.message = message;
    }
}
