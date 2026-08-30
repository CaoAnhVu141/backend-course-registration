package modules.topics.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RejectTopicRequest {

    @NotBlank(message = "Lý do từ chối không được để trống")
    private String reason;
}
