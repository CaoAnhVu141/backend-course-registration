package modules.registration_period.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import jakarta.validation.Valid;
import modules.registration_period.dto.CreateRegistrationPeriodRequest;
import modules.registration_period.dto.RegistrationPeriodResponse;
import modules.registration_period.dto.UpdateRegistrationPeriodRequest;
import modules.registration_period.enums.EnumRegistrationPeriodSuccess;
import modules.registration_period.service.RegistrationPeriodService;

@RestController
@RequestMapping("/api/registration-periods")
@RequiredArgsConstructor
public class RegistrationPeriodController {

        private final RegistrationPeriodService registrationPeriodService;

        @PreAuthorize("hasRole('ADMIN')")
        @PostMapping
        public ResponseEntity<ApiResponse<RegistrationPeriodResponse>> createRegistrationPeriodController(
                        @Valid @RequestBody CreateRegistrationPeriodRequest request) {
                RegistrationPeriodResponse response = registrationPeriodService
                                .createRegistrationPeriodService(request);
                return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(HttpStatus.CREATED.value(),
                                EnumRegistrationPeriodSuccess.CREATED.getMessage(), response));
        }

        @GetMapping
        public ResponseEntity<ApiResponse<List<RegistrationPeriodResponse>>> getAllRegistrationPeriodsController() {
                List<RegistrationPeriodResponse> response = registrationPeriodService.getAllRegistrationPeriodService();
                return ResponseEntity.ok(
                                ApiResponse.success(
                                                HttpStatus.OK.value(),
                                                EnumRegistrationPeriodSuccess.GET_ALL.getMessage(),
                                                response));
        }

        @GetMapping("/{id}")
        public ResponseEntity<ApiResponse<RegistrationPeriodResponse>> getRegistrationPeriodByIdController(
                        @PathVariable Long id) {
                RegistrationPeriodResponse response = registrationPeriodService.getRegistrationPeriodByIdService(id);
                return ResponseEntity.ok(
                                ApiResponse.success(
                                                HttpStatus.OK.value(),
                                                EnumRegistrationPeriodSuccess.GET_DETAIL.getMessage(),
                                                response));
        }

        @PreAuthorize("hasRole('ADMIN')")
        @PutMapping("/{id}")
        public ResponseEntity<ApiResponse<RegistrationPeriodResponse>> updateRegistrationPeriodController(
                        @PathVariable Long id,
                        @Valid @RequestBody UpdateRegistrationPeriodRequest request) {
                RegistrationPeriodResponse response = registrationPeriodService.updateRegistrationPeriodService(id,
                                request);
                return ResponseEntity.ok(
                                ApiResponse.success(
                                                HttpStatus.OK.value(),
                                                EnumRegistrationPeriodSuccess.UPDATED.getMessage(),
                                                response));
        }

        @PreAuthorize("hasRole('ADMIN')")
        @DeleteMapping("/{id}")
        public ResponseEntity<ApiResponse<Void>> deleteRegistrationPeriodController(@PathVariable Long id) {
                registrationPeriodService.deleteRegistrationPeriodService(id);
                return ResponseEntity.ok(
                                ApiResponse.success(
                                                HttpStatus.OK.value(),
                                                EnumRegistrationPeriodSuccess.DELETED.getMessage(),
                                                null));
        }

}
