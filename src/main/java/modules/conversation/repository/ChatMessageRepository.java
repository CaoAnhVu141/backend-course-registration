package modules.conversation.repository;

import modules.conversation.entity.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    // Lấy tất cả tin nhắn thuộc một conversation
    List<ChatMessage> findAllByConversationIdOrderByCreatedAtAsc(
            Long conversationId);
}
