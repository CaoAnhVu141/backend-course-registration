package modules.conversation.repository;

import modules.conversation.entity.ConversationMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ConversationMemberRepository extends JpaRepository<ConversationMember, Long> {
    boolean existsByConversationIdAndUserId(
            Long conversationId,
            Long userId);

    List<ConversationMember> findAllByConversationId(Long conversationId);

    void deleteAllByConversationId(Long conversationId);
}
