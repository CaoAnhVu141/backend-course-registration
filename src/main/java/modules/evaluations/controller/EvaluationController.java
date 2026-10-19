package modules.evaluations.controller;

import static global.security.PermissionConstants.HAS_ANY_ROLE_ADMIN_LECTURER;
import static global.security.PermissionConstants.HAS_ANY_ROLE_ADMIN_LECTURER_STUDENT;
import static global.security.PermissionConstants.HAS_ROLE_LECTURER;
import static global.security.PermissionConstants.HAS_ROLE_STUDENT;
import modules.evaluations.service.EvaluationService;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import global.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import modules.evaluations.dto.CreateEvaluationRequest;
import modules.evaluations.dto.EvaluationResponse;
import modules.evaluations.dto.FinalResultResponse;
import modules.evaluations.enums.EnumEvaluationSuccess;

@RestController
@RequestMapping("/api/evaluations")
@RequiredArgsConstructor
public class EvaluationController {

    private final EvaluationService evaluationService;

    //
    @PreAuthorize(HAS_ROLE_LECTURER)
    @PostMapping
    public ResponseEntity<ApiResponse<EvaluationResponse>> createEvaluationController(
            @Valid @RequestBody CreateEvaluationRequest request,
            Authentication authentication) {
        EvaluationResponse response = evaluationService.createEvaluationService(request, authentication.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponse.success(HttpStatus.CREATED.value(), EnumEvaluationSuccess.EVALUATED.getMessage(),
                        response));
    }

    // Chủ tịch công bố
    @PreAuthorize(HAS_ROLE_LECTURER)
    @PostMapping("/assignments/{assignmentId}/publish")
    public ResponseEntity<ApiResponse<FinalResultResponse>> publishFinalResultController(
            @PathVariable Long assignmentId, Authentication authentication) {
        FinalResultResponse response = evaluationService.publishFinalResultService(
                assignmentId,
                authentication.getName());

        return ResponseEntity.ok(
                ApiResponse.success(HttpStatus.OK.value(), EnumEvaluationSuccess.RESULT_PUBLISHED.getMessage(),
                        response));
    }

    // Xem danh sách điểm của sự phân công
    @PreAuthorize(HAS_ANY_ROLE_ADMIN_LECTURER)
    @GetMapping("/assignments/{assignmentId}")
    public ResponseEntity<ApiResponse<List<EvaluationResponse>>> getEvaluationsByAssignmentController(
            @PathVariable Long assignmentId) {
        List<EvaluationResponse> response = evaluationService.getEvaluationsByAssignmentService(assignmentId);
        return ResponseEntity.ok(
                ApiResponse.success(HttpStatus.OK.value(), EnumEvaluationSuccess.GET_ALL.getMessage(), response));
    }

    // Xem kết quả cuối cùng
    @PreAuthorize(HAS_ANY_ROLE_ADMIN_LECTURER_STUDENT)
    @GetMapping("/assignments/{assignmentId}/result")
    public ResponseEntity<ApiResponse<FinalResultResponse>> getFinalResultByAssignmentController(
            @PathVariable Long assignmentId) {
        FinalResultResponse response = evaluationService.getFinalResultByAssignmentService(assignmentId);
        return ResponseEntity.ok(
                ApiResponse.success(HttpStatus.OK.value(), EnumEvaluationSuccess.GET_RESULT.getMessage(), response));
    }

    // lấy theo nhóm sinh viên
    @PreAuthorize(HAS_ROLE_STUDENT)
    @GetMapping("/my-group")
    public ResponseEntity<ApiResponse<List<FinalResultResponse>>> getMyGroupFinalResultController(
            Authentication authentication) {
        List<FinalResultResponse> response = evaluationService.getMyGroupFinalResultService(authentication.getName());
        return ResponseEntity.ok(
                ApiResponse.success(HttpStatus.OK.value(), EnumEvaluationSuccess.GET_RESULT.getMessage(), response));
    }
}
