package modules.defense_committees.controller;

import global.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import modules.defense_committees.dto.AddCommitteeMemberRequest;
import modules.defense_committees.dto.CommitteeMemberResponse;
import modules.defense_committees.dto.CreateDefenseCommitteeRequest;
import modules.defense_committees.dto.DefenseCommitteeResponse;
import modules.defense_committees.enums.EnumDefenseCommitteeSuccess;
import modules.defense_committees.service.DefenseCommitteeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import static global.security.PermissionConstants.HAS_ANY_ROLE_ADMIN_LECTURER;
import static global.security.PermissionConstants.HAS_ROLE_ADMIN;
import static global.security.PermissionConstants.HAS_ROLE_LECTURER;

import org.springframework.security.access.prepost.PreAuthorize;

@RestController
@RequestMapping("/api/defense-committees")
@RequiredArgsConstructor
public class DefenseCommitteeController {
    private final DefenseCommitteeService defenseCommitteeService;

    @PreAuthorize(HAS_ROLE_ADMIN)
    @PostMapping
    public ResponseEntity<ApiResponse<DefenseCommitteeResponse>> createDefenseCommitteeController(
            @Valid @RequestBody CreateDefenseCommitteeRequest request,
            Authentication authentication) {
        DefenseCommitteeResponse response = defenseCommitteeService.createDefenseCommitteeService(request,
                authentication.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponse.success(
                        HttpStatus.CREATED.value(),
                        EnumDefenseCommitteeSuccess.CREATED.getMessage(),
                        response));
    }

    @PreAuthorize(HAS_ROLE_ADMIN)
    @PostMapping("/{id}/members")
    public ResponseEntity<ApiResponse<DefenseCommitteeResponse>> createCommitteeMemberController(@PathVariable Long id,
            @Valid @RequestBody AddCommitteeMemberRequest request) {
        DefenseCommitteeResponse response = defenseCommitteeService.createMemberCommitteeService(id, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponse.success(
                        HttpStatus.CREATED.value(),
                        EnumDefenseCommitteeSuccess.MEMBER_ADDED.getMessage(),
                        response));
    }

    @PreAuthorize(HAS_ANY_ROLE_ADMIN_LECTURER)
    @GetMapping
    public ResponseEntity<ApiResponse<List<DefenseCommitteeResponse>>> getAllDefenseeCommitteeController() {
        List<DefenseCommitteeResponse> response = defenseCommitteeService.getAllDefenseCommitteesService();
        return ResponseEntity.ok(
                ApiResponse.success(HttpStatus.OK.value(), EnumDefenseCommitteeSuccess.GET_ALL.getMessage(), response));
    }

    @PreAuthorize(HAS_ANY_ROLE_ADMIN_LECTURER)
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<DefenseCommitteeResponse>> getDefenseCommitteeByIdController(
            @PathVariable Long id) {
        DefenseCommitteeResponse response = defenseCommitteeService.getDefenseCommitteByIdService(id);
        return ResponseEntity.ok(
                ApiResponse.success(HttpStatus.OK.value(), EnumDefenseCommitteeSuccess.GET_DETAIL.getMessage(),
                        response));
    }

    @PreAuthorize(HAS_ANY_ROLE_ADMIN_LECTURER)
    @GetMapping("/registration-period/{registrationPeriodId}")
    public ResponseEntity<ApiResponse<List<DefenseCommitteeResponse>>> getByRegistrationPeriodController(
            @PathVariable Long registrationPeriodId) {
        List<DefenseCommitteeResponse> response = defenseCommitteeService
                .getAllByRegistrationPeriodService(registrationPeriodId);
        return ResponseEntity.ok(
                ApiResponse.success(
                        HttpStatus.OK.value(),
                        EnumDefenseCommitteeSuccess.GET_ALL.getMessage(),
                        response));
    }

    @PreAuthorize(HAS_ROLE_LECTURER)
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<List<CommitteeMemberResponse>>> getMyCommitteeMembersController(
            Authentication authentication) {

        List<CommitteeMemberResponse> response = defenseCommitteeService
                .getMyCommitteeMembersService(authentication.getName());
        return ResponseEntity.ok(
                ApiResponse.success(
                        HttpStatus.OK.value(),
                        EnumDefenseCommitteeSuccess.GET_DETAIL.getMessage(),
                        response));
    }

    @PreAuthorize(HAS_ROLE_ADMIN)
    @PatchMapping("/{id}/close")
    public ResponseEntity<ApiResponse<DefenseCommitteeResponse>> closeDefenseeCommitteeController(
            @PathVariable Long id) {
        DefenseCommitteeResponse response = defenseCommitteeService.closeDefenseCommitteeService(id);
        return ResponseEntity.ok(
                ApiResponse.success(HttpStatus.OK.value(),
                        EnumDefenseCommitteeSuccess.CLOSED.getMessage(), response));
    }
}
