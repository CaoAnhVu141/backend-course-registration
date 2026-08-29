package modules.registration_period.mapper;

import modules.registrationperiods.dto.RegistrationPeriodResponse;
import modules.registrationperiods.entity.RegistrationPeriod;
import org.springframework.stereotype.Component;

@Component
public class RegistrationPeriodMapper {

    public RegistrationPeriodResponse toResponse(RegistrationPeriod registrationPeriod) {
        return RegistrationPeriodResponse.builder()
                .id(registrationPeriod.getId())
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
