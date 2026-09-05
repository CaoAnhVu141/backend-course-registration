package modules.submissions.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import modules.submissions.entity.SubmissionVersion;

import java.util.Optional;
import java.util.List;

public interface SubmissionVersionRepository extends JpaRepository<SubmissionVersion, Long> {

    List<SubmissionVersion> findAllBySubmissionId(Long submissionId);

}
