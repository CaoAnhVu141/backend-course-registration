package modules.student_group.controller;

import global.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import modules.student_group.dto.CreateStudentGroupRequest;
import modules.student_group.dto.StudentGroupResponse;
import modules.student_group.enums.EnumStudentGroupSuccess;
import modules.student_group.service.StudentGroupService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;

import static global.security.PermissionConstants.HAS_ROLE_ADMIN;
import static global.security.PermissionConstants.HAS_ROLE_STUDENT;
import static global.security.PermissionConstants.HAS_ANY_ROLE_ADMIN_LECTURER;

@RestController
@RequestMapping("/api/student-groups")
@RequiredArgsConstructor
public class StudentGroupController {

    private final StudentGroupService studentGroupService;

    @PreAuthorize(HAS_ROLE_STUDENT)
    @PostMapping
    public ResponseEntity<ApiResponse<StudentGroupResponse>> createStudentGroupController(
            @Valid @RequestBody CreateStudentGroupRequest request,
            Authentication authentication) {
        StudentGroupResponse response = studentGroupService.createStudentGroupService(request,
                authentication.getName());

        return ResponseEntity.status(HttpStatus.CREATED.value()).body(
                ApiResponse.success(HttpStatus.CREATED.value(),
                        EnumStudentGroupSuccess.CREATED.getMessage(),
                        response));

    }

    @PreAuthorize(HAS_ANY_ROLE_ADMIN_LECTURER)
    @GetMapping
    public ResponseEntity<ApiResponse<List<StudentGroupResponse>>> getAllStudentGroupController() {
        List<StudentGroupResponse> response = studentGroupService.getAllStudentGroupService();
        return ResponseEntity
                .ok(ApiResponse.success(HttpStatus.OK.value(), EnumStudentGroupSuccess.GET_ALL.getMessage(), response));
    }

    // function lấy danh sách sinh viên theo nhóm sinh viên
    @PreAuthorize(HAS_ANY_ROLE_ADMIN_LECTURER)
    @GetMapping("/registration-period/{registrationPeriodId}")
    public ResponseEntity<ApiResponse<List<StudentGroupResponse>>> getStudentGroupsByRegistrationPeriodController(
            @PathVariable Long registrationPeriodId) {
        List<StudentGroupResponse> response = studentGroupService
                .getAllByRegistrationPeriodService(registrationPeriodId);
        return ResponseEntity.ok(
                ApiResponse.success(
                        HttpStatus.OK.value(),
                        EnumStudentGroupSuccess.GET_ALL.getMessage(),
                        response));
    }

    // Sinh viên xem nhóm của chính mình
    @PreAuthorize(HAS_ROLE_STUDENT)
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<List<StudentGroupResponse>>> getMyStudentGroupsController(
            Authentication authentication) {
        List<StudentGroupResponse> response = studentGroupService
                .getMyStudentGroupsService(authentication.getName());
        return ResponseEntity.ok(
                ApiResponse.success(HttpStatus.OK.value(), EnumStudentGroupSuccess.GET_ALL.getMessage(), response));
    }

    @PreAuthorize(HAS_ANY_ROLE_ADMIN_LECTURER)
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<StudentGroupResponse>> getStudentGroupByIdController(@PathVariable Long id) {
        StudentGroupResponse response = studentGroupService.getStudentGroupByIdService(id);
        return ResponseEntity.ok(
                ApiResponse.success(
                        HttpStatus.OK.value(),
                        EnumStudentGroupSuccess.GET_DETAIL.getMessage(),
                        response));
    }

    @PreAuthorize(HAS_ROLE_ADMIN)
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteStudentGroupController(@PathVariable Long id) {
        studentGroupService.deleteStudentGroupService(id);
        return ResponseEntity.ok(
                ApiResponse.success(
                        HttpStatus.OK.value(),
                        EnumStudentGroupSuccess.DELETED.getMessage(),
                        null));
    }
}
