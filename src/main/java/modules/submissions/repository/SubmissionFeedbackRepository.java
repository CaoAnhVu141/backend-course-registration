package modules.submissions.repository;

import modules.submissions.entity.SubmissionFeedback;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubmissionFeedbackRepository extends JpaRepository<SubmissionFeedback, Long> {

    List<SubmissionFeedback> getAllBySubmssionVersionId(Long submissionVersionId);

    List<SubmissionFeedback> findAllByLecturerUserId(Long lecturerUserId);
}