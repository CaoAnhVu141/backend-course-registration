package modules.topics.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import modules.topics.entity.Topic;
import modules.topics.enums.TopicStatus;
import java.util.List;

public interface TopicRepository extends JpaRepository<Topic, Long> {

    List<Topic> findAllByStatus(TopicStatus status);
}
