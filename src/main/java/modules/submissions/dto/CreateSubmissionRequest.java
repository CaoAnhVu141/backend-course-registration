package modules.submissions.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateSubmissionRequest {
    @NotNull(message = "Đăng kí đề tài không được để trống")
    @Positive(message = "Đăng kí đề tài không hợp lệ")
    private Long topicRegistrationId;
}
