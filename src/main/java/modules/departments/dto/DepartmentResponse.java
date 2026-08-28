package modules.departments.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class DepartmentResponse {
    private Long departmentId;
    private String name;
}
