package modules.auth.exception;

import modules.auth.enums.EnumAuthError;

public class InvalidCredentialsException extends RuntimeException {

    public InvalidCredentialsException() {
        super(EnumAuthError.INVALID_CREDENTIALS.getMessage());
    }
}