package modules.expertisetags.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateExpertiseTagRequest {

    @NotBlank(message = "Tên chuyên môn không được để trống")
    @Size(max = 255, message = "Tên chuyên môn không được vượt quá 255 ký tự")
    private String name;
}
