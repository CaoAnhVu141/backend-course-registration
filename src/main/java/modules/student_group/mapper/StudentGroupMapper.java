package modules.student_group.mapper;

import java.util.List;

import org.springframework.stereotype.Component;

import modules.student_group.dto.GroupMemberResponse;
import modules.student_group.entity.GroupMember;
import modules.student_group.entity.StudentGroup;
import modules.student_group.dto.StudentGroupResponse;

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
                .userId(member.getStudent().getUserId())
                .username(member.getStudent().getUser().getUsername())
                .name(member.getStudent().getUser().getName())
                .studentCode(member.getStudent().getStudentCode())
                .role(member.getRole())
                .joinedAt(member.getJoinedAt())
                .build();
    }
}
