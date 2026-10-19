package modules.departments.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import modules.departments.dto.CreateDepartmentRequest;
import modules.departments.dto.DepartmentResponse;
import modules.departments.dto.UpdateDepartmentRequest;
import modules.departments.service.DepartmentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import global.response.ApiResponse;
import modules.departments.enums.EnumDepartmentSuccess;
import modules.students.export.ExportExcelService;

import java.util.List;

@RestController
@RequestMapping("/api/departments")
@RequiredArgsConstructor
public class DepartmentController {
    private final DepartmentService departmentService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<ApiResponse<DepartmentResponse>> createDepartmentController(
            @Valid @RequestBody CreateDepartmentRequest request) {
        DepartmentResponse response = departmentService.createDepartmentService(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponse.success(
                        HttpStatus.CREATED.value(),
                        EnumDepartmentSuccess.CREATED.getMessage(),
                        response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<DepartmentResponse>>> getAllDepartmentController() {
        List<DepartmentResponse> response = departmentService.getAllDepartmentsService();
        return ResponseEntity.ok(
                ApiResponse.success(
                        HttpStatus.OK.value(),
                        EnumDepartmentSuccess.GET_ALL.getMessage(),
                        response));
    }

    @GetMapping("/export")
    public ResponseEntity<byte[]> exportDepartmentsController() {
        return ExportExcelService.download(departmentService.exportDepartmentsToExcelService(), "phong-ban");
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<DepartmentResponse>> getDepartmentByIdController(@PathVariable Long id) {
        DepartmentResponse response = departmentService.getDepartmentByIdService(id);
        return ResponseEntity.ok(
                ApiResponse.success(
                        HttpStatus.OK.value(),
                        EnumDepartmentSuccess.GET_DETAIL.getMessage(),
                        response));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<DepartmentResponse>> updateDepartmentController(
            @PathVariable Long id,
            @Valid @RequestBody UpdateDepartmentRequest request) {
        DepartmentResponse response = departmentService.updateDepartment(id, request);
        return ResponseEntity.ok(
                ApiResponse.success(
                        HttpStatus.OK.value(),
                        EnumDepartmentSuccess.UPDATED.getMessage(),
                        response));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteDepartmentController(@PathVariable Long id) {
        departmentService.deleteDepartmentService(id);
        return ResponseEntity.ok(
                ApiResponse.success(
                        HttpStatus.OK.value(),
                        EnumDepartmentSuccess.DELETED.getMessage(),
                        null));
    }

}
