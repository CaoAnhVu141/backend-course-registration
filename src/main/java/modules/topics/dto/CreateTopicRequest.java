package modules.topics.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateTopicRequest {

    @NotBlank(message = "Tên đề tài không được để trống")
    @Size(max = 255, message = "Tên đề tài không được vượt quá 255 ký tự")
    private String title;

    private String requirements;

    @NotNull(message = "Khoa/phòng ban không được để trống")
    @Positive(message = "Khoa/phòng ban không hợp lệ")
    private Long departmentId;

    @NotNull(message = "Kỳ đăng ký không được để trống")
    @Positive(message = "Kỳ đăng ký không hợp lệ")
    private Long registrationPeriodId;

    // GVHD thứ 2, khác người tạo đề tài
    @Positive(message = "Giảng viên hướng dẫn thứ hai không hợp lệ")
    private Long coSupervisorId;

}