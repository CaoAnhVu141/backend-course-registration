package modules.evaluations.repository;

import modules.evaluations.entity.Evaluation;

public interface FinalResultRepository extends JpaRepository<Evaluation, Long> {

    // check có kết quả cuối cùng bài làm
    boolean existsByCommitteeTopicAssignmentId(Long assignmentId);

    // Lấy kết quả
    List<FinalResult> findByCommitteeTopicAssignmentId(Long assignmentId);

    // sinh viên xem kết quả của nhóm mình
    List<FinalResult> findAllByCommitteeTopicAssignmentIdInAndPublishedTrue(Long assignmentIds);

}
