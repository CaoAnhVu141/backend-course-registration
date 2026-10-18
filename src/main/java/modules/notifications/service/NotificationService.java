package modules.notifications.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import modules.auth.entity.User;
import modules.auth.repository.UserRepository;
import modules.notifications.dto.CreateNotificationRequest;
import modules.notifications.dto.NotificationResponse;
import modules.notifications.entity.Notification;
import modules.notifications.enums.EnumNotificationError;
import modules.notifications.enums.NotificationTarget;
import modules.notifications.enums.NotificationType;
import modules.notifications.exception.NotificationException;
import modules.notifications.mapper.NotificationMapper;
import modules.notifications.repository.NotificationRepository;
import org.springframework.messaging.simp.SimpMessagingTemplate;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final NotificationMapper notificationMapper;
    private final SimpMessagingTemplate messagingTemplate;

    @Transactional
    public int createNotificationService(CreateNotificationRequest request) {
        List<Long> recipientIds = resolveRecipientIds(request.getTarget());
        if (recipientIds.isEmpty()) {
            throw new NotificationException(EnumNotificationError.NO_RECIPIENTS);
        }
        notifyUsers(recipientIds, NotificationType.ANNOUNCEMENT, request.getTitle(), request.getContent());
        return recipientIds.size(); // số người nhận
    }

    // dùng chung: tạo 1 thông báo cho mỗi user (các service khác gọi khi có sự
    // kiện)
    @Transactional
    public void notifyUsers(List<Long> userIds, NotificationType type, String title, String content) {
        if (userIds == null || userIds.isEmpty()) {
            return;
        }
        List<User> users = userRepository.findAllById(userIds);
        if (users.isEmpty()) {
            return;
        }
        LocalDateTime now = LocalDateTime.now();
        List<Notification> notifications = users.stream()
                .map(user -> Notification.builder()
                        .user(user)
                        .type(type)
                        .title(title)
                        .content(content)
                        .readed(false)
                        .createdAt(now)
                        .build())
                .toList();
        notificationRepository.saveAll(notifications);
        // websocket
        notifications.forEach(n -> messagingTemplate.convertAndSendToUser(
                n.getUser().getUsername(),
                "/queue/notifications",
                notificationMapper.toResponse(n)));
    }

    // thông báo của tôi (mới nhất trước)
    @Transactional(readOnly = true)
    public List<NotificationResponse> getMyNotificationsService(String username) {
        User user = findUserByUsername(username);
        return notificationRepository.findAllByUserIdOrderByCreatedAtDesc(user.getId())
                .stream().map(notificationMapper::toResponse).toList();
    }

    // số chưa đọc
    @Transactional(readOnly = true)
    public long getUnreadCountService(String username) {
        User user = findUserByUsername(username);
        return notificationRepository.countByUserIdAndReadedFalse(user.getId());
    }

    @Transactional
    public NotificationResponse markAsReadService(Long notificationId, String username) {
        User user = findUserByUsername(username);
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new NotificationException(EnumNotificationError.NOTIFICATION_NOT_FOUND));
        if (!notification.getUser().getId().equals(user.getId())) {
            throw new NotificationException(EnumNotificationError.NOT_NOTIFICATION_OWNER);
        }
        if (!notification.isReaded()) {
            notification.setReaded(true);
            notification.setReadedAt(LocalDateTime.now());
        }
        return notificationMapper.toResponse(notification);
    }

    // func helper
    private User findUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new NotificationException(EnumNotificationError.USER_NOT_FOUND));
    }

    private List<Long> resolveRecipientIds(NotificationTarget target) {
        List<User> users = switch (target) {
            case ALL -> userRepository.findAll();
            case STUDENT -> userRepository.findAllByRolesName("STUDENT");
            case LECTURER -> userRepository.findAllByRolesName("LECTURER");
        };
        return users.stream().map(User::getId).toList();
    }
}
