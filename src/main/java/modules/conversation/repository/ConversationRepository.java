package modules.conversation.repository;

import modules.conversation.entity.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ConversationRepository extends JpaRepository<Conversation, Long> {
    Optional<Conversation> findByTopicRegistrationId(Long topicRegistrationId);

    boolean existsByTopicRegistrationId(Long topicRegistrationId);
}
