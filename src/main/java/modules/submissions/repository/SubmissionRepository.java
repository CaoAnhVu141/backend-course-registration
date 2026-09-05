package modules.submissions.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import modules.submissions.entity.Submission;
import java.util.Optional;

public interface SubmissionRepository extends JpaRepository<Submission, Long> {

    boolean existsByTopicRegistrationId(Long topicRegistrationId);

    Optional<Submission> findByTopicRegistrationId(Long topicRegistrationId);
}
