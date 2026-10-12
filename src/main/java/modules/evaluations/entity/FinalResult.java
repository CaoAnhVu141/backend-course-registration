package modules.evaluations.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.*;
import modules.committee_topic_assignments.entity.CommitteeTopicAssignment;
import modules.evaluations.enums.EvaluationStatus;
import modules.lecturers.entity.Lecturer;

@Entity
@Table(name = "final_results")

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FinalResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "final_result_id")
    private Long id;

    // chỉ có 1 kết quả duy nhất
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "committee_topic_assignment_id", nullable = false, unique = true)
    private CommitteeTopicAssignment committeeTopicAssignment;

    // điểm
    @Column(name = "final_score")
    private Double finaleScore;

    @Column(name = "published", nullable = false)
    private boolean published;

    @Column(name = "published_by", length = 255)
    private String pulishedBy;

    @Column(name = "published_at")
    private LocalDateTime publishedAt;

}
