package modules.topics.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import modules.topics.entity.Topic;
import modules.topics.enums.TopicStatus;
import java.util.List;

public interface TopicRepository extends JpaRepository<Topic, Long>, JpaSpecificationExecutor<Topic> {

    List<Topic> findAllByStatus(TopicStatus status);
}
