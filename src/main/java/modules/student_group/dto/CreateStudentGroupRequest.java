package modules.student_group.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import java.util.Set;

@Getter
@Setter
public class CreateStudentGroupRequest {
    @NotNull
    @Positive
    private Long registrationPeriodId;

    @NotBlank
    @Size(max = 255)
    private String name;

    @NotEmpty(message = "Danh sách sinh viên không được để trống")
    private Set<Long> memberUserIds;
}
