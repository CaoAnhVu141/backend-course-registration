package modules.topic_registrations.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateTopicRegistrationRequest {

    @NotNull(message = "Nhóm sinh viên không được để trống")
    @Positive(message = "Nhóm sinh viên không hợp lệ")
    private Long studentGroupId;

    @NotNull(message = "Đề tài không được để trống")
    @Positive(message = "Đề tài không hợp lệ")
    private Long topicId;
}
