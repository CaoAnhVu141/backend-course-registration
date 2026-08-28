package modules.departments.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateDepartmentRequest {

    @NotBlank(message = "Tên phòng ban không được để trống")
    private String name;
}
