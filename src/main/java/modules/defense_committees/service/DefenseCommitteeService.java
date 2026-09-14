package modules.defense_committees.service;

import java.util.List;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import modules.auth.entity.User;
import modules.auth.repository.UserRepository;
import modules.defense_committees.dto.CreateDefenseCommitteeRequest;
import modules.defense_committees.dto.DefenseCommitteeResponse;
import modules.defense_committees.entity.DefenseCommittee;
import modules.defense_committees.enums.DefenseCommitteeStatus;
import modules.defense_committees.enums.EnumDefenseCommitteeError;
import modules.defense_committees.exception.DefenseCommitteeException;
import modules.defense_committees.mapper.DefenseCommitteeMapper;
import modules.defense_committees.repository.CommitteeMemberRepository;
import modules.defense_committees.repository.DefenseCommitteeRepository;
import modules.lecturers.repository.LecturerRepository;
import modules.registration_period.repository.RegistrationPeriodRepository;

@Service
@RequiredArgsConstructor
public class DefenseCommitteeService {

    private final DefenseCommitteeRepository defenseCommitteeRepository;
    private final CommitteeMemberRepository committeeMemberRepository;
    private final RegistrationPeriodRepository registrationPeriodRepository;
    private final LecturerRepository lecturerRepository;
    private final UserRepository userRepository;
    private final DefenseCommitteeMapper defenseCommitteeMapper;

    @Transactional
    public DefenseCommitteeResponse createDefenseCommitteeService(
            CreateDefenseCommitteeRequest createDefenseCommitteeRequest,
            String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new DefenseCommitteeException(EnumDefenseCommitteeError.USER_NOT_FOUND));

        // kiểm tra kì đăng kí tồn tại không
        if (!registrationPeriodRepository.existsById(createDefenseCommitteeRequest.getRegistrationPeriodId())) {
            throw new DefenseCommitteeException(EnumDefenseCommitteeError.REGISTRATION_PERIOD_NOT_FOUND);
        }

        // kiểm tra hội đồng
        if (defenseCommitteeRepository.existsByCode(createDefenseCommitteeRequest.getCode())) {
            throw new DefenseCommitteeException(EnumDefenseCommitteeError.DEFENSE_COMMITTEE_CODE_EXISTS);
        }

        DefenseCommittee defenseCommittee = defenseCommitteeRepository.save(
                DefenseCommittee.builder()
                        .registrationPeriodId(createDefenseCommitteeRequest.getRegistrationPeriodId())
                        .code(createDefenseCommitteeRequest.getCode())
                        .name(createDefenseCommitteeRequest.getName())
                        .status(DefenseCommitteeStatus.ACTIVE)
                        .createdBy(user.getEmail())
                        .build());
        return defenseCommitteeMapper.toResponse(defenseCommittee, List.of());
    }
}
