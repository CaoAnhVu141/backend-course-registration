package modules.submissions.exception;

import modules.submissions.enums.EnumSubmissionError;
import modules.submissions.dto.SubmissionFeedbackResponse;
import modules.submissions.entity.SubmissionFeedback;

public class SubmissionException extends RuntimeException {

    public SubmissionException(EnumSubmissionError error) {
        super(error.getMessage());
    }
}