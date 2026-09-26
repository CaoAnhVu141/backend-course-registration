package modules.committee_topic_assignments.mapper;

import org.springframework.stereotype.Component;

import modules.committee_topic_assignments.dto.CommitteeTopicAssignmentResponse;
import modules.committee_topic_assignments.entity.CommitteeTopicAssignment;
import modules.defense_committees.entity.DefenseCommittee;
import modules.topic_registrations.entity.TopicRegistration;

@Component
public class CommitteeTopicAssignmentMapper {

    public CommitteeTopicAssignmentResponse toResponse(CommitteeTopicAssignment committeeTopicAssignment) {
        DefenseCommittee defenseCommittee = committeeTopicAssignment.getDefenseCommittee();
        TopicRegistration topicRegistration = committeeTopicAssignment.getTopicRegistration();

        return CommitteeTopicAssignmentResponse.builder()
                .id(committeeTopicAssignment.getId())
                .defenseCommitteeId(defenseCommittee.getId())
                .defenseCommitteeCode(defenseCommittee.getCode())
                .defenseCommitteeName(defenseCommittee.getName())
                .registrationPeriodId(defenseCommittee.getRegistrationPeriodId())
                .topicRegistrationId(topicRegistration.getId())
                .topicId(topicRegistration.getTopic().getId())
                .topicTitle(topicRegistration.getTopic().getTitle())
                .studentGroupId(topicRegistration.getStudentGroup().getId())
                .studentGroupName(topicRegistration.getStudentGroup().getName())
                .scheduledStart(committeeTopicAssignment.getScheduledStart())
                .scheduledEnd(committeeTopicAssignment.getScheduledEnd())
                .room(committeeTopicAssignment.getRoom())
                .status(committeeTopicAssignment.getStatus())
                .assignedBy(committeeTopicAssignment.getAssignedBy())
                .assignedAt(committeeTopicAssignment.getAssignedAt())
                .build();
    }
}
