package modules.submissions.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateSubmissionVersionRequest {

    @NotNull(message = "Đăng kí đề tài không được để trống")
    @Positive(message = "Đăng kí đề tài không hợp lệ")
    private Long topicRegistrationId;

    @NotBlank(message = "File không được để trống")
    private String fileName;

    @NotBlank(message = "Đường dẫn file không được để trống")
    private String storageKey;

    @NotBlank(message = "Mime type không được để trống")
    private String mimeType;

    @NotBlank(message = "Kích thước file không được để trống")
    @Positive(message = "Kích thước file không hợp lệ")
    private String fileSize;

    private String changeNote;

}
