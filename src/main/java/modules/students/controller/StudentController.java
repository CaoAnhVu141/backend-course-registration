package modules.students.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import modules.students.dto.RegisterStudentRequest;
import modules.students.dto.StudentResponse;
import modules.students.service.StudentService;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import global.response.ApiResponse;
import modules.students.dto.UpdateStudentRequest;
import modules.students.enums.EnumStudentSuccess;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import java.util.List;

@RestController
@RequestMapping("/api/students")
@RequiredArgsConstructor
public class StudentController {
    private final StudentService studentService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<StudentResponse>> register(@Valid @RequestBody RegisterStudentRequest request) {
        StudentResponse response = studentService.register(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponse.success(
                        HttpStatus.CREATED.value(),
                        EnumStudentSuccess.CREATED.getMessage(),
                        response));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'LECTURER')")
    @GetMapping
    public ResponseEntity<ApiResponse<List<StudentResponse>>> getAllStudentsController() {
        List<StudentResponse> response = studentService.getAllStudentsService();
        return ResponseEntity.ok(
                ApiResponse.success(
                        HttpStatus.OK.value(),
                        EnumStudentSuccess.GET_ALL.getMessage(),
                        response));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'LECTURER')")
    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<StudentResponse>> getStudentByIdController(@PathVariable Long userId) {
        StudentResponse response = studentService.getStudentByIdService(userId);
        return ResponseEntity.ok(
                ApiResponse.success(
                        HttpStatus.OK.value(),
                        EnumStudentSuccess.GET_DETAIL.getMessage(),
                        response));
    }

    // Tra cứu sinh viên theo mã số (dùng khi thêm thành viên nhóm)
    @PreAuthorize("hasAnyRole('ADMIN', 'LECTURER', 'STUDENT')")
    @GetMapping("/by-code/{studentCode}")
    public ResponseEntity<ApiResponse<StudentResponse>> getStudentByCodeController(
            @PathVariable String studentCode) {
        StudentResponse response = studentService.getStudentByCodeService(studentCode);
        return ResponseEntity.ok(
                ApiResponse.success(
                        HttpStatus.OK.value(),
                        EnumStudentSuccess.GET_DETAIL.getMessage(),
                        response));
    }

    @PreAuthorize("hasRole('STUDENT')")
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<StudentResponse>> getMyProfileController(Authentication authentication) {
        StudentResponse response = studentService.getMyProfileService(authentication.getName());

        return ResponseEntity.ok(
                ApiResponse.success(
                        HttpStatus.OK.value(),
                        EnumStudentSuccess.GET_PROFILE.getMessage(),
                        response));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{userId}")
    public ResponseEntity<ApiResponse<StudentResponse>> updateStudentController(
            @PathVariable Long userId,
            @Valid @RequestBody UpdateStudentRequest request) {
        StudentResponse response = studentService.updateStudentService(request, userId);
        return ResponseEntity.ok(
                ApiResponse.success(
                        HttpStatus.OK.value(),
                        EnumStudentSuccess.UPDATED.getMessage(),
                        response));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponse<StudentResponse>> deleteStudentController(@PathVariable Long userId) {
        StudentResponse response = studentService.deleteStudentService(userId);
        return ResponseEntity.ok(
                ApiResponse.success(
                        HttpStatus.OK.value(),
                        EnumStudentSuccess.DELETED.getMessage(),
                        response));
    }
}
