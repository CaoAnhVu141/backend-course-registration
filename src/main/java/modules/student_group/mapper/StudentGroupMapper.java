package modules.student_group.mapper;

import java.util.List;

import org.springframework.stereotype.Component;

import modules.student_group.dto.GroupMemberResponse;

@Component
public class StudentGroupMapper {

    public StudentGroupResponse toResponse(StudentGroup studentGroup, List<GroupMember> members) {
        return StudentGroupResponse.builder()
                .id(studentGroup.getId())
                .registrationPeriodId(studentGroup.getRegistrationPeriodId())
                .name(studentGroup.getName())
                .status(studentGroup.isStatus())
                .createdBy(studentGroup.getCreateBy())
                .members(members.stream().map(this::toMemberResponse).toList())
                .build();
    }

    private GroupMemberResponse toMemberResponse(GroupMember member) {
        return GroupMemberResponse.builder()
                .userId(groupMember.getStudent().getUserId())
                .username(groupMember.getStudent().getUser().getUsername())
                .studentCode(groupMember.getStudent().getStudentCode())
                .role(groupMember.getRole())
                .joinedAt(groupMember.getJoinedAt())
                .build();
    }
}
