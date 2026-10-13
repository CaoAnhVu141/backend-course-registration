package modules.evaluations.service;

import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.tool.schema.spi.CommandAcceptanceException;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import modules.auth.repository.UserRepository;
import modules.committee_topic_assignments.entity.CommitteeTopicAssignment;
import modules.committee_topic_assignments.enums.CommitteeTopicAssignmentStatus;
import modules.committee_topic_assignments.repository.CommitteeTopicAssignmentRepository;
import modules.defense_committees.entity.CommitteeMember;
import modules.defense_committees.enums.CommitteeRole;
import modules.defense_committees.repository.CommitteeMemberRepository;
import modules.evaluations.dto.CreateEvaluationRequest;
import modules.evaluations.dto.EvaluationResponse;
import modules.evaluations.dto.FinalResultResponse;
import modules.evaluations.entity.Evaluation;
import modules.evaluations.entity.FinalResult;
import modules.evaluations.enums.EnumEvaluationError;
import modules.evaluations.enums.EvaluationStatus;
import modules.evaluations.exception.EvaluationException;
import modules.evaluations.mapper.EvaluationMapper;
import modules.evaluations.repository.EvaluationRepository;
import modules.evaluations.repository.FinalResultRepository;
import modules.lecturers.entity.Lecturer;
import modules.lecturers.repository.LecturerRepository;
import modules.student_group.repository.GroupMemberRepository;
import modules.students.repository.StudentRepository;

@Service
@RequiredArgsConstructor
public class EvaluationService {

    private final EvaluationRepository evaluationRepository;
    private final FinalResultRepository finalResultRepository;
    private final CommitteeTopicAssignmentRepository committeeTopicAssignmentRepository;
    private final CommitteeMemberRepository committeeMemberRepository;
    private final LecturerRepository lecturerRepository;
    private final StudentRepository studentRepository;
    private final GroupMemberRepository groupMemberRepository;
    private final UserRepository userRepository;
    private final EvaluationMapper evaluationMapper;

    @Transactional
    public EvaluationResponse createEvaluationService(CreateEvaluationRequest createEvaluationRequest,
            String username) {
        Lecturer lecturer = lecturerRepository.findByUserUsername(username)
                .orElseThrow(() -> new EvaluationException(EnumEvaluationError.LECTURER_NOT_FOUND));
        CommitteeTopicAssignment committeeTopicAssignment = findCommitteeTopicAssignmentById(
                createEvaluationRequest.getAssignmentId());

        // chỉ chấm khi đã có sự phân công
        if (committeeTopicAssignment.getStatus() != CommitteeTopicAssignmentStatus.ASSIGNED) {
            throw new EvaluationException(EnumEvaluationError.INVALID_ASSIGNMENT_STATUS);
        }

        // giảng viên chấm phải thuộc đề tài
        if (!committeeMemberRepository.existsByLecturerUserIdAndDefenseCommitteeId(
                lecturer.getUserId(), committeeTopicAssignment.getDefenseCommittee().getId())) {
            throw new EvaluationException(EnumEvaluationError.NOT_COMMITTEE_MEMBER);
        }

        // mỗi gv chỉ chấm 1 lần
        if (evaluationRepository.existsByCommitteeTopicAssignmentIdAndLecturerUserId(
                committeeTopicAssignment.getId(), lecturer.getUserId())) {
            throw new EvaluationException(EnumEvaluationError.ALREADY_EVALUATED);
        }

        Evaluation evaluation = evaluationRepository.save(
                Evaluation.builder()
                        .committeeTopicAssignment(committeeTopicAssignment)
                        .lecturer(lecturer)
                        .score(createEvaluationRequest.getScore())
                        .comment(createEvaluationRequest.getComment())
                        .status(EvaluationStatus.SUBMITTED)
                        .submittedAt(LocalDateTime.now())
                        .build());
        return evaluationMapper.toEvaluationResponse(evaluation);
    }

    @Transactional
    public FinalResultResponse publishFInalResultService(Long assignmentId, String username) {
        Lecturer lecturer = lecturerRepository.findByUserUsername(username)
                .orElseThrow(() -> new EvaluationException(EnumEvaluationError.LECTURER_NOT_FOUND));
        CommitteeTopicAssignment committeeTopicAssignment = findCommitteeTopicAssignmentById(assignmentId);
        if (committeeTopicAssignment.getStatus() != CommitteeTopicAssignmentStatus.ASSIGNED) {
            throw new EvaluationException(EnumEvaluationError.INVALID_ASSIGNMENT_STATUS);
        }

        // mỗi phân công chỉ có 1 kết quả thôi
        if (finalResultRepository.existsByCommitteeTopicAssignmentId(committeeTopicAssignment.getId())) {
            throw new EvaluationException(EnumEvaluationError.RESULT_ALREADY_PUBLISHED);
        }

        List<CommitteeMember> committeeMembers = committeeMemberRepository
                .findAllByDefenseCommitteeId(committeeTopicAssignment.getDefenseCommittee().getId());

        // check
        boolean isChairman = committeeMembers.stream()
                .anyMatch(m -> m.getCommitteeRole() == CommitteeRole.CHAIRMAN
                        && m.getLecturer().getUserId().equals(lecturer.getUserId()));
        if (!isChairman) {
            throw new EvaluationException(EnumEvaluationError.NOT_CHAIRMAN);
        }

        List<Evaluation> evaluations = evaluationRepository.findAllByCommitteeTopicAssignmentId(
                committeeTopicAssignment.getId());
        if (evaluations.isEmpty()) {
            throw new EvaluationException(EnumEvaluationError.NO_EVALUATIONS_YET);
        }

        // phải chấm đầy đủ thành viên
        if (evaluations.size() < committeeMembers.size()) {
            throw new EvaluationException(EnumEvaluationError.NOT_ALL_MEMBERS_EVALUATED);
        }

        double avg = evaluations.stream().mapToDouble(Evaluation::getScore).average().orElse(0.0);
        double finalScore = Math.round(avg * 100.0) / 100.0; // làm tròn số lẻ

        FinalResult finalResult = finalResultRepository.save(
                FinalResult.builder()
                        .committeeTopicAssignment(committeeTopicAssignment)
                        .finalScore(finalScore)
                        .published(true)
                        .publishedBy(lecturer.getUser().getEmail())
                        .publishedAt(LocalDateTime.now())
                        .build());
        committeeTopicAssignment.setStatus(CommitteeTopicAssignmentStatus.COMPLETED);
        return evaluationMapper.toFinalResultResponse(finalResult, evaluations);

    }

    // xem theo của 1 sự phân công
    @Transactional(readOnly = true)
    public List<EvaluationResponse> getEvaluationsByAssignmentService(Long assignmentId) {
        findCommitteeTopicAssignmentById(assignmentId);
        return evaluationRepository.findAllByCommitteeTopicAssignmentId(assignmentId).stream()
                .map(evaluationMapper::toEvaluationResponse).toList();
    }

    // func helper
    private CommitteeTopicAssignment findCommitteeTopicAssignmentById(Long id) {
        return committeeTopicAssignmentRepository.findById(id)
                .orElseThrow(() -> new EvaluationException(EnumEvaluationError.ASSIGNMENT_NOT_FOUND));
    }
}
