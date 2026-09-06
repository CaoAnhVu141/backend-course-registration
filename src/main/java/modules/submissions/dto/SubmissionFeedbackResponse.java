package modules.submissions.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SubmissionFeedbackResponse {

    private Long id;
    private Long submissionVersionId;
    private Long lecturerUserId;
    private String lecturerEmail;
    private String lecturerCode;
    private String comment;
}
