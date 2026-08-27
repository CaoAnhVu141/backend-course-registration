package modules.auth.exception;

public class InvalidCredentialsException extends RuntimeException {

    public InvalidCredentialsException() {
        super("Tài khoản hoặc mật khẩu không hợp lệ");
    }
}