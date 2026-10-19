package modules.topic_registrations.controller;

import global.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import modules.topic_registrations.dto.CreateTopicRegistrationRequest;
import modules.topic_registrations.dto.RejectTopicRegistrationRequest;
import modules.topic_registrations.dto.TopicRegistrationResponse;
import modules.topic_registrations.entity.TopicRegistration;
import modules.topic_registrations.enums.EnumTopicRegistrationSuccess;
import modules.topic_registrations.service.TopicRegistrationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.method.P;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static global.security.PermissionConstants.HAS_ANY_ROLE_ADMIN_LECTURER;
import static global.security.PermissionConstants.HAS_ANY_ROLE_ADMIN_LECTURER_STUDENT;
import static global.security.PermissionConstants.HAS_ROLE_STUDENT;

@RestController
@RequestMapping("/api/topic-registrations")
@RequiredArgsConstructor

public class TopicRegistrationController {
        private final TopicRegistrationService topicRegistrationService;

        @PreAuthorize(HAS_ROLE_STUDENT)
        @PostMapping
        public ResponseEntity<ApiResponse<TopicRegistrationResponse>> createTopicRegistrationController(
                        @Valid @RequestBody CreateTopicRegistrationRequest request,
                        Authentication authentication) {
                TopicRegistrationResponse response = topicRegistrationService.createTopicRegistrationService(request,
                                authentication.getName());
                return ResponseEntity.status(HttpStatus.CREATED).body(
                                ApiResponse.success(
                                                HttpStatus.CREATED.value(),
                                                EnumTopicRegistrationSuccess.CREATED.getMessage(),
                                                response));
        }

        @PreAuthorize(HAS_ANY_ROLE_ADMIN_LECTURER)
        @GetMapping
        public ResponseEntity<ApiResponse<List<TopicRegistrationResponse>>> getAllTopicRegistrationConntroller() {
                List<TopicRegistrationResponse> response = topicRegistrationService.getAllTopicRegistrationsService();
                return ResponseEntity.ok(
                                ApiResponse.success(
                                                HttpStatus.OK.value(),
                                                EnumTopicRegistrationSuccess.GET_ALL.getMessage(),
                                                response));
        }

        @PreAuthorize(HAS_ANY_ROLE_ADMIN_LECTURER_STUDENT)
        @GetMapping("/{id}")
        public ResponseEntity<ApiResponse<TopicRegistrationResponse>> getTopicRegistrationByIdController(
                        @PathVariable Long id) {
                TopicRegistrationResponse response = topicRegistrationService.getTopicRegistrationByIdService(id);
                return ResponseEntity.ok(
                                ApiResponse.success(
                                                HttpStatus.OK.value(),
                                                EnumTopicRegistrationSuccess.GET_DETAIL.getMessage(),
                                                response));
        }

        @PreAuthorize(HAS_ANY_ROLE_ADMIN_LECTURER)
        @GetMapping("/student-group/{studentGroupId}")
        public ResponseEntity<ApiResponse<List<TopicRegistrationResponse>>> getTopicRegistrationsByStudentGroupIdController(
                        @PathVariable Long studentGroupId) {
                List<TopicRegistrationResponse> response = topicRegistrationService
                                .getByStudentGroupService(studentGroupId);
                return ResponseEntity.ok(
                                ApiResponse.success(
                                                HttpStatus.OK.value(),
                                                EnumTopicRegistrationSuccess.GET_ALL.getMessage(),
                                                response));
        }

        @PreAuthorize(HAS_ANY_ROLE_ADMIN_LECTURER)
        @GetMapping("/topic/{topicId}")
        public ResponseEntity<ApiResponse<List<TopicRegistrationResponse>>> getByTopicController(
                        @PathVariable Long topicId) {
                List<TopicRegistrationResponse> response = topicRegistrationService
                                .getByTopicService(topicId);
                return ResponseEntity.ok(
                                ApiResponse.success(
                                                HttpStatus.OK.value(),
                                                EnumTopicRegistrationSuccess.GET_ALL.getMessage(),
                                                response));
        }

        @PreAuthorize(HAS_ANY_ROLE_ADMIN_LECTURER)
        @PatchMapping("/{id}/approve")
        public ResponseEntity<ApiResponse<TopicRegistrationResponse>> approveTopicRegistrationController(
                        @PathVariable Long id,
                        Authentication authentication) {
                TopicRegistrationResponse response = topicRegistrationService.approveTopicRegistrationService(
                                id,
                                authentication.getName());

                return ResponseEntity.ok(
                                ApiResponse.success(
                                                HttpStatus.OK.value(),
                                                EnumTopicRegistrationSuccess.APPROVED.getMessage(),
                                                response));
        }

        @PreAuthorize(HAS_ANY_ROLE_ADMIN_LECTURER)
        @PatchMapping("/{id}/reject")
        public ResponseEntity<ApiResponse<TopicRegistrationResponse>> rejectTopicRegistrationController(
                        @PathVariable Long id,
                        @Valid @RequestBody RejectTopicRegistrationRequest request,
                        Authentication authentication) {
                TopicRegistrationResponse response = topicRegistrationService.rejectTopicRegistrationService(
                                id,
                                request,
                                authentication.getName());
                return ResponseEntity.ok(
                                ApiResponse.success(
                                                HttpStatus.OK.value(),
                                                EnumTopicRegistrationSuccess.REJECTED.getMessage(),
                                                response));
        }

        @PreAuthorize(HAS_ROLE_STUDENT)
        @PatchMapping("/{id}/cancel")
        public ResponseEntity<ApiResponse<TopicRegistrationResponse>> cancelTopicRegistrationController(
                        @PathVariable Long id,
                        Authentication authentication) {
                TopicRegistrationResponse response = topicRegistrationService.cancelTopicRegistrationService(
                                id,
                                authentication.getName());
                return ResponseEntity.ok(
                                ApiResponse.success(
                                                HttpStatus.OK.value(),
                                                EnumTopicRegistrationSuccess.CANCELLED.getMessage(),
                                                response));
        }

        // SV xem đăng ký đề tài của nhóm mình
        @PreAuthorize(HAS_ROLE_STUDENT)
        @GetMapping("/me")
        public ResponseEntity<ApiResponse<List<TopicRegistrationResponse>>> getMyTopicRegistrationsController(
                        Authentication authentication) {
                List<TopicRegistrationResponse> response = topicRegistrationService
                                .getMyTopicRegistrationService(authentication.getName());
                return ResponseEntity.ok(
                                ApiResponse.success(HttpStatus.OK.value(),
                                                EnumTopicRegistrationSuccess.GET_ALL.getMessage(), response));
        }
}
