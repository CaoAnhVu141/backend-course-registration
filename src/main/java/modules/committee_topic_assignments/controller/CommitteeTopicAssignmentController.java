package modules.committee_topic_assignments.controller;

import static global.security.PermissionConstants.HAS_ROLE_ADMIN;

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
import modules.committee_topic_assignments.enums.EnumCommitteeTopicAssignmentSuccess;
import modules.committee_topic_assignments.service.CommitteeTopicAssignmentService;
import modules.defense_committees.dto.CommitteeMemberResponse;

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
}
