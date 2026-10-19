package modules.topics.controller;

import global.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import modules.topics.dto.CreateTopicRequest;
import modules.topics.dto.RejectTopicRequest;
import modules.topics.dto.TopicResponse;
import modules.topics.dto.UpdateTopicRequest;
import modules.topics.enums.EnumTopicSuccess;
import modules.topics.service.TopicService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static global.security.PermissionConstants.HAS_ROLE_ADMIN;
import static global.security.PermissionConstants.HAS_ROLE_LECTURER;
import static global.security.PermissionConstants.HAS_ROLE_STUDENT;
import static global.security.PermissionConstants.HAS_ANY_ROLE_ADMIN_LECTURER;

@Controller
@RequestMapping("/api/topics")
@RequiredArgsConstructor
public class TopicController {
        private final TopicService topicService;

        @PreAuthorize(HAS_ROLE_LECTURER)
        @PostMapping
        public ResponseEntity<ApiResponse<TopicResponse>> createTopicController(
                        @Valid @RequestBody CreateTopicRequest request,
                        Authentication authentication) {
                TopicResponse response = topicService.createTopicService(request, authentication.getName());

                return ResponseEntity.status(HttpStatus.CREATED).body(
                                ApiResponse.success(
                                                HttpStatus.CREATED.value(),
                                                EnumTopicSuccess.CREATED.getMessage(),
                                                response));
        }

        @PreAuthorize(HAS_ANY_ROLE_ADMIN_LECTURER)
        @GetMapping
        public ResponseEntity<ApiResponse<List<TopicResponse>>> getAllTopicController() {
                List<TopicResponse> response = topicService.getAllTopicsService();

                return ResponseEntity.ok(
                                ApiResponse.success(
                                                HttpStatus.OK.value(),
                                                EnumTopicSuccess.GET_ALL.getMessage(),
                                                response));
        }

        @PreAuthorize(HAS_ROLE_STUDENT)
        @GetMapping("/published")
        public ResponseEntity<ApiResponse<List<TopicResponse>>> getPublishedTopicsController() {
                List<TopicResponse> response = topicService.getTopicPublishedService();

                return ResponseEntity.ok(
                                ApiResponse.success(
                                                HttpStatus.OK.value(),
                                                EnumTopicSuccess.GET_ALL.getMessage(),
                                                response));
        }

        @PreAuthorize(HAS_ANY_ROLE_ADMIN_LECTURER)
        @GetMapping("/{id}")
        public ResponseEntity<ApiResponse<TopicResponse>> getTopicByIdController(@PathVariable Long id) {
                TopicResponse response = topicService.getTopicByIdService(id);

                return ResponseEntity.ok(
                                ApiResponse.success(
                                                HttpStatus.OK.value(),
                                                EnumTopicSuccess.GET_DETAIL.getMessage(),
                                                response));
        }

        @PreAuthorize(HAS_ROLE_LECTURER)
        @GetMapping("/me")
        public ResponseEntity<ApiResponse<List<TopicResponse>>> getMyTopicsController(Authentication authentication) {
                List<TopicResponse> response = topicService.getMyTopicService(authentication.getName());

                return ResponseEntity.ok(
                                ApiResponse.success(
                                                HttpStatus.OK.value(),
                                                EnumTopicSuccess.GET_MY_TOPICS.getMessage(),
                                                response));
        }

        @PreAuthorize(HAS_ANY_ROLE_ADMIN_LECTURER)
        @PutMapping("/{id}")
        public ResponseEntity<ApiResponse<TopicResponse>> updateTopicController(
                        @PathVariable Long id,
                        @Valid @RequestBody UpdateTopicRequest request) {
                TopicResponse response = topicService.updateTopicService(id, request);

                return ResponseEntity.ok(
                                ApiResponse.success(
                                                HttpStatus.OK.value(),
                                                EnumTopicSuccess.UPDATED.getMessage(),
                                                response));
        }

        @PreAuthorize(HAS_ROLE_ADMIN)
        @PatchMapping("/{id}/approve")
        public ResponseEntity<ApiResponse<TopicResponse>> approveTopicController(@PathVariable Long id) {
                TopicResponse response = topicService.approveTopicService(id);

                return ResponseEntity.ok(
                                ApiResponse.success(
                                                HttpStatus.OK.value(),
                                                EnumTopicSuccess.APPROVED.getMessage(),
                                                response));
        }

        @PreAuthorize(HAS_ROLE_ADMIN)
        @PatchMapping("/{id}/reject")
        public ResponseEntity<ApiResponse<TopicResponse>> rejectTopicController(
                        @PathVariable Long id,
                        @Valid @RequestBody RejectTopicRequest request) {
                TopicResponse response = topicService.rejectTopicService(id, request.getReason());

                return ResponseEntity.ok(
                                ApiResponse.success(
                                                HttpStatus.OK.value(),
                                                EnumTopicSuccess.REJECTED.getMessage(),
                                                response));
        }

        @PreAuthorize(HAS_ROLE_ADMIN)
        @PatchMapping("/{id}/publish")
        public ResponseEntity<ApiResponse<TopicResponse>> publishTopicController(@PathVariable Long id) {
                TopicResponse response = topicService.publishTopicService(id);

                return ResponseEntity.ok(
                                ApiResponse.success(
                                                HttpStatus.OK.value(),
                                                EnumTopicSuccess.PUBLISHED.getMessage(),
                                                response));
        }

        @PreAuthorize(HAS_ROLE_ADMIN)
        @DeleteMapping("/{id}")
        public ResponseEntity<ApiResponse<Void>> deleteTopicController(@PathVariable Long id) {
                topicService.deleteTopicService(id);

                return ResponseEntity.ok(
                                ApiResponse.success(
                                                HttpStatus.OK.value(),
                                                EnumTopicSuccess.DELETED.getMessage(),
                                                null));
        }

}
