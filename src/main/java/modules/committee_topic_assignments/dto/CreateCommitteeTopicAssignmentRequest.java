package modules.committee_topic_assignments.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateCommitteeTopicAssignmentRequest {
    @NotNull(message = "Hội đồng không được để trống")
    @Positive(message = "Hội đồng không hợp lệ")
    private Long defenseCommitteeId;

    @NotNull(message = "Đăng kí đề tài không được để trống")
    @Positive(message = "Đăng kí đề tài không hợp lệ")
    private Long topicRegistrationId;

    private LocalDateTime scheduledStart;
    private LocalDateTime scheduledEnd;

    @Size(max = 100, message = "Phòng bảo vệ không được vượt quá 100 ký tự")
    private String room;
}
