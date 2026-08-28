package modules.departments.mapper;

import modules.departments.dto.DepartmentResponse;
import modules.departments.entity.Department;
import org.springframework.stereotype.Component;

public class DepartmentMapper {

    public DepartmentResponse toResponse(Department department) {
        return DepartmentResponse.builder()
                .departmentId(department.getId())
                .name(department.getName())
                .build();
    }

}
