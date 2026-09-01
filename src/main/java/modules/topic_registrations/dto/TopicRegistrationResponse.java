package modules.topic_registrations.dto;

import lombok.Builder;
import lombok.Getter;
import modules.topic_registrations.enums.TopicRegistrationStatus;

import java.time.LocalDateTime;

@Getter
@Builder
public class TopicRegistrationResponse {

    private Long id;
    private Long studentGroupId;
    private String studentGroupName;
    private Long topicId;
    private String topicTitle;
    private TopicRegistrationStatus status;
    private String registeredBy;
    private LocalDateTime registeredAt;
    private String decidedBy;
    private String rejectionReason;
}