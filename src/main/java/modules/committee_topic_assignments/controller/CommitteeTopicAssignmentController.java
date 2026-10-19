package modules.committee_topic_assignments.controller;

import static global.security.PermissionConstants.HAS_ANY_ROLE_ADMIN_LECTURER;
import static global.security.PermissionConstants.HAS_ROLE_ADMIN;
import static global.security.PermissionConstants.HAS_ROLE_LECTURER;
import static global.security.PermissionConstants.HAS_ROLE_STUDENT;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import global.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import modules.committee_topic_assignments.dto.CommitteeTopicAssignmentResponse;
import modules.committee_topic_assignments.dto.CreateCommitteeTopicAssignmentRequest;
import modules.committee_topic_assignments.dto.UpdateDefenseScheduleRequest;
import modules.committee_topic_assignments.enums.EnumCommitteeTopicAssignmentSuccess;
import modules.committee_topic_assignments.service.CommitteeTopicAssignmentService;
import modules.students.export.ExportExcelService;

@RestController
@RequestMapping("/api/committee-topic-assignments")
@RequiredArgsConstructor
public class CommitteeTopicAssignmentController {

        private final CommitteeTopicAssignmentService committeeTopicAssignmentService;

        @PreAuthorize(HAS_ROLE_ADMIN)
        @PostMapping
        public ResponseEntity<ApiResponse<CommitteeTopicAssignmentResponse>> createCommitteeTopiccAssignmentController(
                        @Valid @RequestBody CreateCommitteeTopicAssignmentRequest request,
                        Authentication authentication) {
                CommitteeTopicAssignmentResponse response = committeeTopicAssignmentService
                                .createCommitteeTopicAssignmentService(request, authentication.getName());
                return ResponseEntity.status(HttpStatus.CREATED).body(
                                ApiResponse.success(HttpStatus.CREATED.value(),
                                                EnumCommitteeTopicAssignmentSuccess.CREATED.getMessage(),
                                                response));
        }

        @PreAuthorize(HAS_ROLE_ADMIN)
        @PutMapping("/{id}/schedule")
        public ResponseEntity<ApiResponse<CommitteeTopicAssignmentResponse>> updateDefenseScheduleController(
                        @PathVariable Long id, @Valid @RequestBody UpdateDefenseScheduleRequest request) {
                CommitteeTopicAssignmentResponse response = committeeTopicAssignmentService
                                .updateDefenseScheduleService(id, request);
                return ResponseEntity.ok(
                                ApiResponse.success(
                                                HttpStatus.OK.value(),
                                                EnumCommitteeTopicAssignmentSuccess.SCHEDULE_UPDATED.getMessage(),
                                                response));
        }

        // lấy toàn bộ phân công
        @PreAuthorize(HAS_ROLE_ADMIN)
        @GetMapping
        public ResponseEntity<ApiResponse<List<CommitteeTopicAssignmentResponse>>> getAllCommitteeTopicAssignmentController() {
                List<CommitteeTopicAssignmentResponse> response = committeeTopicAssignmentService
                                .getAllCommitteeTopicAssignmentService();
                return ResponseEntity.ok(
                                ApiResponse.success(HttpStatus.OK.value(),
                                                EnumCommitteeTopicAssignmentSuccess.GET_ALL.getMessage(), response));
        }

        @PreAuthorize(HAS_ROLE_ADMIN)
        @GetMapping("/export")
        public ResponseEntity<byte[]> exportCommitteeTopicAssignmentsController() {
                return ExportExcelService.download(
                                committeeTopicAssignmentService.exportCommitteeTopicAssignmentsToExcelService(),
                                "phan-cong-de-tai");
        }

        // xem chi tiết
        @PreAuthorize(HAS_ANY_ROLE_ADMIN_LECTURER)
        @GetMapping("/{id}")
        public ResponseEntity<ApiResponse<CommitteeTopicAssignmentResponse>> getCommitteeTopicAssignmentByIdService(
                        @PathVariable Long id) {
                CommitteeTopicAssignmentResponse response = committeeTopicAssignmentService
                                .getCommitteeTopicAssignmentByIdService(id);
                return ResponseEntity.ok(
                                ApiResponse.success(HttpStatus.OK.value(),
                                                EnumCommitteeTopicAssignmentSuccess.GET_DETAIL.getMessage(), response));
        }

        // hoàn thành bảo vệ đồ án xong
        @PreAuthorize(HAS_ROLE_ADMIN)
        @PatchMapping("/{id}/complete")
        public ResponseEntity<ApiResponse<CommitteeTopicAssignmentResponse>> completeCommitteeTopicAssignmentController(
                        @PathVariable Long id) {
                CommitteeTopicAssignmentResponse response = committeeTopicAssignmentService
                                .completeCommitteeTopicAssignmentService(id);
                return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(),
                                EnumCommitteeTopicAssignmentSuccess.COMPLETED.getMessage(), response));
        }

        @PreAuthorize(HAS_ROLE_ADMIN)
        @PatchMapping("/{id}/cancel")
        public ResponseEntity<ApiResponse<CommitteeTopicAssignmentResponse>> cancelCommitteeTopicAssignmentController(
                        @PathVariable Long id) {
                CommitteeTopicAssignmentResponse response = committeeTopicAssignmentService
                                .cancelCommitteeTopicAssignmentService(id);
                return ResponseEntity.ok(
                                ApiResponse.success(HttpStatus.OK.value(),
                                                EnumCommitteeTopicAssignmentSuccess.CANCELLED.getMessage(), response));
        }

        // lấy danh sách đề tài mà hội đồng chấm
        @PreAuthorize(HAS_ANY_ROLE_ADMIN_LECTURER)
        @GetMapping("/defense-committee/{defenseCommitteeId}")
        public ResponseEntity<ApiResponse<List<CommitteeTopicAssignmentResponse>>> getByDefenseCommitteeController(
                        @PathVariable Long defenseCommitteeId) {
                List<CommitteeTopicAssignmentResponse> response = committeeTopicAssignmentService
                                .getAllByDefenseCommitteeService(defenseCommitteeId);
                return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(),
                                EnumCommitteeTopicAssignmentSuccess.GET_ALL.getMessage(), response));
        }

        // giảng viên xem lịch chấm của mình
        @PreAuthorize(HAS_ROLE_LECTURER)
        @GetMapping("/me")
        public ResponseEntity<ApiResponse<List<CommitteeTopicAssignmentResponse>>> getMyAssignmentsAsLecturerController(
                        Authentication authentication) {
                List<CommitteeTopicAssignmentResponse> response = committeeTopicAssignmentService
                                .getMyAssignmentsAsLecturerService(authentication.getName());
                return ResponseEntity.ok(
                                ApiResponse.success(HttpStatus.OK.value(),
                                                EnumCommitteeTopicAssignmentSuccess.GET_ALL.getMessage(), response));
        }

        // xem theo nhóm sinh viên
        @PreAuthorize(HAS_ROLE_STUDENT)
        @GetMapping("/my-group")
        public ResponseEntity<ApiResponse<List<CommitteeTopicAssignmentResponse>>> getMyGroupAssignmentsController(
                        Authentication authentication) {
                List<CommitteeTopicAssignmentResponse> response = committeeTopicAssignmentService
                                .getMyGroupAssignmentsAsStudentService(authentication.getName());
                return ResponseEntity.ok(
                                ApiResponse.success(HttpStatus.OK.value(),
                                                EnumCommitteeTopicAssignmentSuccess.GET_ALL.getMessage(), response));
        }
}
