package modules.topic_registrations.mapper;

import modules.topic_registrations.dto.TopicRegistrationResponse;
import modules.topic_registrations.entity.TopicRegistration;
import org.springframework.stereotype.Component;

@Component
public class TopicRegistrationMapper {

    public TopicRegistrationResponse toResponse(TopicRegistration registration) {
        return TopicRegistrationResponse.builder()
                .id(registration.getId())
                .studentGroupId(registration.getStudentGroup().getId())
                .studentGroupName(registration.getStudentGroup().getName())
                .topicId(registration.getTopic().getId())
                .topicTitle(registration.getTopic().getTitle())
                .status(registration.getStatus())
                .registeredBy(registration.getRegisteredBy())
                .registeredAt(registration.getRegisteredAt())
                .decidedBy(registration.getDecidedBy())
                .rejectionReason(registration.getRejectedReason())
                .build();
    }
}
