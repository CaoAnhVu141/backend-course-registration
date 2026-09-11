package modules.defense_committees.mapper;

import java.util.List;
import org.springframework.stereotype.Component;
import modules.defense_committees.dto.CommitteeMemberResponse;
import modules.defense_committees.dto.DefenseCommitteeResponse;
import modules.defense_committees.entity.CommitteeMember;
import modules.defense_committees.entity.DefenseCommittee;

@Component
public class DefenseCommitteeMapper {

    public DefenseCommitteeResponse toResponse(DefenseCommittee defenseCommittee,
            List<CommitteeMember> committeeMembers) {
        return DefenseCommitteeResponse.builder()
                .id(defenseCommittee.getId())
                .registrationPeriodId(defenseCommittee.getRegistrationPeriodId())
                .code(defenseCommittee.getCode())
                .name(defenseCommittee.getName())
                .status(defenseCommittee.getStatus())
                .createdBy(defenseCommittee.getCreatedBy())
                .members(members.stream().map(this::toMemberResponse).toList())
                .build();
    }

    public CommitteeMemberResponse toMemberResponse(CommitteeMember member) {
        return CommitteeMemberResponse.builder()
                .id(member.getId())
                .lecturerUserId(member.getLecturer().getUserId())
                .lecturerCode(member.getLecturer().getLecturerCode())
                .lecturerEmail(member.getLecturer().getUser().getEmail())
                .committeeRole(member.getCommitteeRole())
                .joinedAt(member.getJoinedAt())
                .build();

    }
}
