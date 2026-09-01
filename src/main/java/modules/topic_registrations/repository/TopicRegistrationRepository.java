package modules.topic_registrations.repository;

import modules.topic_registrations.entity.TopicRegistration;
import modules.topic_registrations.enums.TopicRegistrationStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TopicRegistrationRepository extends JpaRepository<TopicRegistration, Long> {

    // check sinh viên đã đăng ký đề tài trong kỳ đăng ký hiện tại chưa
    boolean existsByStudentGroupIdAndStatusIn(Long studentGroupId,
            List<TopicRegistrationStatus> statuses);

    // kiểm tra đề tài tồn tại và trạng thái như thế nào
    boolean existsByTopicIdAndStatus(Long topicId, TopicRegistrationStatus status);

    List<TopicRegistration> findAllByTopicId(Long topicId);

    List<TopicRegistration> findAllByStudentGroupId(Long studentGroupId);
}
