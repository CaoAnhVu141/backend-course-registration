package modules.conversation.controller;

import lombok.RequiredArgsConstructor;
import modules.conversation.dto.SendChatMessageRequest;
import modules.conversation.service.ConversationService;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller

@RequiredArgsConstructor
public class ChatWebSocketController {

    private final ConversationService conversationService;

    @MessageMapping("/chat.send")
    public void sendMessage(
            @Payload SendChatMessageRequest request,
            Principal principal) {
        // conversationId nên truyền thêm trong request
        conversationService.sendMessage(
                request.getConversationId(),
                request.getContent(),
                principal.getName());
    }
}
