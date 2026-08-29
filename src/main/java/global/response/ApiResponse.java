package global.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ApiResponse {
    private LocalDateTime timestamp;
    private int status;
    private String message;
    private T data;

    public static <T> ApiResponse<T> success(int status, String message, T data) {
        return ApiResponse.<T>builder()
                .timestamp(LocalDateTime.now())
                .status(status)
                .message(message)
                .data(data)
                .build();
    }
}
