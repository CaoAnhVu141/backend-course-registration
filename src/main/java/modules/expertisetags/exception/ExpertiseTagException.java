package modules.expertisetags.exception;

import modules.expertisetags.enums.EnumExpertiseTagError;

public class ExpertiseTagException extends RuntimeException {

    private final EnumExpertiseTagError error;

    public ExpertiseTagException(EnumExpertiseTagError error) {
        supper(error.getMessage());
    }
}
