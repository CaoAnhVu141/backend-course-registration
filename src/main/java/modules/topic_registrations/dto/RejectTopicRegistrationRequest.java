package modules.topic_registrations.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RejectTopicRegistrationRequest {

    @NotBlank(message = "Lý do từ chối không được để trống")
    private String rejectionReason;
}
