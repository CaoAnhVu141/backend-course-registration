package modules.submissions.dto;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SubmissionResponse {

    private Long id;
    private Long topicRegistrationId;
    private boolean status;
    private LocalDateTime submittedAt;
    private List<SubmissionVersionResponse> versions;
}
