package modules.defense_committees.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateDefenseCommitteeRequest {

    @NotNull(message = "Kì đăng kí không được để trống")
    @Positive(message = "Kì đăng kí không hợp lệ")
    private Long registrationPeriodId;

    @NotNull(message = "Mã hội đồng không được để trống")
    @Size(max = 50, message = "Mã hội đồng không được vượt quá 50 ký tự")
    private String code;

    @NotNull(message = "Tên hội đồng không được để trống")
    @Size(max = 100, message = "Tên hội đồng không được vượt quá 100 ký tự")
    private String name;
}
