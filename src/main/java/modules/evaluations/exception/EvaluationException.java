package modules.evaluations.exception;

import modules.evaluations.enums.EnumEvaluationError;

public class EvaluationException extends RuntimeException {

    public EvaluationException(EnumEvaluationError error) {
        super(error.getMessage());
    }
}
