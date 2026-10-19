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
import modules.lecturers.entity.Lecturer;
import modules.lecturers.repository.LecturerRepository;
import modules.submissions.dto.CreateSubmissionFeedbackRequest;
import modules.submissions.dto.SubmissionFeedbackResponse;
import modules.submissions.entity.SubmissionFeedback;
import modules.submissions.repository.SubmissionFeedbackRepository;
import java.time.LocalDateTime;
import java.util.List;
import modules.student_group.enums.GroupMemberRole;
import modules.student_group.repository.GroupMemberRepository;
import org.springframework.web.multipart.MultipartFile;
import global.storage.FileStorageService;
import modules.submissions.entity.SubmissionVersion;

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
        private final SubmissionFeedbackRepository submissionFeedbackRepository;
        private final LecturerRepository lecturerRepository;
        private final GroupMemberRepository groupMemberRepository;
        private final FileStorageService fileStorageService;

        @Transactional
        public SubmissionResponse createSubmissionService(CreateSubmissionRequest createSubmissionRequest,
                        String username) {
                TopicRegistration topicRegistration = topicRegistrationRepository
                                .findById(createSubmissionRequest.getTopicRegistrationId())
                                .orElseThrow(() -> new SubmissionException(
                                                EnumSubmissionError.TOPIC_REGISTRATION_NOT_FOUND));
                User user = userRepository.findByUsername(username).orElseThrow(
                                () -> new SubmissionException(EnumSubmissionError.USER_NOT_FOUND));

                if (topicRegistration.getStatus() != TopicRegistrationStatus.APPROVED) {
                        throw new SubmissionException(EnumSubmissionError.TOPIC_REGISTRATION_NOT_APPROVED);
                }

                // chỉ nhóm trưởng mới thực hiện nộp được báo cáo
                if (!groupMemberRepository.existsByStudentGroupIdAndStudentUserIdAndRole(
                                topicRegistration.getStudentGroup().getId(), user.getId(), GroupMemberRole.LEADER)) {
                        throw new SubmissionException(EnumSubmissionError.NOT_GROUP_LEADER);
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
                        Long submissionId, String changeNote,
                        MultipartFile file, String username) {
                User user = userRepository.findByUsername(username)
                                .orElseThrow(() -> new SubmissionException(EnumSubmissionError.USER_NOT_FOUND));
                Submission submission = findSubmissionById(submissionId);
                // lưu file thật xuống đĩa, lấy storageKey
                String storageKey = fileStorageService.store(file);
                SubmissionVersion submissionVersion = submissionVersionRepository.save(
                                SubmissionVersion.builder()
                                                .submission(submission)
                                                .fileName(file.getOriginalFilename())
                                                .storageKey(storageKey)
                                                .mimeType(file.getContentType())
                                                .fileSize(file.getSize())
                                                .changeNote(changeNote)
                                                .uploadedBy(user.getEmail())
                                                .build());
                return submissionMapper.toSubmissionVersionResponse(submissionVersion);
        }

        // lấy version bản báo cáo
        @Transactional(readOnly = true)
        public SubmissionVersion getSubmissionVersionForDownloadService(Long versionId) {
                return submissionVersionRepository.findById(versionId).orElseThrow(
                                () -> new SubmissionException(EnumSubmissionError.SUBMISSION_VERSION_NOT_FOUND));
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

        // xử lý feedback cho báo cáo
        @Transactional
        public SubmissionFeedbackResponse createSubmissionFeedbackService(
                        CreateSubmissionFeedbackRequest createSubmissionFeedbackRequest, String username) {
                Lecturer lecturer = lecturerRepository.findByUserUsername(username)
                                .orElseThrow(() -> new SubmissionException(EnumSubmissionError.LECTURER_NOT_FOUND));
                SubmissionVersion submissionVersion = submissionVersionRepository
                                .findById(createSubmissionFeedbackRequest.getSubmissionVersionId())
                                .orElseThrow(() -> new SubmissionException(
                                                EnumSubmissionError.SUBMISSION_VERSION_NOT_FOUND));
                SubmissionFeedback feedback = submissionFeedbackRepository.save(
                                SubmissionFeedback.builder()
                                                .submissionVersion(submissionVersion)
                                                .lecturer(lecturer)
                                                .comment(createSubmissionFeedbackRequest.getComment().trim())
                                                .build());
                return submissionMapper.toResponseSubmissionFeedbackResponse(feedback);
        }

        @Transactional(readOnly = true)
        public List<SubmissionFeedbackResponse> getFeedbacksBySubmissionVersionService(Long submissionVersionId) {
                if (!submissionVersionRepository.existsById(submissionVersionId)) {
                        throw new SubmissionException(EnumSubmissionError.SUBMISSION_VERSION_NOT_FOUND);
                }
                return submissionFeedbackRepository.findAllBySubmissionVersionId(submissionVersionId)
                                .stream()
                                .map(submissionMapper::toResponseSubmissionFeedbackResponse)
                                .toList();
        }

        @Transactional(readOnly = true)
        public List<SubmissionFeedbackResponse> getMyFeedbacksService(String username) {
                Lecturer lecturer = lecturerRepository.findByUserUsername(username)
                                .orElseThrow(() -> new SubmissionException(EnumSubmissionError.LECTURER_NOT_FOUND));

                return submissionFeedbackRepository.findAllByLecturerUserId(lecturer.getUserId())
                                .stream()
                                .map(submissionMapper::toResponseSubmissionFeedbackResponse)
                                .toList();
        }

        // function hepler
        private Submission findSubmissionById(Long submissionId) {
                return submissionRepository.findById(submissionId)
                                .orElseThrow(() -> new SubmissionException(EnumSubmissionError.SUBMISSION_NOT_FOUND));
        }
}
