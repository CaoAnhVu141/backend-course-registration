package modules.conversation.entity;

import jakarta.persistence.*;
import lombok.*;
import modules.auth.entity.User;

import java.time.LocalDateTime;

@Entity
@Table(name = "conversation_members", uniqueConstraints = @UniqueConstraint(columnNames = { "conversation_id", "user_id"
}))

@Getter

@Setter

@Builder

@NoArgsConstructor

@AllArgsConstructor
public class ConversationMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "conversation_member_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "conversation_id", nullable = false)
    private Conversation conversation;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "joined_at", nullable = false)
    private LocalDateTime joinedAt;

    @PrePersist
    protected void onCreate() {
        joinedAt = LocalDateTime.now();
    }
}