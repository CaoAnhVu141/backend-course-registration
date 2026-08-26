package modules.students.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor
public class RegisterStudentRequest {
    @NotBlank @Size(max = 255)
    private String username;

    @NotBlank @Email @Size(max = 255)
    private String email;

    @NotBlank @Size(min = 6, max = 72)
    private String password;

    @NotBlank @Size(max = 50)
    private String studentCode;

    @NotNull @Positive
    private Long departmentId;

    @NotBlank @Size(max = 100)
    private String className;
}
