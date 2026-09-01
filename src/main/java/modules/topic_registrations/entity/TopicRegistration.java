package modules.topic_registrations.entity;

import jakarta.persistence.*;
import lombok.*;
import modules.student_group.entity.StudentGroup;
import modules.topic_registrations.enums.TopicRegistrationStatus;
import modules.topics.entity.Topic;

import java.time.LocalDateTime;

@Entity
@Table(name = "topic_registrations")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TopicRegistration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "topic_registration_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "student_group_id", nullable = false)
    private StudentGroup studentGroup;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "topic_id", nullable = false)
    private Topic topic;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 50)
    private TopicRegistrationStatus status;

    @Column(name = "registered_by", nullable = false)
    private String registeredBy;

    @Column(name = "registered_at", nullable = false, length = 255)
    private LocalDateTime registeredAt;

    @Column(name = "decided_by", length = 255)
    private String decidedBy;

    @Column(name = "rejected_reason", length = 255)
    private String rejectedReason;

}
