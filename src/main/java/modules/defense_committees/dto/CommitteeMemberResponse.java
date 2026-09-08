package modules.defense_committees.dto;

import java.time.LocalDateTime;
import modules.defense_committees.enums.CommitteeRole;

public class CommitteeMemberResponse {

    private Long id;
    private Long lecturerId;
    private String lecturerCode;
    private String lecturerEmail;
    private CommitteeRole committeeRole;
    private LocalDateTime joinedAt;
}
