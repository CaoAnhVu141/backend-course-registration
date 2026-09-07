package modules.submissions.controller;

import static global.security.PermissionConstants.HAS_ANY_ROLE_ADMIN_LECTURER;
import static global.security.PermissionConstants.HAS_ANY_ROLE_ADMIN_LECTURER_STUDENT;
import static global.security.PermissionConstants.HAS_ROLE_STUDENT;
import static global.security.PermissionConstants.HAS_ROLE_LECTURER;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import global.response.ApiResponse;
import org.springframework.http.HttpStatus;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import modules.submissions.dto.CreateSubmissionRequest;
import modules.submissions.dto.CreateSubmissionVersionRequest;
import modules.submissions.dto.SubmissionResponse;
import modules.submissions.dto.SubmissionVersionResponse;
import modules.submissions.entity.Submission;
import modules.submissions.enums.EnumSubmissionSuccess;
import modules.submissions.service.SubmissionService;
import modules.submissions.dto.CreateSubmissionFeedbackRequest;
import modules.submissions.dto.SubmissionFeedbackResponse;

@RestController
@RequestMapping("/api/submissions")
@RequiredArgsConstructor
public class SubmissionController {

        private final SubmissionService submissionService;

        @PreAuthorize(HAS_ROLE_STUDENT)
        @PostMapping
        public ResponseEntity<ApiResponse<SubmissionResponse>> createSubmissionController(
                        @Valid @RequestBody CreateSubmissionRequest request) {
                SubmissionResponse response = submissionService.createSubmissionService(request);
                return ResponseEntity.status(HttpStatus.CREATED).body(
                                ApiResponse.success(HttpStatus.CREATED.value(),
                                                EnumSubmissionSuccess.CREATED.getMessage(), response));
        }

        @PreAuthorize(HAS_ANY_ROLE_ADMIN_LECTURER)
        @GetMapping
        public ResponseEntity<ApiResponse<List<SubmissionResponse>>> getAllSubmissionController() {
                List<SubmissionResponse> response = submissionService.getAllSubmissionService();
                return ResponseEntity.ok(
                                ApiResponse.success(HttpStatus.OK.value(),
                                                EnumSubmissionSuccess.GET_ALL.getMessage(), response));
        }

        @PreAuthorize(HAS_ANY_ROLE_ADMIN_LECTURER_STUDENT)
        @GetMapping("/{id}")
        public ResponseEntity<ApiResponse<SubmissionResponse>> getSubmissionById(@PathVariable Long id) {
                SubmissionResponse response = submissionService.getSubmissionByIdService(id);
                return ResponseEntity.ok(
                                ApiResponse.success(HttpStatus.OK.value(),
                                                EnumSubmissionSuccess.GET_DETAIL.getMessage(), response));
        }

        // Nộp báo cáo
        @PreAuthorize(HAS_ROLE_STUDENT)
        @PostMapping("/versions")
        public ResponseEntity<ApiResponse<SubmissionVersionResponse>> createSubmissionVersionController(
                        @Valid @RequestBody CreateSubmissionVersionRequest request,
                        Authentication authentication) {
                SubmissionVersionResponse response = submissionService.createSubmissionVersionService(request,
                                authentication.getName());
                return ResponseEntity.ok(
                                ApiResponse.success(HttpStatus.OK.value(),
                                                EnumSubmissionSuccess.VERSION_CREATED.getMessage(), response));
        }

        // Lấy theo đề tài đăng kí
        @PreAuthorize(HAS_ANY_ROLE_ADMIN_LECTURER_STUDENT)
        @GetMapping("/topic-registration/{topicRegistrationId}")
        public ResponseEntity<ApiResponse<SubmissionResponse>> getByTopicRegistration(
                        @PathVariable Long topicRegistrationId) {
                SubmissionResponse response = submissionService
                                .getSubmissionByTopicRegistrationService(topicRegistrationId);
                return ResponseEntity.ok(
                                ApiResponse.success(HttpStatus.OK.value(),
                                                EnumSubmissionSuccess.GET_DETAIL.getMessage(), response));
        }

        // Tạo feedback cho báo
        @PreAuthorize(HAS_ROLE_LECTURER)
        @PostMapping("/feedbacks")
        public ResponseEntity<ApiResponse<SubmissionFeedbackResponse>> createFeedBackSubmissionController(
                        @Valid @RequestBody CreateSubmissionFeedbackRequest request,
                        Authentication authentication) {
                SubmissionFeedbackResponse response = submissionService.createSubmissionFeedbackService(request,
                                authentication.getName());
                return ResponseEntity.status(HttpStatus.CREATED).body(
                                ApiResponse.success(
                                                HttpStatus.CREATED.value(),
                                                EnumSubmissionSuccess.FEEDBACK_CREATED.getMessage(),
                                                response));
        }

        // Lấy feedback cho version báo báo
        @PreAuthorize(HAS_ANY_ROLE_ADMIN_LECTURER_STUDENT)
        @GetMapping("/versions/{submissionVersionId}/feedbacks")
        public ResponseEntity<ApiResponse<List<SubmissionFeedbackResponse>>> getFeedbackSubmissionByVersionIdController(
                        @PathVariable Long submissionVersionId) {
                List<SubmissionFeedbackResponse> response = submissionService
                                .getFeedbacksBySubmissionVersionService(submissionVersionId);
                return ResponseEntity.ok(
                                ApiResponse.success(
                                                HttpStatus.OK.value(),
                                                EnumSubmissionSuccess.GET_FEEDBACKS.getMessage(),
                                                response));

        }

        // Xem feedback theo lecturer
        @PreAuthorize(HAS_ROLE_LECTURER)
        @GetMapping("/feedbacks/me")
        public ResponseEntity<ApiResponse<List<SubmissionFeedbackResponse>>> getFeedbackByLecturerController(
                        Authentication authentication) {
                List<SubmissionFeedbackResponse> response = submissionService
                                .getMyFeedbacksService(authentication.getName());
                return ResponseEntity.ok(
                                ApiResponse.success(
                                                HttpStatus.OK.value(),
                                                EnumSubmissionSuccess.GET_FEEDBACKS.getMessage(),
                                                response));

        }
}
