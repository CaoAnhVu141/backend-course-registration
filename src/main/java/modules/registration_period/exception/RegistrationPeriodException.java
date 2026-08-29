package modules.registration_period.exception;

import modules.registrationperiods.enums.EnumRegistrationPeriodError;

public class RegistrationPeriodException extends RuntimeException {

    public RegistrationPeriodException(EnumRegistrationPeriodError error) {
        super(error.getMessage());
    }
}
