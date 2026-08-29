package modules.registration_period.enums;

import lombok.Getter;

@Getter
public class EnumRegistrationPeriodSucess {

    CREATED("Tạo kỳ đăng ký thành công"),
    UPDATED("Cập nhật kỳ đăng ký thành công"),
    DELETED("Xóa kỳ đăng ký thành công"),
    GET_ALL("Lấy danh sách kỳ đăng ký thành công"),
    GET_DETAIL("Lấy thông tin kỳ đăng ký thành công");

    private final String message;

    EnumRegistrationPeriodSuccess(String message) {
        this.message = message;
    }
}
