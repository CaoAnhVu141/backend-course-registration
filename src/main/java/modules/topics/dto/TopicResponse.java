package modules.topics.dto;

import lombok.Builder;
import lombok.Getter;
import modules.topics.enums.TopicStatus;

import java.util.Set;

@Getter

@Builder
public class TopicResponse {

    private Long id;
    private String title;
    private String requirements;
    private Long departmentId;
    private Long registrationPeriodId;
    private String proposedBy;
    private TopicStatus status;
    private Set<Long> supervisorIds;

}
