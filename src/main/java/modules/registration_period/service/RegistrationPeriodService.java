package modules.registration_period.service;

import lombok.RequiredArgsConstructor;
import modules.registration_period.dto.CreateRegistrationPeriodRequest;
import modules.registration_period.dto.RegistrationPeriodResponse;
import modules.registration_period.dto.UpdateRegistrationPeriodRequest;
import modules.registration_period.entity.RegistrationPeriod;
import modules.registration_period.enums.EnumRegistrationPeriodError;
import modules.registration_period.exception.RegistrationPeriodException;
import modules.registration_period.mapper.RegistrationPeriodMapper;
import modules.registration_period.repository.RegistrationPeriodRepository;
import java.time.LocalDateTime;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RegistrationPeriodService {

    private final RegistrationPeriodRepository registrationPerriodRepository;
    private final RegistrationPeriodMapper registrationPeriodMapper;

    @Transactional
    public RegistrationPeriodResponse createRegistrationPeriodService(
            CreateRegistrationPeriodRequest registrationPeriodRequest) {
        if (registrationPerriodRepository.existsByType(registrationPeriodRequest.getType())) {
            throw new RegistrationPeriodException(EnumRegistrationPeriodError.REGISTRATION_PERIOD_NAME_EXISTS);
        }
        RegistrationPeriod registrationPeriod = registrationPerriodRepository.save(
                RegistrationPeriod.builder()
                        .type(registrationPeriodRequest.getType())
                        .lecturerRegistrationStart(registrationPeriodRequest.getLecturerRegistrationStart())
                        .lecturerRegistrationEnd(registrationPeriodRequest.getLecturerRegistrationEnd())
                        .studentRegistrationStart(registrationPeriodRequest.getStudentRegistrationStart())
                        .studentRegistrationEnd(registrationPeriodRequest.getStudentRegistrationEnd())
                        .reviewerScoreDeadline(registrationPeriodRequest.getReviewerScoreDeadline())
                        .defenseDate(registrationPeriodRequest.getDefenseDate())
                        .status(true)
                        .build());
        return registrationPeriodMapper.toResponse(registrationPeriod);
    }

}
