package modules.departments.service;

import lombok.RequiredArgsConstructor;
import modules.departments.dto.CreateDepartmentRequest;
import modules.departments.dto.DepartmentResponse;
import modules.departments.dto.UpdateDepartmentRequest;
import modules.departments.entity.Department;
import modules.departments.mapper.DepartmentMapper;
import modules.departments.repository.DepartmentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import modules.departments.exception.DepartmentException;
import modules.departments.enums.EnumDepartmentError;

@Service
@RequiredArgsConstructor
public class DepartmentService {
    private final DepartmentRepository departmentRepository;
    private final DepartmentMapper departmentMapper;

    @Transactional
    public DepartmentResponse createDepartmentService(CreateDepartmentRequest request) {
        if (departmentRepository.existsByName(request.getName())) {
            throw new DepartmentException(EnumDepartmentError.DEPARTMENT_NAME_EXISTS);
        }

        Department department = departmentRepository.save(
            Department.builder()
                .name(request.getName())
                .build()
        )
        return departmentMapper.toResponse(department);
    }

    @Transactional(readOnly = true)
    public List<DepartmentResponse> getAllDepartmentsService() {
        return departmentRepository.findAll()
                .stream()
                .map(departmentMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public DepartmentResponse getDepartmentByIdService(long departmentId) {
        Department department = departmentRepository.findDepartmentById(departmentId);
        if (department == null) {
            throw new DepartmentException(EnumDepartmentError.DEPARTMENT_NOT_FOUND);
        }
        return departmentMapper.toResponse(department);
    }

    @Transactional
    public DepartmentResponse updateDepartment(long departmentId, UpdateDepartmentRequest request) {
        Department department = findDepartmentById(departmentId);
        if (department == null) {
            throw new DepartmentException(EnumDepartmentError.DEPARTMENT_NOT_FOUND);
        }
        department.setName(request.getName());
        departmentRepository.save(department);
        return departmentMapper.toResponse(department);
    }

    @Transactional
    public void deleteDepartmentService(long departmentId) {
        Department department = findDepartmentById(departmentId);
        if (department == null) {
            throw new DepartmentException(EnumDepartmentError.DEPARTMENT_NOT_FOUND);
        }
        departmentRepository.delete(department);
    }

    //
    private Department findDepartmentById(Long id) {
        return departmentRepository.findById(id)
                .orElseThrow(() -> new DepartmentException(EnumDepartmentError.DEPARTMENT_NOT_FOUND));
    }
}
