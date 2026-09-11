package modules.defense_committees.dto;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import modules.defense_committees.enums.CommitteeRole;

@Getter
@Builder
public class CommitteeMemberResponse {

    private Long id;
    private Long lecturerId;
    private String lecturerCode;
    private String lecturerEmail;
    private CommitteeRole committeeRole;
    private LocalDateTime joinedAt;
}
