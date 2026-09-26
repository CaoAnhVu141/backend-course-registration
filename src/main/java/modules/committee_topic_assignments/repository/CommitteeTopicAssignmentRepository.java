package modules.committee_topic_assignments.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import modules.committee_topic_assignments.entity.CommitteeTopicAssignment;
import modules.committee_topic_assignments.enums.CommitteeTopicAssignmentStatus;

public interface CommitteeTopicAssignmentRepository extends JpaRepository<CommitteeTopicAssignment, Long> {

        // một đăng kí đề tài chỉ được phân công cho một hội đồng
        boolean existsByTopicRegistrationId(Long topicRegistrationId);

        Optional<CommitteeTopicAssignment> findByTopicRegistrationId(Long topicRegistrationId);

        // danh sách đề tài theo hội đồng chấm
        List<CommitteeTopicAssignment> findAllByDefenseCommitteeId(Long defenseCommitteeId);

        // giảng viên xem lịch chấm của các hội đồng mình tham gia
        List<CommitteeTopicAssignment> findAllByDefenseCommitteeIdIn(List<Long> defenseCommitteeIds);

        // lọc theo kỳ đăng ký (đi qua hội đồng vì bảng này không lưu
        // registration_period_id)
        List<CommitteeTopicAssignment> findAllByDefenseCommitteeRegistrationPeriodId(Long registrationPeriodId);

        // sinh viên xem lịch bảo vệ của nhóm mình
        List<CommitteeTopicAssignment> findAllByTopicRegistrationStudentGroupIdIn(List<Long> studentGroupIds);

        // đếm số đề tài đang chấm của các hội đồng mà giảng viên tham gia -> check
        // max_review_load
        long countByDefenseCommitteeIdInAndStatus(List<Long> defenseCommitteeIds,
                        CommitteeTopicAssignmentStatus status);

        // check trùng phòng + trùng giờ khi tạo phân công mới
        // điều kiện giao nhau: scheduledStart < newEnd AND scheduledEnd > newStart
        boolean existsByRoomAndStatusNotAndScheduledStartLessThanAndScheduledEndGreaterThan(
                        String room,
                        CommitteeTopicAssignmentStatus excludedStatus,
                        LocalDateTime newScheduledEnd,
                        LocalDateTime newScheduledStart);

        // check trùng phòng + trùng giờ khi cập nhật lịch (bỏ qua chính bản ghi đang
        // sửa)
        boolean existsByRoomAndStatusNotAndIdNotAndScheduledStartLessThanAndScheduledEndGreaterThan(
                        String room,
                        CommitteeTopicAssignmentStatus excludedStatus,
                        Long excludedId,
                        LocalDateTime newScheduledEnd,
                        LocalDateTime newScheduledStart);

}
