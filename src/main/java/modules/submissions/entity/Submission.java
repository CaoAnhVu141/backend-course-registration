package modules.submissions.entity;

import jakarta.persistence.*;
import lombok.*;
import modules.topic_registrations.entity.TopicRegistration;

import java.time.LocalDateTime;

@Entity
@Table(name = "submissions")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Submission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "submission_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "topic_registration_id", nullable = false, unique = true)
    private TopicRegistration topicRegistration;

    @Column(name = "status", nullable = false)
    private boolean status;

    @Column(name = "submitted_at")
    private LocalDateTime submittedAt;
}
