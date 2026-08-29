package modules.registration_period.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateRegistrationPeriodRequest {

    @NotBlank(message = "Loại kỳ đăng ký không được để trống")
    @Size(max = 100, message = "Loại kỳ đăng ký không được vượt quá 100 ký tự")
    private String type;

    @NotNull(message = "Thời gian bắt đầu giảng viên đăng ký không được để trống")
    private LocalDateTime lecturerRegistrationStart;

    @NotNull(message = "Thời gian kết thúc giảng viên đăng ký không được để trống")
    private LocalDateTime lecturerRegistrationEnd;

    @NotNull(message = "Thời gian bắt đầu sinh viên đăng ký không được để trống")
    private LocalDateTime studentRegistrationStart;

    @NotNull(message = "Thời gian kết thúc sinh viên đăng ký không được để trống")
    private LocalDateTime studentRegistrationEnd;

    @NotNull(message = "Deadline chấm phản biện không được để trống")
    private LocalDateTime reviewerScoreDeadline;

    @NotNull(message = "Ngày bảo vệ không được để trống")
    private LocalDateTime defenseDate;
}
