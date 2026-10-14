package modules.evaluations.controller;

import static global.security.PermissionConstants.HAS_ANY_ROLE_ADMIN_LECTURER;
import static global.security.PermissionConstants.HAS_ANY_ROLE_ADMIN_LECTURER_STUDENT;
import static global.security.PermissionConstants.HAS_ROLE_LECTURER;
import static global.security.PermissionConstants.HAS_ROLE_STUDENT;
import modules.auth.repository.RefreshTokenRepository;
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

}
