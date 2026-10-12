package modules.evaluations.enums;

import lombok.Getter;

@Getter
public enum EnumEvaluationSuccess {

    EVALUATED("Chấm điểm thành công"),
    RESULT_PUBLISHED("Tổng hợp và công bố thành công"),
    GET_ALL("Lấy danh sách điểm thành công"),
    GET_DETAIL("Lấy chi tiết thành công"),
    GET_RESULT("Lấy kết quả đề tài thành công");

    private final String message;

    EnumEvaluationSuccess(String message) {
        this.message = message;
    }
}
