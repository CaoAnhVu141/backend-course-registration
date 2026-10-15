package modules.notifications.dto;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;
import modules.notifications.enums.NotificationType;

@Getter
@Builder
public class NotificationResponse {
    private Long id;
    private NotificationType type;
    private String title;
    private String content;
    private boolean readed;
    private LocalDateTime readedAt;
    private LocalDateTime createdAt;
}
