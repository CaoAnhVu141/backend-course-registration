package modules.topics.mapper;

import modules.topics.dto.TopicResponse;
import modules.topics.entity.Topic;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Set;

@Component
public class TopicMapper {

    public TopicResponse toResponse(Topic topic) {
        return toResponse(topic, Collections.emptySet());
    }

    public TopicResponse toResponse(Topic topic, Set<Long> supervisorIds) {
        return TopicResponse.builder()
                .id(topic.getId())
                .title(topic.getTitle())
                .requirements(topic.getRequirements())
                .departmentId(topic.getDepartmentId())
                .registrationPeriodId(topic.getRegistrationPeriodId())
                .proposedBy(topic.getProposedBy())
                .status(topic.getStatus())
                .rejectionReason(topic.getRejectionReason())
                .supervisorIds(supervisorIds)
                .build();
    }
}