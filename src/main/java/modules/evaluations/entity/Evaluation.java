package modules.evaluations.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.*;
import modules.committee_topic_assignments.entity.CommitteeTopicAssignment;
import modules.evaluations.enums.EvaluationStatus;
import modules.lecturers.entity.Lecturer;

@Entity
@Table(name = "evaluations", uniqueConstraints = @UniqueConstraint(columnNames = {
        "committee_topic_assignment_id", "user_id"
}))
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Evaluation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "evaluation_id")
    private Long id;

    // phân công chấm
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "committee_topic_assignment_id", nullable = false)
    private CommitteeTopicAssignment committeeTopicAssignment;

    // giảng viên chấm
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private Lecturer lecturer;

    @Column(name = "score", nullable = false)
    private Double score;

    @Column(name = "comment", columnDefinition = "TEXT")
    private String comment;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 50)
    private EvaluationStatus status;

    @Column(name = "submitted_at", nullable = false)
    private LocalDateTime submittedAt;
}
