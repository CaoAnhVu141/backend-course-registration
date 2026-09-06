package modules.submissions.entity;

import jakarta.persistence.*;
import lombok.*;
import modules.auth.entity.User;
import modules.lecturers.entity.Lecturer;

@Entity
@Table(name = "submission_feedbacks")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class SubmissionFeedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "submission_feedback_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "submission_version_id", nullable = false)
    private SubmissionVersion submissionVersion;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private Lecturer lecturer;

    @Column(name = "comment", nullable = false, columnDefinition = "TEXT")
    private String comment;
}
