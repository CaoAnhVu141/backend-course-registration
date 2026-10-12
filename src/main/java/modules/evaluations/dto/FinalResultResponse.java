package modules.evaluations.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FinalResultResponse {

    private Long id;
    private Long assignmentId;

    private Long topicId;
    private String topicTitle;
    private Long studentGroupId;
    private String studentGroupName;

    private Double finalScore;
    private boolean published;
    private String publishedBy;
    private LocalDateTime publishedAt;

    // điểm của thành viên của giảng viên cho
    private List<EvaluationResponse> evaluations;
}
