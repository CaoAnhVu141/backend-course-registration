package modules.students.dto;

import lombok.*;
import java.util.Set;

@Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor
public class StudentResponse {
    private Long userId;
    private String username;
    private String email;
    private String name;
    private Set<String> roles;
    private String studentCode;
    private Long departmentId;
    private String className;
}
