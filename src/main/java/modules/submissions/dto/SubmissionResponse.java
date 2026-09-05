package modules.submissions.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class SubmissionResponse {

    private Long id;
    private Long topicRegistrationId;
    private boolean status;
    private LocalDateTime submittedAt;
    private List<SubmissionVersionResponse> submissionVersions;
}
