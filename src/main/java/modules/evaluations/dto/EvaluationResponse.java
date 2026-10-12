package modules.evaluations.dto;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;
import modules.evaluations.enums.EvaluationStatus;

@Getter
@Builder
public class EvaluationResponse {

    private Long id;
    private Long assignmentId;
    private Long lecturerUserId;
    private String lecturerCode;
    private String lecturerEmail;

    private Double score;
    private String comment;
    private EvaluationStatus status;
    private LocalDateTime submittedAt;
}
