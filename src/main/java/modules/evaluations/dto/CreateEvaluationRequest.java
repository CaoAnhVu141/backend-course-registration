package modules.evaluations.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateEvaluationRequest {
    @NotNull(message = "Phân công không được để trống")
    @Positive(message = "Phân công không hợp lệ")
    private Long assignmentId;

    @NotNull(message = "Điểm không được để trống")
    @DecimalMin(value = "0.0", message = "Điểm tối thiểu là 0")
    @DecimalMax(value = "10.0", message = "Điểm tối đa là 10")
    private Double score;

    @Size (max = 1000, message = "Nhận xét không được vượt quá 1000 ký tự")
    private String comment;
}
