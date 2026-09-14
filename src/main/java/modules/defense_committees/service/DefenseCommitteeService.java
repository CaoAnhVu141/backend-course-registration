package modules.defense_committees.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import modules.auth.entity.User;
import modules.auth.repository.UserRepository;
import modules.defense_committees.dto.AddCommitteeMemberRequest;
import modules.defense_committees.dto.CreateDefenseCommitteeRequest;
import modules.defense_committees.dto.DefenseCommitteeResponse;
import modules.defense_committees.entity.DefenseCommittee;
import modules.defense_committees.enums.DefenseCommitteeStatus;
import modules.defense_committees.enums.EnumDefenseCommitteeError;
import modules.defense_committees.exception.DefenseCommitteeException;
import modules.defense_committees.mapper.DefenseCommitteeMapper;
import modules.defense_committees.repository.CommitteeMemberRepository;
import modules.defense_committees.repository.DefenseCommitteeRepository;
import modules.lecturers.entity.Lecturer;
import modules.lecturers.repository.LecturerRepository;
import modules.registration_period.repository.RegistrationPeriodRepository;
import modules.defense_committees.entity.CommitteeMember;
import java.time.LocalDateTime;

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

    // thêm thành viên hội đồng
    public DefenseCommitteeResponse createMemberCommitteeService(Long defenseCommitteeId,
            AddCommitteeMemberRequest addCommitteeMemberRequest) {
        DefenseCommittee defenseCommittee = findDefenseCommitteeById(defenseCommitteeId);

        if (defenseCommittee.getStatus() == DefenseCommitteeStatus.CLOSED) {
            throw new DefenseCommitteeException(EnumDefenseCommitteeError.DEFENSE_COMMITTEE_CLOSED);
        }

        Lecturer lecturer = lecturerRepository.findById(addCommitteeMemberRequest.getLecturerId())
                .orElseThrow(() -> new DefenseCommitteeException(EnumDefenseCommitteeError.LECTURER_NOT_FOUND));
        // kiểm tra giảng viên thuộc hội đồng nào chưa
        if (committeeMemberRepository.existsByLecturerUserIdAndDefenseCommitteeId(lecturer.getUserId(),
                defenseCommittee.getId())) {
            throw new DefenseCommitteeException(EnumDefenseCommitteeError.COMMITTEE_MEMBER_ALREADY_EXISTS);
        }

        committeeMemberRepository.save(
                CommitteeMember.builder()
                        .defenseCommittee(defenseCommittee)
                        .lecturer(lecturer)
                        .committeeRole(addCommitteeMemberRequest.getCommitteeRole())
                        .joinedAt(LocalDateTime.now())
                        .build());
        return defenseCommitteeMapper.toResponse(defenseCommittee,
                committeeMemberRepository.findAllByDefenseCommitteeId(defenseCommittee.getId()));
    }

    @Transactional(readOnly = true)
    public List<DefenseCommitteeResponse> getAllDefenseCommitteesService() {
        return defenseCommitteeRepository.findAll().stream()
                .map(defenseCommittee -> defenseCommitteeMapper.toResponse(defenseCommittee,
                        committeeMemberRepository.findAllByDefenseCommitteeId(defenseCommittee.getId())))
                .toList();
    }

    private DefenseCommittee findDefenseCommitteeById(Long id) {
        return defenseCommitteeRepository.findById(id)
                .orElseThrow(() -> new DefenseCommitteeException(
                        EnumDefenseCommitteeError.DEFENSE_COMMITTEE_NOT_FOUND));
    }
}
