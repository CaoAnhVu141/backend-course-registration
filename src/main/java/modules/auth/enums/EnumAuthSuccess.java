package modules.auth.enums;

import lombok.Getter;

@Getter
public enum EnumAuthSuccess {

    REGISTER_STUDENT("Đăng ký sinh viên thành công"),
    REGISTER_LECTURER("Đăng ký giảng viên thành công"),
    REGISTER_ADMIN("Đăng ký admin thành công"),
    LOGIN("Đăng nhập thành công"),
    REFRESH_TOKEN("Làm mới token thành công");

    private final String message;

    EnumAuthSuccess(String message) {
        this.message = message;
    }
}