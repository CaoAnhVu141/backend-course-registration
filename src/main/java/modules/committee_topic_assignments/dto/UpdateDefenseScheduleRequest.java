package modules.committee_topic_assignments.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateDefenseScheduleRequest {
    @NotNull(message = "Thời gian bắt đầu không được để trống")
    private LocalDateTime scheduledStart;

    @NotNull(message = "Thời gian kết thúc không được để trống")
    private LocalDateTime scheduledEnd;

    @NotNull(message = "Phòng bảo vệ không được để trống")
    @Size(max = 100, message = "Phòng bảo vệ không được vượt quá 100 ký tự")
    private String room;
}
