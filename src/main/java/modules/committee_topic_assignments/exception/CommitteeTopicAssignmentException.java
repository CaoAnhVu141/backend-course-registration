package modules.committee_topic_assignments.exception;

import modules.committee_topic_assignments.enums.EnumCommitteeTopicAssignmentError;

public class CommitteeTopicAssignmentException extends RuntimeException {

    public CommitteeTopicAssignmentException(EnumCommitteeTopicAssignmentError error) {
        super(error.getMessage());
    }
}
