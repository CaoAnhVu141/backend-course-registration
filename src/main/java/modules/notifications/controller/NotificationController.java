package modules.notifications.controller;

import static global.security.PermissionConstants.HAS_ANY_ROLE_ADMIN_LECTURER_STUDENT;
import static global.security.PermissionConstants.HAS_ROLE_ADMIN;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import global.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import modules.notifications.dto.CreateNotificationRequest;
import modules.notifications.dto.NotificationResponse;
import modules.notifications.enums.EnumNotificationSuccess;
import modules.notifications.service.NotificationService;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    // admin đăng thông báo (ALL / STUDENT / LECTURER)
    @PreAuthorize(HAS_ROLE_ADMIN)
    @PostMapping
    public ResponseEntity<ApiResponse<Integer>> createNotificationController(
            @Valid @RequestBody CreateNotificationRequest request) {
        int sent = notificationService.createNotificationService(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponse.success(HttpStatus.CREATED.value(),
                        EnumNotificationSuccess.SENT.getMessage(), sent));
    }

    // thông báo của tôi
    @PreAuthorize(HAS_ANY_ROLE_ADMIN_LECTURER_STUDENT)
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<List<NotificationResponse>>> getMyNotificationsController(
            Authentication authentication) {
        List<NotificationResponse> response = notificationService
                .getMyNotificationsService(authentication.getName());
        return ResponseEntity.ok(
                ApiResponse.success(HttpStatus.OK.value(),
                        EnumNotificationSuccess.GET_ALL.getMessage(), response));
    }

    // số thông báo chưa đọc
    @PreAuthorize(HAS_ANY_ROLE_ADMIN_LECTURER_STUDENT)
    @GetMapping("/me/unread-count")
    public ResponseEntity<ApiResponse<Long>> getUnreadCountController(Authentication authentication) {
        long count = notificationService.getUnreadCountService(authentication.getName());
        return ResponseEntity.ok(
                ApiResponse.success(HttpStatus.OK.value(),
                        EnumNotificationSuccess.GET_UNREAD_COUNT.getMessage(), count));
    }

    // đánh dấu 1 thông báo đã đọc
    @PreAuthorize(HAS_ANY_ROLE_ADMIN_LECTURER_STUDENT)
    @PatchMapping("/{id}/read")
    public ResponseEntity<ApiResponse<NotificationResponse>> markAsReadController(
            @PathVariable Long id,
            Authentication authentication) {
        NotificationResponse response = notificationService.markAsReadService(id, authentication.getName());
        return ResponseEntity.ok(
                ApiResponse.success(HttpStatus.OK.value(),
                        EnumNotificationSuccess.MARKED_READ.getMessage(), response));
    }
}
