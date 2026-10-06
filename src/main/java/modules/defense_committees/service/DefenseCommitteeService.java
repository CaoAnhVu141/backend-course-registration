package modules.defense_committees.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import modules.auth.entity.User;
import modules.auth.repository.UserRepository;
import modules.defense_committees.dto.AddCommitteeMemberRequest;
import modules.defense_committees.dto.CommitteeMemberResponse;
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
import modules.defense_committees.enums.CommitteeRole;

@Service
@RequiredArgsConstructor
public class DefenseCommitteeService {

        private final DefenseCommitteeRepository defenseCommitteeRepository;
        private final CommitteeMemberRepository committeeMemberRepository;
        private final RegistrationPeriodRepository registrationPeriodRepository;
        private final LecturerRepository lecturerRepository;
        private final UserRepository userRepository;
        private final DefenseCommitteeMapper defenseCommitteeMapper;
        private static final int MIN_COMMITTEE_MEMBERS = 3;
        private static final int MAX_COMMITTEE_MEMBERS = 5;

        @Transactional
        public DefenseCommitteeResponse createDefenseCommitteeService(
                        CreateDefenseCommitteeRequest createDefenseCommitteeRequest,
                        String username) {
                User user = userRepository.findByUsername(username)
                                .orElseThrow(() -> new DefenseCommitteeException(
                                                EnumDefenseCommitteeError.USER_NOT_FOUND));

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
                                                .registrationPeriodId(
                                                                createDefenseCommitteeRequest.getRegistrationPeriodId())
                                                .code(createDefenseCommitteeRequest.getCode())
                                                .name(createDefenseCommitteeRequest.getName())
                                                .status(DefenseCommitteeStatus.ACTIVE)
                                                .createdBy(user.getEmail())
                                                .build());
                return defenseCommitteeMapper.toResponse(defenseCommittee, List.of());
        }

        // thêm thành viên hội đồng
        @Transactional
        public DefenseCommitteeResponse createMemberCommitteeService(Long defenseCommitteeId,
                        AddCommitteeMemberRequest addCommitteeMemberRequest) {
                DefenseCommittee defenseCommittee = findDefenseCommitteeById(defenseCommitteeId);

                if (defenseCommittee.getStatus() == DefenseCommitteeStatus.CLOSED) {
                        throw new DefenseCommitteeException(EnumDefenseCommitteeError.DEFENSE_COMMITTEE_CLOSED);
                }

                Lecturer lecturer = lecturerRepository.findById(addCommitteeMemberRequest.getLecturerId())
                                .orElseThrow(() -> new DefenseCommitteeException(
                                                EnumDefenseCommitteeError.LECTURER_NOT_FOUND));
                // // kiểm tra giảng viên thuộc hội đồng nào chưa
                if (committeeMemberRepository.existsByLecturerUserIdAndDefenseCommitteeId(lecturer.getUserId(),
                                defenseCommittee.getId())) {
                        throw new DefenseCommitteeException(EnumDefenseCommitteeError.COMMITTEE_MEMBER_ALREADY_EXISTS);
                }

                List<CommitteeMember> members = committeeMemberRepository
                                .findAllByDefenseCommitteeId(defenseCommittee.getId());
                if (members.size() >= MAX_COMMITTEE_MEMBERS) {
                        throw new DefenseCommitteeException(EnumDefenseCommitteeError.COMMITTEE_FULL);
                }

                // tối đa 1 chủ tịch và 1 thư kí
                CommitteeRole committeeRole = addCommitteeMemberRequest.getCommitteeRole();
                if (committeeRole == CommitteeRole.CHAIRMAN
                                && members.stream().anyMatch(m -> m.getCommitteeRole() == CommitteeRole.CHAIRMAN)) {
                        throw new DefenseCommitteeException(EnumDefenseCommitteeError.CHAIRMAN_ALREADY_EXISTS);
                }
                if (committeeRole == CommitteeRole.SECRETARY
                                && members.stream().anyMatch(m -> m.getCommitteeRole() == CommitteeRole.SECRETARY)) {
                        throw new DefenseCommitteeException(EnumDefenseCommitteeError.SECRETARY_ALREADY_EXISTS);
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
                                                committeeMemberRepository
                                                                .findAllByDefenseCommitteeId(defenseCommittee.getId())))
                                .toList();
        }

        // Lấy theo kì đăng kí
        @Transactional(readOnly = true)
        public List<DefenseCommitteeResponse> getAllByRegistrationPeriodService(Long registrationPeriodId) {
                return defenseCommitteeRepository.findAllByRegistrationPeriodId(registrationPeriodId).stream().map(
                                defenseCommittee -> defenseCommitteeMapper
                                                .toResponse(defenseCommittee,
                                                                committeeMemberRepository.findAllByDefenseCommitteeId(
                                                                                defenseCommittee.getId())))
                                .toList();
        }

        // xem chi tiết
        @Transactional(readOnly = true)
        public DefenseCommitteeResponse getDefenseCommitteByIdService(Long id) {
                DefenseCommittee defenseCommittee = findDefenseCommitteeById(id);
                return defenseCommitteeMapper.toResponse(defenseCommittee,
                                committeeMemberRepository.findAllByDefenseCommitteeId(defenseCommittee.getId()));
        }

        // giảng viên xem mình thuộc hội đồng nào
        @Transactional(readOnly = true)
        public List<CommitteeMemberResponse> getMyCommitteeMembersService(String username) {
                Lecturer lecturer = lecturerRepository.findByUserUsername(username)
                                .orElseThrow(() -> new DefenseCommitteeException(
                                                EnumDefenseCommitteeError.LECTURER_NOT_FOUND));
                return committeeMemberRepository.findAllByLecturerUserId(lecturer.getUserId())
                                .stream().map(defenseCommitteeMapper::toMemberResponse).toList();
        }

        // đóng hội đồng
        @Transactional
        public DefenseCommitteeResponse closeDefenseCommitteeService(Long id) {
                DefenseCommittee defenseCommittee = findDefenseCommitteeById(id);
                List<CommitteeMember> members = committeeMemberRepository.findAllByDefenseCommitteeId(id);
                if (members.size() < MIN_COMMITTEE_MEMBERS || members.size() > MAX_COMMITTEE_MEMBERS) {
                        throw new DefenseCommitteeException(EnumDefenseCommitteeError.INVALID_COMMITTEE_SIZE);
                }
                // đúng 1 chủ tịch, 1 thư ký
                Long chairmen = members.stream().filter(m -> m.getCommitteeRole() == CommitteeRole.CHAIRMAN).count();
                if (chairmen != 1) {
                        throw new DefenseCommitteeException(EnumDefenseCommitteeError.COMMITTEE_NO_CHAIRMAN);
                }
                Long secretaries = members.stream().filter(m -> m.getCommitteeRole() == CommitteeRole.SECRETARY)
                                .count();
                if (secretaries != 1) {
                        throw new DefenseCommitteeException(EnumDefenseCommitteeError.COMMITTEE_NO_SECRETARY);
                }
                defenseCommittee.setStatus(DefenseCommitteeStatus.CLOSED);
                return defenseCommitteeMapper.toResponse(
                                defenseCommittee,
                                committeeMemberRepository.findAllByDefenseCommitteeId(defenseCommittee.getId()));
        }

        // function helper
        private DefenseCommittee findDefenseCommitteeById(Long id) {
                return defenseCommitteeRepository.findById(id)
                                .orElseThrow(() -> new DefenseCommitteeException(
                                                EnumDefenseCommitteeError.DEFENSE_COMMITTEE_NOT_FOUND));
        }
}
