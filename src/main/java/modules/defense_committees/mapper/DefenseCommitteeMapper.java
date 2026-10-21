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
                .committeeMembers(committeeMembers.stream().map(this::toMemberResponse).toList())
                .build();
    }

    public CommitteeMemberResponse toMemberResponse(CommitteeMember committeeMember) {
        return CommitteeMemberResponse.builder()
                .id(committeeMember.getId())
                .lecturerId(committeeMember.getLecturer().getUserId())
                .lecturerName(committeeMember.getLecturer().getUser().getName())
                .lecturerCode(committeeMember.getLecturer().getLecturerCode())
                .lecturerEmail(committeeMember.getLecturer().getUser().getEmail())
                .committeeRole(committeeMember.getCommitteeRole())
                .joinedAt(committeeMember.getJoinedAt())
                .build();
    }
}
