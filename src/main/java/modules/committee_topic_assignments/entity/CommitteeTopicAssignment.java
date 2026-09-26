package modules.committee_topic_assignments.entity;

import java.time.LocalDateTime;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "committee_topic_assignments", uniqueConstraints = @UniqueConstraint(columnNames = {
                "topic_registration_id" }))
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class CommitteeTopicAssignment {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "committee_topic_assignment_id")
        private Long id;

        @ManyToOne(fetch = FetchType.LAZY, optional = false)
        @JoinColumn(name = "defense_committee_id", nullable = false)
        private DefenseCommittee defenseCommittee;

        // mỗi đăng kí đề tài chỉ được một hội đồng chấm
        @OneToOne(fetch = FetchType.LAZY, optional = false)
        @JoinColumn(name = "topic_registration_id", nullable = false, unique = true)
        private TopicRegistration topicRegistration;

        @Column(name = "scheduled_start")
        private LocalDateTime scheduledStart;

        @Column(name = "scheduled_end")
        private LocalDateTime scheduledEnd;

        // phòng bảo vệ
        @Column(name = "room", length = 100)
        private String room;

        @Enumerated(EnumType.STRING)
        @Column(name = "status", nullable = false, length = 50)
        private CommitteeTopicAssignmentStatus status;

        @Column(name = "assigned_by", nullable = false, length = 255)
        private String assignedBy;

        @Column(name = "assigned_at", nullable = false)
        private LocalDateTime assignedAt;
}
