package modules.expertisetags.exception;

import modules.expertisetags.enums.EnumExpertiseTagError;

public class ExpertiseTagException extends RuntimeException {

    public ExpertiseTagException(EnumExpertiseTagError error) {
        super(error.getMessage());
    }
}