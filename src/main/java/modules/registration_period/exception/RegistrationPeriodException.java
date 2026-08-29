package modules.registration_period.exception;

import modules.registration_period.enums.EnumRegistrationPeriodError;

public class RegistrationPeriodException extends RuntimeException {

    public RegistrationPeriodException(EnumRegistrationPeriodError error) {
        super(error.getMessage());
    }
}
