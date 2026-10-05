package modules.registration_period.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
import modules.registration_period.enums.RegistrationPeriodType;

@Getter
@Setter
public class CreateRegistrationPeriodRequest {

    @NotBlank(message = "Tên kỳ đăng ký không được để trống")
    @Size(max = 255, message = "Tên kỳ đăng ký không được vượt quá 255 ký tự")
    private String name;

    @NotNull(message = "Loại kỳ đăng ký không được để trống")
    private RegistrationPeriodType type;

    @NotNull(message = "Thời gian bắt đầu giảng viên đăng ký không được để trống")
    private LocalDateTime lecturerRegistrationStart;

    @NotNull(message = "Thời gian kết thúc giảng viên đăng ký không được để trống")
    private LocalDateTime lecturerRegistrationEnd;

    @NotNull(message = "Thời gian bắt đầu sinh viên đăng ký không được để trống")
    private LocalDateTime studentRegistrationStart;

    @NotNull(message = "Thời gian kết thúc sinh viên đăng ký không được để trống")
    private LocalDateTime studentRegistrationEnd;

    // Deadline chấm phản biện
    private LocalDateTime reviewerScoreDeadline;

    // Ngày bảo vệ
    private LocalDateTime defenseDate;

}
