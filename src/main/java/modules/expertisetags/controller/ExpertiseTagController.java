package modules.expertisetags.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import modules.expertisetags.dto.CreateExpertiseTagRequest;
import modules.expertisetags.dto.ExpertiseTagResponse;
import modules.expertisetags.dto.UpdateExpertiseTagRequest;
import modules.expertisetags.service.ExpertiseTagService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/expertise-tags")
@RequiredArgsConstructor
public class ExpertiseTagController {

    private final ExpertiseTagService expertiseTagService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<ExpertiseTagResponse> createExpertiseTagController(
            @Valid @RequestBody CreateExpertiseTagRequest request) {
        ExpertiseTagResponse response = expertiseTagService.createExpertiseTagService(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<ExpertiseTagResponse>> getAllExpertiseTagsController() {
        return ResponseEntity.ok(expertiseTagService.getAllExpertiseTagsService());
    }
}
