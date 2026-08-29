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
import modules.registration_period.validator.RegistrationPeriodValidator;
import java.time.LocalDateTime;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RegistrationPeriodService {

    private final RegistrationPeriodRepository registrationPeriodRepository;
    private final RegistrationPeriodMapper registrationPeriodMapper;
    private final RegistrationPeriodValidator registrationPeriodValidator;

    @Transactional
    public RegistrationPeriodResponse createRegistrationPeriodService(
            CreateRegistrationPeriodRequest registrationPeriodRequest) {
        registrationPeriodValidator.validateForCreate(registrationPeriodRequest);
        if (registrationPeriodRepository.existsByType(registrationPeriodRequest.getType())) {
            throw new RegistrationPeriodException(EnumRegistrationPeriodError.REGISTRATION_PERIOD_TYPE_EXISTS);
        }
        if (registrationPeriodRepository.existsByName(registrationPeriodRequest.getName())) {
            throw new RegistrationPeriodException(EnumRegistrationPeriodError.REGISTRATION_PERIOD_NAME_EXISTS);
        }
        RegistrationPeriod registrationPeriod = registrationPeriodRepository.save(
                RegistrationPeriod.builder()
                        .name(registrationPeriodRequest.getName())
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

    @Transactional(readOnly = true)
    public List<RegistrationPeriodResponse> getAllRegistrationPeriodService() {
        return registrationPeriodRepository.findAll()
                .stream()
                .map(registrationPeriodMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public RegistrationPeriodResponse getRegistrationPeriodByIdService(Long id) {
        RegistrationPeriod registrationPeriod = findRegistrationPeriodById(id);
        return registrationPeriodMapper.toResponse(registrationPeriod);
    }

    @Transactional
    public RegistrationPeriodResponse updateRegistrationPeriodService(
            Long id, UpdateRegistrationPeriodRequest updateRegistrationPeriodRequest) {
        registrationPeriodValidator.validateForUpdate(updateRegistrationPeriodRequest);
        RegistrationPeriod registrationPeriod = findRegistrationPeriodById(id);

        if (!registrationPeriod.getType().equals(updateRegistrationPeriodRequest.getType())
                && registrationPeriodRepository.existsByType(updateRegistrationPeriodRequest.getType())) {
            throw new RegistrationPeriodException(EnumRegistrationPeriodError.REGISTRATION_PERIOD_TYPE_EXISTS);
        }
        registrationPeriod.setType(updateRegistrationPeriodRequest.getType());
        registrationPeriod.setLecturerRegistrationStart(updateRegistrationPeriodRequest.getLecturerRegistrationStart());
        registrationPeriod.setLecturerRegistrationEnd(updateRegistrationPeriodRequest.getLecturerRegistrationEnd());
        registrationPeriod.setStudentRegistrationStart(updateRegistrationPeriodRequest.getStudentRegistrationStart());
        registrationPeriod.setStudentRegistrationEnd(updateRegistrationPeriodRequest.getStudentRegistrationEnd());
        registrationPeriod.setReviewerScoreDeadline(updateRegistrationPeriodRequest.getReviewerScoreDeadline());
        registrationPeriod.setDefenseDate(updateRegistrationPeriodRequest.getDefenseDate());
        registrationPeriod.setStatus(updateRegistrationPeriodRequest.isStatus());
        return registrationPeriodMapper.toResponse(registrationPeriod);
    }

    @Transactional
    public void deleteRegistrationPeriodService(Long id) {
        RegistrationPeriod registrationPeriod = findRegistrationPeriodById(id);
        registrationPeriodRepository.delete(registrationPeriod);
    }

    // function hepper
    private RegistrationPeriod findRegistrationPeriodById(Long id) {
        return registrationPeriodRepository.findById(id)
                .orElseThrow(() -> new RegistrationPeriodException(
                        EnumRegistrationPeriodError.REGISTRATION_PERIOD_NOT_FOUND));
    }
}
