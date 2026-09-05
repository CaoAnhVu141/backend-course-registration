package modules.submissions.service;

import modules.auth.entity.User;
import modules.auth.repository.UserRepository;
import modules.submissions.dto.CreateSubmissionRequest;
import modules.submissions.dto.CreateSubmissionVersionRequest;
import modules.submissions.dto.SubmissionResponse;
import modules.submissions.dto.SubmissionVersionResponse;
import modules.submissions.entity.Submission;
import modules.submissions.entity.SubmissionVersion;
import modules.submissions.enums.EnumSubmissionError;
import modules.submissions.exception.SubmissionException;
import modules.submissions.mapper.SubmissionMapper;
import modules.submissions.repository.SubmissionRepository;
import modules.submissions.repository.SubmissionVersionRepository;
import modules.topic_registrations.enums.TopicRegistrationStatus;
import modules.topic_registrations.repository.TopicRegistrationRepository;
import modules.topic_registrations.entity.TopicRegistration;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SubmissionService {

    private final SubmissionRepository submissionRepository;
    private final SubmissionVersionRepository submissionVersionRepository;
    private final TopicRegistrationRepository topicRegistrationRepository;
    private final UserRepository userRepository;
    private final SubmissionMapper submissionMapper;

    @Transactional
    public SubmissionResponse createSubmissionService(CreateSubmissionRequest createSubmissionRequest) {
        TopicRegistration topicRegistration = topicRegistrationRepository
                .findById(createSubmissionRequest.getTopicRegistrationId())
                .orElseThrow(() -> new SubmissionException(EnumSubmissionError.TOPIC_REGISTRATION_NOT_FOUND));

        if (topicRegistration.getStatus() != TopicRegistrationStatus.APPROVED) {
            throw new SubmissionException(EnumSubmissionError.TOPIC_REGISTRATION_NOT_APPROVED);
        }

        if (submissionRepository.existsByTopicRegistrationId(topicRegistration.getId())) {
            throw new SubmissionException(EnumSubmissionError.SUBMISSION_ALREADY_EXISTS);
        }

        Submission submission = submissionRepository.save(
                Submission.builder()
                        .topicRegistration(topicRegistration)
                        .status(true)
                        .submittedAt(LocalDateTime.now())
                        .build());

        return submissionMapper.toResponse(submission, List.of());
    }

    @Transactional
    public SubmissionVersionResponse createSubmissionVersionService(
            CreateSubmissionVersionRequest createSubmissionVersionRequest, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new SubmissionException(EnumSubmissionError.USER_NOT_FOUND));
        Submission submission = findSubmissionById(createSubmissionVersionRequest.getSubmissionId());
        SubmissionVersion submissionVersion = submissionVersionRepository.save(
                SubmissionVersion.builder()
                        .submission(submission)
                        .fileName(createSubmissionVersionRequest.getFileName())
                        .storageKey(createSubmissionVersionRequest.getStorageKey())
                        .mimeType(createSubmissionVersionRequest.getMimeType())
                        .fileSize(createSubmissionVersionRequest.getFileSize())
                        .changeNote(createSubmissionVersionRequest.getChangeNote())
                        .uploadedBy(user.getEmail())
                        .build());
        return submissionMapper.toSubmissionVersionResponse(submissionVersion);
    }

    @Transactional(readOnly = true)
    public List<SubmissionResponse> getAllSubmissionService() {
        return submissionRepository.findAll().stream()
                .map(submission -> submissionMapper.toResponse(
                        submission,
                        submissionVersionRepository.findAllBySubmissionId(submission.getId())))
                .toList();
    }

    @Transactional(readOnly = true)
    public SubmissionResponse getSubmissionByIdService(Long submissionId) {
        Submission submission = findSubmissionById(submissionId);
        return submissionMapper.toResponse(
                submission,
                submissionVersionRepository.findAllBySubmissionId(submission.getId()));
    }

    @Transactional(readOnly = true)
    public SubmissionResponse getSubmissionByTopicRegistrationService(Long topicRegistrationId) {
        Submission submission = submissionRepository.findByTopicRegistrationId(topicRegistrationId)
                .orElseThrow(() -> new SubmissionException(EnumSubmissionError.SUBMISSION_NOT_FOUND));
        return submissionMapper.toResponse(
                submission,
                submissionVersionRepository.findAllBySubmissionId(submission.getId()));
    }

    // function hepler
    private Submission findSubmissionById(Long submissionId) {
        return submissionRepository.findById(submissionId)
                .orElseThrow(() -> new SubmissionException(EnumSubmissionError.SUBMISSION_NOT_FOUND));
    }
}
