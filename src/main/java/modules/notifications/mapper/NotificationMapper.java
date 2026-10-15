package modules.notifications.mapper;

import org.springframework.stereotype.Component;

import modules.notifications.dto.NotificationResponse;
import modules.notifications.entity.Notification;

public class NotificationMapper {

    public NotificationResponse toResponse(Notification notification) {
        return NotificationResponse.builder()
                .id(notification.getId())
                .type(notification.getType())
                .title(notification.getTitle())
                .content(notification.getContent())
                .readed(notification.isReaded())
                .readedAt(notification.getReadedAt())
                .createdAt(notification.getCreatedAt())
                .build();
    }
}
