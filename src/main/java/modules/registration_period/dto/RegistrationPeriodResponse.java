package modules.registration_period.dto;

import lombok.Builder;
import lombok.Getter;
import modules.registration_period.enums.RegistrationPeriodType;

import java.time.LocalDateTime;

@Getter
@Builder
public class RegistrationPeriodResponse {

    private Long id;
    private String name;
    private RegistrationPeriodType type;
    private LocalDateTime lecturerRegistrationStart;
    private LocalDateTime lecturerRegistrationEnd;
    private LocalDateTime studentRegistrationStart;
    private LocalDateTime studentRegistrationEnd;
    private LocalDateTime reviewerScoreDeadline;
    private LocalDateTime defenseDate;
    private boolean status;
}
