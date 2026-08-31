package modules.student_group.dto;

import java.util.List;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class StudentGroupResponse {
    private Long id;
    private Long registrationPeriodId;
    private String name;
    private boolean status;
    private String createdBy;
    private List<GroupMemberResponse> members;
}
