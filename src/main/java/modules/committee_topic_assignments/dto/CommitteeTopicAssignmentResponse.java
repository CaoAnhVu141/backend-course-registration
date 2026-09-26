package modules.committee_topic_assignments.dto;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;
import modules.committee_topic_assignments.enums.CommitteeTopicAssignmentStatus;

@Getter
@Builder
public class CommitteeTopicAssignmentResponse {

    private Long id;

    private Long defenseCommitteeId;
    private String defenseCommitteeCode;
    private String defenseCommitteeName;
    private Long registrationPeriodId;

    //topic
    private Long topicRegistrationId;
    private Long topicId;
    private String topicTitle;

    // student
    private Long studentGroupId;
    private String studentGroupName;

    private LocalDateTime scheduledStart;
    private LocalDateTime scheduledEnd;
    private String room;

    private CommitteeTopicAssignmentStatus status;
    private String assignedBy;
    private LocalDateTime assignedAt;
}
