package modules.lecturers.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import modules.lecturers.dto.*;
import modules.lecturers.service.LecturerService;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import global.response.ApiResponse;
import modules.lecturers.enums.EnumLecturerSuccess;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;

import java.util.List;

@RestController
@RequestMapping("/api/lecturers")
@RequiredArgsConstructor
public class LecturerController {
    private final LecturerService lecturerService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<LecturerResponse>> register(@Valid @RequestBody RegisterLecturerRequest request) {
        LecturerResponse response = lecturerService.register(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponse.success(
                        HttpStatus.CREATED.value(),
                        EnumLecturerSuccess.CREATED.getMessage(),
                        response));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<ApiResponse<List<LecturerResponse>>> getAllLecturerController() {
        List<LecturerResponse> response = lecturerService.getAllLecturerService();

        return ResponseEntity.ok(
                ApiResponse.success(
                        HttpStatus.OK.value(),
                        EnumLecturerSuccess.GET_ALL.getMessage(),
                        response));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<LecturerResponse>> getLecturerByIdController(@PathVariable Long userId) {
        LecturerResponse response = lecturerService.getLecturerByIdService(userId);

        return ResponseEntity.ok(
                ApiResponse.success(
                        HttpStatus.OK.value(),
                        EnumLecturerSuccess.GET_DETAIL.getMessage(),
                        response));
    }

    @PreAuthorize("hasRole('LECTURER')")
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<LecturerResponse>> getMyProfileController(Authentication authentication) {
        LecturerResponse response = lecturerService.getMyProfileService(authentication.getName());

        return ResponseEntity.ok(
                ApiResponse.success(
                        HttpStatus.OK.value(),
                        EnumLecturerSuccess.GET_PROFILE.getMessage(),
                        response));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{userId}")
    public ResponseEntity<ApiResponse<LecturerResponse>> updateLecturerController(
            @PathVariable Long userId,
            @Valid @RequestBody UpdateLecturerRequest request) {
        LecturerResponse response = lecturerService.updateLecturerService(userId, request);

        return ResponseEntity.ok(
                ApiResponse.success(
                        HttpStatus.OK.value(),
                        EnumLecturerSuccess.UPDATED.getMessage(),
                        response));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponse<Void>> deleteLecturerController(@PathVariable Long userId) {
        lecturerService.deleteLecturerService(userId);

        return ResponseEntity.ok(
                ApiResponse.success(
                        HttpStatus.OK.value(),
                        EnumLecturerSuccess.DELETED.getMessage(),
                        null));
    }

}
