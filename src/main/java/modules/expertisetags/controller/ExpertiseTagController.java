package modules.expertisetags.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import modules.expertisetags.dto.CreateExpertiseTagRequest;
import modules.expertisetags.dto.ExpertiseTagResponse;
import modules.expertisetags.dto.UpdateExpertiseTagRequest;
import modules.expertisetags.service.ExpertiseTagService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.method.P;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import modules.expertisetags.enums.EnumExpertiseTagSuccess;
import global.response.ApiResponse;

@RestController
@RequestMapping("/api/expertise-tags")
@RequiredArgsConstructor
public class ExpertiseTagController {

    private final ExpertiseTagService expertiseTagService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<ApiResponse<ExpertiseTagResponse>> createExpertiseTagController(
            @Valid @RequestBody CreateExpertiseTagRequest request) {
        ExpertiseTagResponse response = expertiseTagService.createExpertiseTagService(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(HttpStatus.CREATED.value(),
                EnumExpertiseTagSuccess.CREATED.getMessage(), response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ExpertiseTagResponse>>> getAllExpertiseTagsController() {
        List<ExpertiseTagResponse> response = expertiseTagService.getAllExpertiseTagsService();
        return ResponseEntity.ok(
                ApiResponse.success(
                        HttpStatus.OK.value(),
                        EnumExpertiseTagSuccess.GET_ALL.getMessage(),
                        response));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ExpertiseTagResponse>> getExpertiseTagByIdController(@PathVariable Long id) {
        ExpertiseTagResponse response = expertiseTagService.getExpertiseTagByIdService(id);
        return ResponseEntity.ok(
                ApiResponse.success(
                        HttpStatus.OK.value(),
                        EnumExpertiseTagSuccess.GET_DETAIL.getMessage(),
                        response));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ExpertiseTagResponse>> updateExpertiseTagController(
            @PathVariable Long id,
            @Valid @RequestBody UpdateExpertiseTagRequest request) {
        ExpertiseTagResponse response = expertiseTagService.updateExpertiseTagService(request, id);
        return ResponseEntity.ok(
                ApiResponse.success(
                        HttpStatus.OK.value(),
                        EnumExpertiseTagSuccess.UPDATED.getMessage(),
                        response));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteExpertiseTagController(@PathVariable Long id) {
        expertiseTagService.deleteExpertiseTagService(id);
        return ResponseEntity.ok(
                ApiResponse.success(
                        HttpStatus.OK.value(),
                        EnumExpertiseTagSuccess.DELETED.getMessage(),
                        null));
    }
}
