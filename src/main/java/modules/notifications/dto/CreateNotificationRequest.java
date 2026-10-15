package modules.notifications.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import modules.notifications.enums.NotificationTarget;

@Getter
@Setter
public class CreateNotificationRequest {

    @NotNull(message = "Phạm vi gửi không được để trống")
    private NotificationTarget target;

    @NotBlank(message = "Tiêu đề không được để trống")
    @Size(max = 255, message = "Tiêu đề không được vượt quá 255 ký tự")
    private String title;

    @NotBlank(message = "Nội dung không được để trống")
    private String content;
}
