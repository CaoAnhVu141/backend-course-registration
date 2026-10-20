package modules.notifications.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.*;
import modules.auth.entity.User;
import modules.notifications.enums.NotificationType;

@Entity
@Table(name = "notifications")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, length = 50)
    private NotificationType type;

    @Column(name = "title", nullable = false, length = 255)
    private String title;

    @Column(name = "content", columnDefinition = "TEXT")
    private String content;

    @Column(name = "is_readed", nullable = false)
    private boolean readed;

    @Column(name = "readed_at")
    private LocalDateTime readedAt;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
}
