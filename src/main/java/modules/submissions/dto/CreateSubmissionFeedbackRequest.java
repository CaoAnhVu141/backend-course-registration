package modules.submissions.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateSubmissionFeedbackRequest {
    @NotNull(message = "Phiên bản bài nộp không được để trống")
    @Positive(message = "Phiên bản bài nộp không hợp lệ")
    private Long submissionVersionId;

    @NotBlank(message = "Nội dung góp ý không được để trống")
    private String comment;
}
