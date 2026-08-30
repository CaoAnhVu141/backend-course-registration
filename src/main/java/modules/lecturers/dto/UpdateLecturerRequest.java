package modules.lecturers.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import java.util.Set;

@Getter
@Setter
public class UpdateLecturerRequest {

    @NotBlank(message = "Mã giảng viên không được để trống")
    @Size(max = 50, message = "Mã giảng viên không được vượt quá 50 ký tự")
    private String lecturerCode;

    @NotNull(message = "Khoa/phòng ban không được để trống")
    @Positive(message = "Khoa/phòng ban không hợp lệ")
    private Long departmentId;

    @Size(max = 100, message = "Học hàm/học vị không được vượt quá 100 ký tự")
    private String academicTitle;

    @NotNull(message = "Số lượng phản biện tối đa không được để trống")
    @PositiveOrZero(message = "Số lượng phản biện tối đa không hợp lệ")
    private Integer maxReviewLoad;

    private Set<Long> expertiseTagIds;
}