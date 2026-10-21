package modules.conversation.controller;

import lombok.RequiredArgsConstructor;
import modules.conversation.dto.ChatMessageResponse;
import modules.conversation.dto.ConversationResponse;
import modules.conversation.service.ConversationService;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/chat")

@RequiredArgsConstructor
public class ConversationController {

    private final ConversationService conversationService;

    @GetMapping("/topic-registrations/{topicRegistrationId}")
    public ResponseEntity<ConversationResponse> getConversation(
            @PathVariable Long topicRegistrationId) {
        return ResponseEntity.ok(
                conversationService.getConversationByTopicRegistration(
                        topicRegistrationId));
    }

    @GetMapping("/{conversationId}/messages")
    public ResponseEntity<List<ChatMessageResponse>> getMessages(
            @PathVariable Long conversationId,
            Authentication authentication) {
        return ResponseEntity.ok(
                conversationService.getMessages(
                        conversationId,
                        authentication.getName()));
    }
}
