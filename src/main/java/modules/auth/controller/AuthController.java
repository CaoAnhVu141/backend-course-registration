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
import global.response.ApiResponse;
import modules.auth.enums.EnumAuthSuccess;

@RestController
@RequestMapping("api/auth")
@RequiredArgsConstructor
public class AuthController {

        private final AuthService authService;
        private final StudentService studentService;
        private final LecturerService lecturerService;

        @PostMapping("/register/student")
        public ResponseEntity<ApiResponse<StudentResponse>> registerStudentExplicitly(
                        @Valid @RequestBody RegisterStudentRequest request) {
                StudentResponse response = studentService.register(request);
                return ResponseEntity.status(HttpStatus.CREATED).body(
                                ApiResponse.success(
                                                HttpStatus.CREATED.value(),
                                                EnumAuthSuccess.REGISTER_STUDENT.getMessage(),
                                                response));
        }

        @PostMapping("/register/lecturer")
        public ResponseEntity<ApiResponse<LecturerResponse>> registerLecturer(
                        @Valid @RequestBody RegisterLecturerRequest request) {
                LecturerResponse response = lecturerService.register(request);
                return ResponseEntity.status(HttpStatus.CREATED).body(
                                ApiResponse.success(
                                                HttpStatus.CREATED.value(),
                                                EnumAuthSuccess.REGISTER_LECTURER.getMessage(),
                                                response));
        }

        @PostMapping("/login")
        public ResponseEntity<ApiResponse<LoginResponse>> login(@Valid @RequestBody LoginRequest loginRequest) {
                LoginResponse response = authService.loginService(loginRequest);
                return ResponseEntity.ok(
                                ApiResponse.success(
                                                HttpStatus.OK.value(),
                                                EnumAuthSuccess.LOGIN.getMessage(),
                                                response));
        }

        @PostMapping("/refresh-token")
        public ResponseEntity<ApiResponse<LoginResponse>> refreshToken(
                        @Valid @RequestBody RefreshTokenRequest refreshTokenRequest) {
                LoginResponse response = authService.refreshToken(refreshTokenRequest);
                return ResponseEntity.ok(
                                ApiResponse.success(
                                                HttpStatus.OK.value(),
                                                EnumAuthSuccess.REFRESH_TOKEN.getMessage(),
                                                response));
        }

        // Đăng kí tài khoản Admin
        @PostMapping("/register/admin")
        public ResponseEntity<ApiResponse<RegisterResponse>> registerAdmin(
                        @Valid @RequestBody RegisterRequest request) {
                RegisterResponse response = authService.registerAdmin(request);
                return ResponseEntity.status(HttpStatus.CREATED).body(
                                ApiResponse.success(
                                                HttpStatus.CREATED.value(),
                                                EnumAuthSuccess.REGISTER_ADMIN.getMessage(),
                                                response));
        }

        @PostMapping("/logout")
        public ResponseEntity<ApiResponse<Void>> logoutController(@Valid @RequestBody LogoutRequest request) {
                authService.logoutService(request);
                return ResponseEntity.ok(
                                ApiResponse.success(
                                                HttpStatus.OK.value(),
                                                EnumAuthSuccess.LOGOUT.getMessage(),
                                                null));
        }
}