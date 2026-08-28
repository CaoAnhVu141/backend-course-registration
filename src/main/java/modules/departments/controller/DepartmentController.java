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

import java.util.List;

@RestController
@RequestMapping("/api/departments")
@RequiredArgsConstructor
public class DepartmentController {
    private final DepartmentService departmentService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<DepartmentResponse> createDepartmentController(
            @Valid @RequestBody CreateDepartmentRequest request) {
        DepartmentResponse response = departmentService.createDepartmentService(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<DepartmentResponse>> getAllDepartmentController() {
        return ResponseEntity.ok(departmentService.getAllDepartmentsService());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DepartmentResponse> getDepartmentByIdController(@PathVariable Long id) {
        DepartmentResponse response = departmentService.getDepartmentByIdService(id);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<DepartmentResponse> updateDepartmentController(@PathVariable Long id,
            @Valid @RequestBody UpdateDepartmentRequest request) {
        DepartmentResponse response = departmentService.updateDepartment(id, request);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDepartmentController(@PathVariable Long id) {
        departmentService.deleteDepartmentService(id);
        return ResponseEntity.noContent().build();
    }

}
