package modules.evaluations.mapper;

import java.util.List;

import org.springframework.stereotype.Component;

import modules.committee_topic_assignments.entity.CommitteeTopicAssignment;
import modules.evaluations.dto.EvaluationResponse;
import modules.evaluations.dto.FinalResultResponse;
import modules.evaluations.entity.Evaluation;
import modules.evaluations.entity.FinalResult;
import modules.lecturers.entity.Lecturer;
import modules.topic_registrations.entity.TopicRegistration;

@Component
public class EvaluationMapper {

    public EvaluationResponse toEvaluationResponse(Evaluation evaluation) {
        Lecturer lecturer = evaluation.getLecturer();
        return EvaluationResponse.builder()
                .id(evaluation.getId())
                .assignmentId(evaluation.getCommitteeTopicAssignment().getId())
                .lecturerUserId(lecturer.getUserId())
                .lecturerCode(lecturer.getLecturerCode())
                .lecturerEmail(lecturer.getUser().getEmail())
                .score(evaluation.getScore())
                .comment(evaluation.getComment())
                .status(evaluation.getStatus())
                .submittedAt(evaluation.getSubmittedAt()).build();
    }

    public FinalResultResponse toFinalResultResponse(FinalResult finalResult, List<Evaluation> evaluations) {
        CommitteeTopicAssignment committeeTopicAssignment = finalResult.getCommitteeTopicAssignment();
        TopicRegistration topicRegistration = committeeTopicAssignment.getTopicRegistration();
        return FinalResultResponse.builder()
                .id(finalResult.getId())
                .assignmentId(committeeTopicAssignment.getId())
                .topicId(topicRegistration.getTopic().getId())
                .topicTitle(topicRegistration.getTopic().getTitle())
                .studentGroupId(topicRegistration.getStudentGroup().getId())
                .studentGroupName(topicRegistration.getStudentGroup().getName())
                .finalScore(finalResult.getFinalScore())
                .published(finalResult.isPublished())
                .publishedBy(finalResult.getPublishedBy())
                .publishedAt(finalResult.getPublishedAt())
                .evaluations(evaluations.stream().map(this::toEvaluationResponse).toList())
                .build();
    }
}
