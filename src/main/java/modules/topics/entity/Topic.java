package modules.topics.entity;

import jakarta.persistence.*;
import lombok.*;
import modules.topics.enums.TopicStatus;

@Entity
@Table(name = "topics")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Topic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "topic_id")
    private Long id;

    @Column(name = "title", nullable = false, length = 255)
    private String title;

    @Column(name = "requirements", columnDefinition = "TEXT")
    private String requirements;

    @Column(name = "department_id", nullable = false)
    private Long departmentId;

    @Column(name = "registration_period_id", nullable = false)
    private Long registrationPeriodId;

    @Column(name = "proposed_by", nullable = false, length = 255)
    private String proposedBy;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 50)
    private TopicStatus status;

    @Column(name = "rejection_reason", columnDefinition = "TEXT", nullable = true)
    private String rejectionReason;
}