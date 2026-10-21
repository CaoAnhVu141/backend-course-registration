package modules.conversation.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class ConversationResponse {

    private Long id;
    private Long topicRegistrationId;
    private String name;
    private List<Long> memberUserIds;
}