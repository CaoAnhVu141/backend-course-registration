package modules.evaluations.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import modules.evaluations.entity.FinalResult;

public interface FinalResultRepository extends JpaRepository<FinalResult, Long> {

    // check có kết quả cuối cùng bài làm
    boolean existsByCommitteeTopicAssignmentId(Long assignmentId);

    // lấy kết quả bài làm
    Optional<FinalResult> findByCommitteeTopicAssignmentId(Long assignmentId);

    // sinh viên xem kết quả của nhóm mình
    List<FinalResult> findAllByCommitteeTopicAssignmentIdInAndPublishedTrue(List<Long> assignmentIds);

}
