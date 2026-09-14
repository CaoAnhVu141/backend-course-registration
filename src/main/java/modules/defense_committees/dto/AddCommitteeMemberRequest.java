package modules.defense_committees.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;
import modules.defense_committees.enums.CommitteeRole;

@Getter
@Setter
public class AddCommitteeMemberRequest {

    @NotNull(message = "Giảng viên không được để trống")
    @Positive(message = "Giảng viên không hợp lệ")
    private Long lecturerId;

    @NotNull(message = "Vai trò không được để trống")
    private CommitteeRole committeeRole;
}
