package modules.submissions.exception;

import modules.submissions.enums.EnumSubmissionError;

public class SubmissionException extends RuntimeException {

    public SubmissionException(EnumSubmissionError error) {
        super(error.getMessage());
    }
}