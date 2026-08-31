package modules.student_group.dto;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;
import modules.student_group.enums.GroupMemberRole;

@Getter
@Builder
public class GroupMemberResponse {

    private Long userId;
    private String username;
    private String studentCode;
    private GroupMemberRole role;
    private LocalDateTime joinedAt;
}
