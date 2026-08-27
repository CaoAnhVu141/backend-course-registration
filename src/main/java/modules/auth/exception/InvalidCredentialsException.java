package modules.auth.exception;

public class InvalidCredentialsException extends RuntimeException {
    public InvalidCredentialsException(String message) {
        super("Tài khoản hoặc mật khẩu không hợp lệ");
    }

}
