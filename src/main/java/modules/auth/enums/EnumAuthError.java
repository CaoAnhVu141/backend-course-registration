package modules.auth.enums;

import lombok.Getter;

@Getter
public enum EnumAuthError {

    INVALID_CREDENTIALS("Tài khoản hoặc mật khẩu không hợp lệ"),
    INVALID_TOKEN("Refresh token không hợp lệ"),
    EXPIRED_TOKEN("Refresh token đã hết hạn"),
    REVOKED_TOKEN("Refresh token đã bị thu hồi"),
    INVALID_ACCESS_TOKEN("Access token không hợp lệ hoặc đã hết hạn"),
    FORBIDDEN("Bạn không có quyền truy cập tài nguyên này"),
    USER_NOT_FOUND("Không tìm thấy người dùng");

    public final String message;

    EnumAuthError(String message) {
        this.message = message;
    }

}
