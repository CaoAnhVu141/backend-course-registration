package modules.lecturers.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterLecturerRequest {
    @NotBlank(message = "Họ tên không được để trống")
    @Size(max = 255)
    private String name;
    @NotBlank
    @Size(max = 255)
    private String username;
    @NotBlank
    @Email
    @Size(max = 255)
    private String email;
    @NotBlank
    @Size(min = 6, max = 72)
    private String password;
    @NotBlank
    @Size(max = 50)
    private String lecturerCode;
    @NotNull
    @Positive
    private Long departmentId;
    @Size(max = 100)
    private String academicTitle;
    @NotNull
    @PositiveOrZero
    private Integer maxReviewLoad;

    @NotEmpty(message = "Chuyên môn giảng viên không được để trống")
    private Set<Long> expertiseTagIds;
}
