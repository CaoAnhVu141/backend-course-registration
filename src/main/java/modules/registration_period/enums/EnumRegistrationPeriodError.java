package modules.registration_period.enums;

import lombok.Getter;

@Getter
public enum EnumRegistrationPeriodError {

    REGISTRATION_PERIOD_NAME_EXISTS("Tên kỳ đăng ký đã tồn tại"),
    REGISTRATION_PERIOD_NOT_FOUND("Kỳ đăng ký không tồn tại"),
    INVALID_LECTURER_REGISTRATION_TIME("Thời gian giảng viên đăng ký không hợp lệ"),
    INVALID_STUDENT_REGISTRATION_TIME("Thời gian sinh viên đăng ký không hợp lệ"),
    INVALID_REVIEWER_SCORE_DEADLINE("Deadline chấm phản biện phải sau thời gian sinh viên đăng ký"),
    INVALID_DEFENSE_DATE("Ngày bảo vệ phải sau deadline chấm phản biện"),
    REVIEWER_SCORE_DEADLINE_REQUIRED("Loại TLCN/KLTN bắt buộc có deadline chấm phản biện"),
    REVIEWER_SCORE_DEADLINE_NOT_ALLOWED("Loại này không được thiết lập deadline chấm phản biện"),
    DEFENSE_DATE_REQUIRED("Loại KLTN bắt buộc có ngày báo cáo hội đồng"),
    DEFENSE_DATE_NOT_ALLOWED("Loại này không được thiết lập ngày báo cáo hội đồng");

    private final String message;

    EnumRegistrationPeriodError(String message) {
        this.message = message;
    }
}
