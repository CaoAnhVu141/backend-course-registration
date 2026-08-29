package modules.registration_period.enums;

import lombok.Getter;

@Getter
public enum EnumRegistrationPeriodError {

    REGISTRATION_PERIOD_NOT_FOUND("Kỳ đăng ký không tồn tại"),
    REGISTRATION_PERIOD_NAME_EXISTS("Tên kỳ đăng ký đã tồn tại"),
    INVALID_TIME_RANGE("Thời gian kết thúc phải sau thời gian bắt đầu"),
    REGISTRATION_PERIOD_OVERLAP("Thời gian kỳ đăng ký bị trùng với kỳ đăng ký khác");

    private final String message;

    EnumRegistrationPeriodError(String message) {
        this.message = message;
    }
}
