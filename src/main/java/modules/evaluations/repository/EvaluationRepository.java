package modules.evaluations.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import modules.evaluations.entity.Evaluation;

public interface EvaluationRepository extends JpaRepository<Evaluation, Long> {

    // check giảng viên chấm phân công hay chưa, không được chấm trùng
    boolean existsByCommitteeTopicAssignmentIdAndLecturerUserId(Long assignmentId, Long lecturerUserId);

    // lấy toàn bộ danh sách
    List<Evaluation> findAllByCommitteeTopicAssignmentId(Long assignmentId);

}
