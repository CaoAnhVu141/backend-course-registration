package modules.defense_committees.dto;

import lombok.Builder;
import lombok.Getter;
import modules.defense_committees.enums.DefenseCommitteeStatus;

@Getter
@Builder
public class DefenseCommitteeResponse {

    pivate Long id;
    private Long registrationPeriodId;
    private String code;
    private String name;
    private DefenseCommitteeStatus status;
    private String createdBy;
    private List<CommitteeMemberResponse> committeeMembers;
}
