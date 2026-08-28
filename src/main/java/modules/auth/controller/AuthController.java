package modules.auth.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import modules.auth.dto.*;
import modules.auth.service.AuthService;
import modules.lecturers.dto.LecturerResponse;
import modules.lecturers.dto.RegisterLecturerRequest;
import modules.lecturers.service.LecturerService;
import modules.students.dto.RegisterStudentRequest;
import modules.students.dto.StudentResponse;
import modules.students.service.StudentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final StudentService studentService;
    private final LecturerService lecturerService;

    @PostMapping("/register/student")
    public ResponseEntity<StudentResponse> registerStudentExplicitly(
            @Valid @RequestBody RegisterStudentRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(studentService.register(request));
    }

    @PostMapping("/register/lecturer")
    public ResponseEntity<LecturerResponse> registerLecturer(@Valid @RequestBody RegisterLecturerRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(lecturerService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        LoginResponse loginResponse = authService.loginService(loginRequest);
        return ResponseEntity.ok(loginResponse);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<LoginResponse> refreshToken(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest) {
        LoginResponse loginResponse = authService.refreshToken(refreshTokenRequest);
        return ResponseEntity.ok(loginResponse);
    }

    // Đăng kí tài khoản Admin
    @PostMapping("/register/admin")
    public ResponseEntity<RegisterResponse> registerAdmin(@Valid @RequestBody RegisterRequest request) {
        RegisterResponse registerResponse = authService.registerAdmin(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(registerResponse);
    }
}