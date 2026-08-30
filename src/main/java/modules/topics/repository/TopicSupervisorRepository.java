package modules.topics.repository;

import modules.topics.entity.TopicSupervisor;
import modules.topics.entity.TopicSupervisorId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TopicSupervisorRepository extends JpaRepository<TopicSupervisor, TopicSupervisorId> {

    List<TopicSupervisor> findAllByLecturerUserId(Long userId);

    List<TopicSupervisor> findAllByTopicId(Long topicId);

    void deleteAllByTopicId(Long topicId);
}