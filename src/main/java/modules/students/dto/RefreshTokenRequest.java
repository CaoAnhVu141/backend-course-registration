package modules.students.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

public class RefreshTokenRequest {

    @NotBlank(message = "Refresh token không được để trống")
    private String refreshToken;
}
