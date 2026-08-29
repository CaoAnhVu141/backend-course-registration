package modules.registration_period.mapper;

import modules.registration_period.dto.RegistrationPeriodResponse;
import modules.registration_period.entity.RegistrationPeriod;
import org.springframework.stereotype.Component;

@Component
public class RegistrationPeriodMapper {

    public RegistrationPeriodResponse toResponse(RegistrationPeriod registrationPeriod) {
        return RegistrationPeriodResponse.builder()
                .id(registrationPeriod.getId())
                .name(registrationPeriod.getName())
                .type(registrationPeriod.getType())
                .lecturerRegistrationStart(registrationPeriod.getLecturerRegistrationStart())
                .lecturerRegistrationEnd(registrationPeriod.getLecturerRegistrationEnd())
                .studentRegistrationStart(registrationPeriod.getStudentRegistrationStart())
                .studentRegistrationEnd(registrationPeriod.getStudentRegistrationEnd())
                .reviewerScoreDeadline(registrationPeriod.getReviewerScoreDeadline())
                .defenseDate(registrationPeriod.getDefenseDate())
                .status(registrationPeriod.isStatus())
                .build();
    }
}
