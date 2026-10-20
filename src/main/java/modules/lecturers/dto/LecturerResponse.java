package modules.lecturers.dto;

import lombok.*;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LecturerResponse {
    private Long userId;
    private String username;
    private String email;
    private String name;
    private boolean status;
    private Set<String> roles;
    private String lecturerCode;
    private Long departmentId;
    private String academicTitle;
    private Integer maxReviewLoad;
    private Set<Long> expertiseTagIds;
}
