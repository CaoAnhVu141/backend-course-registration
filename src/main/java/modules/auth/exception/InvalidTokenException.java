package modules.auth.exception;

import modules.auth.enums.EnumAuthError;

public class InvalidTokenException extends RuntimeException {

    public InvalidTokenException(EnumAuthError error) {
        super(error.getMessage());
    }
}