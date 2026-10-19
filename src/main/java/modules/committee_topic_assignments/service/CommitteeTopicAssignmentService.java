package modules.committee_topic_assignments.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import modules.auth.entity.User;
import modules.auth.repository.UserRepository;
import modules.committee_topic_assignments.dto.CommitteeTopicAssignmentResponse;
import modules.committee_topic_assignments.dto.CreateCommitteeTopicAssignmentRequest;
import modules.committee_topic_assignments.dto.UpdateDefenseScheduleRequest;
import modules.committee_topic_assignments.entity.CommitteeTopicAssignment;
import modules.committee_topic_assignments.enums.CommitteeTopicAssignmentStatus;
import modules.committee_topic_assignments.enums.EnumCommitteeTopicAssignmentError;
import modules.committee_topic_assignments.exception.CommitteeTopicAssignmentException;
import modules.committee_topic_assignments.mapper.CommitteeTopicAssignmentMapper;
import modules.committee_topic_assignments.repository.CommitteeTopicAssignmentRepository;
import modules.defense_committees.entity.CommitteeMember;
import modules.defense_committees.entity.DefenseCommittee;
import modules.defense_committees.enums.CommitteeRole;
import modules.defense_committees.enums.DefenseCommitteeStatus;
import modules.defense_committees.repository.CommitteeMemberRepository;
import modules.defense_committees.repository.DefenseCommitteeRepository;
import modules.lecturers.entity.Lecturer;
import modules.lecturers.repository.LecturerRepository;
import modules.registration_period.entity.RegistrationPeriod;
import modules.registration_period.repository.RegistrationPeriodRepository;
import modules.student_group.entity.GroupMember;
import modules.student_group.repository.GroupMemberRepository;
import modules.students.entity.Student;
import modules.students.repository.StudentRepository;
import modules.topic_registrations.entity.TopicRegistration;
import modules.topic_registrations.enums.TopicRegistrationStatus;
import modules.topic_registrations.repository.TopicRegistrationRepository;
import modules.topics.entity.TopicSupervisor;
import modules.topics.repository.TopicSupervisorRepository;
import modules.notifications.enums.NotificationType;
import modules.notifications.service.NotificationService;

@Service
@RequiredArgsConstructor
public class CommitteeTopicAssignmentService {
        private final CommitteeTopicAssignmentRepository committeeTopicAssignmentRepository;
        private final DefenseCommitteeRepository defenseCommitteeRepository;
        private final CommitteeMemberRepository committeeMemberRepository;
        private final TopicRegistrationRepository topicRegistrationRepository;
        private final RegistrationPeriodRepository registrationPeriodRepository;
        private final TopicSupervisorRepository topicSupervisorRepository;
        private final LecturerRepository lecturerRepository;
        private final StudentRepository studentRepository;
        private final GroupMemberRepository groupMemberRepository;
        private final UserRepository userRepository;
        private final CommitteeTopicAssignmentMapper committeeTopicAssignmentMapper;
        private final NotificationService notificationService;

        // phân công đề tài cho hội đồng
        @Transactional
        public CommitteeTopicAssignmentResponse createCommitteeTopicAssignmentService(
                        CreateCommitteeTopicAssignmentRequest createCommitteeTopicAssignmentRequest,
                        String username) {
                User user = userRepository.findByUsername(username).orElseThrow(
                                () -> new CommitteeTopicAssignmentException(
                                                EnumCommitteeTopicAssignmentError.USER_NOT_FOUND));
                DefenseCommittee defenseCommittee = findDefenseCommitteeById(
                                createCommitteeTopicAssignmentRequest.getDefenseCommitteeId());
                if (defenseCommittee.getStatus() == DefenseCommitteeStatus.CLOSED) {
                        throw new CommitteeTopicAssignmentException(
                                        EnumCommitteeTopicAssignmentError.DEFENSE_COMMITTEE_CLOSED);
                }

                // lấy danh sách thành viên
                List<CommitteeMember> committeeMembers = committeeMemberRepository
                                .findAllByDefenseCommitteeId(defenseCommittee.getId());
                boolean hasChairman = committeeMembers.stream()
                                .anyMatch(committeeMember -> committeeMember
                                                .getCommitteeRole() == CommitteeRole.CHAIRMAN);
                if (!hasChairman) {
                        throw new CommitteeTopicAssignmentException(
                                        EnumCommitteeTopicAssignmentError.DEFENSE_COMMITTEE_NO_CHAIRMAN);
                }

                TopicRegistration topicRegistration = topicRegistrationRepository
                                .findById(createCommitteeTopicAssignmentRequest.getTopicRegistrationId()).orElseThrow(
                                                () -> new CommitteeTopicAssignmentException(
                                                                EnumCommitteeTopicAssignmentError.TOPIC_REGISTRATION_NOT_FOUND));
                // dề tài được duyệt mới được đưa ra hội đồng
                if (topicRegistration.getStatus() != TopicRegistrationStatus.APPROVED) {
                        throw new CommitteeTopicAssignmentException(
                                        EnumCommitteeTopicAssignmentError.TOPIC_REGISTRATION_NOT_APPROVED);
                }

                // một đề tài chỉ được chấm 1 hội đồng
                if (committeeTopicAssignmentRepository.existsByTopicRegistrationId(topicRegistration.getId())) {
                        throw new CommitteeTopicAssignmentException(
                                        EnumCommitteeTopicAssignmentError.ASSIGNMENT_ALREADY_EXISTS);
                }

                RegistrationPeriod registrationPeriod = findRegistrationPeriodById(
                                defenseCommittee.getRegistrationPeriodId());
                // hội đồng và dề tài cũng 1 kì đăng kí
                if (!registrationPeriod.getId().equals(topicRegistration.getTopic().getRegistrationPeriodId())) {
                        throw new CommitteeTopicAssignmentException(
                                        EnumCommitteeTopicAssignmentError.REGISTRATION_PERIOD_MISMATCH);
                }

                validateSupervisorConflict(topicRegistration.getTopic().getId(), committeeMembers);
                validateReviewLoad(committeeMembers);
                if (createCommitteeTopicAssignmentRequest.getScheduledStart() != null
                                || createCommitteeTopicAssignmentRequest.getScheduledEnd() != null) {

                        // giờ kết thúc phải sau giờ bắt đầu, và không được sớm hơn defense_date của kỳ
                        validateDefenseTime(
                                        createCommitteeTopicAssignmentRequest.getScheduledStart(),
                                        createCommitteeTopicAssignmentRequest.getScheduledEnd(),
                                        registrationPeriod);
                }

                CommitteeTopicAssignment committeeTopicAssignment = committeeTopicAssignmentRepository.save(
                                CommitteeTopicAssignment.builder()
                                                .defenseCommittee(defenseCommittee)
                                                .topicRegistration(topicRegistration)
                                                .scheduledStart(createCommitteeTopicAssignmentRequest
                                                                .getScheduledStart())
                                                .scheduledEnd(createCommitteeTopicAssignmentRequest
                                                                .getScheduledEnd())
                                                .room(createCommitteeTopicAssignmentRequest.getRoom())
                                                .status(CommitteeTopicAssignmentStatus.ASSIGNED)
                                                .assignedBy(user.getEmail())
                                                .assignedAt(LocalDateTime.now())
                                                .build());
                notificationService.notifyUsers(groupMemberUserIds(topicRegistration),
                                NotificationType.DEFENSE_SCHEDULED, "Đề tài đã được đưa ra hội đồng",
                                buildScheduleMessage(committeeTopicAssignment));
                return committeeTopicAssignmentMapper.toResponse(committeeTopicAssignment);

        }

        // thực hiện cập nhật lịch bảo vệ và phòng học
        @Transactional
        public CommitteeTopicAssignmentResponse updateDefenseScheduleService(Long id,
                        UpdateDefenseScheduleRequest updateDefenseScheduleRequest) {
                CommitteeTopicAssignment committeeTopicAssignment = findCommitteeTopicAssignmentById(id);
                if (committeeTopicAssignment.getStatus() != CommitteeTopicAssignmentStatus.ASSIGNED) {
                        throw new CommitteeTopicAssignmentException(
                                        EnumCommitteeTopicAssignmentError.INVALID_ASSIGNMENT_STATUS);
                }

                RegistrationPeriod registrationPeriod = findRegistrationPeriodById(
                                committeeTopicAssignment.getDefenseCommittee().getRegistrationPeriodId());

                validateDefenseTime(updateDefenseScheduleRequest.getScheduledStart(),
                                updateDefenseScheduleRequest.getScheduledEnd(), registrationPeriod);
                // check trùng phòng
                validateRoomAvailable(updateDefenseScheduleRequest.getRoom(),
                                updateDefenseScheduleRequest.getScheduledStart(),
                                updateDefenseScheduleRequest.getScheduledEnd(), committeeTopicAssignment.getId());

                committeeTopicAssignment.setScheduledStart(updateDefenseScheduleRequest.getScheduledStart());
                committeeTopicAssignment.setScheduledEnd(updateDefenseScheduleRequest.getScheduledEnd());
                committeeTopicAssignment.setRoom(updateDefenseScheduleRequest.getRoom());

                notificationService.notifyUsers(
                                groupMemberUserIds(committeeTopicAssignment.getTopicRegistration()),
                                NotificationType.DEFENSE_SCHEDULED, "Cập nhật lịch bảo vệ",
                                buildScheduleMessage(committeeTopicAssignment));
                return committeeTopicAssignmentMapper.toResponse(committeeTopicAssignment);
        }

        // xác nhận đẫ bảo vệ xong
        @Transactional
        public CommitteeTopicAssignmentResponse completeCommitteeTopicAssignmentService(Long id) {
                CommitteeTopicAssignment committeeTopicAssignment = findCommitteeTopicAssignmentById(id);
                if (committeeTopicAssignment.getStatus() != CommitteeTopicAssignmentStatus.ASSIGNED) {
                        throw new CommitteeTopicAssignmentException(
                                        EnumCommitteeTopicAssignmentError.INVALID_ASSIGNMENT_STATUS);
                }
                committeeTopicAssignment.setStatus(CommitteeTopicAssignmentStatus.COMPLETED);
                return committeeTopicAssignmentMapper.toResponse(committeeTopicAssignment);
        }

        // huỷ phân công
        @Transactional
        public CommitteeTopicAssignmentResponse cancelCommitteeTopicAssignmentService(Long id) {
                CommitteeTopicAssignment committeeTopicAssignment = findCommitteeTopicAssignmentById(id);
                if (committeeTopicAssignment.getStatus() != CommitteeTopicAssignmentStatus.ASSIGNED) {
                        throw new CommitteeTopicAssignmentException(
                                        EnumCommitteeTopicAssignmentError.INVALID_ASSIGNMENT_STATUS);
                }
                committeeTopicAssignment.setStatus(CommitteeTopicAssignmentStatus.CANCELED);
                return committeeTopicAssignmentMapper.toResponse(committeeTopicAssignment);
        }

        // lấy toàn bộ phân công
        @Transactional(readOnly = true)
        public List<CommitteeTopicAssignmentResponse> getAllCommitteeTopicAssignmentService() {
                return committeeTopicAssignmentRepository.findAll().stream()
                                .map(committeeTopicAssignmentMapper::toResponse).toList();
        }

        @Transactional(readOnly = true)
        public CommitteeTopicAssignmentResponse getCommitteeTopicAssignmentByIdService(Long id) {
                return committeeTopicAssignmentMapper.toResponse(findCommitteeTopicAssignmentById(id));
        }

        // danh sách đề tại mà hội đồng chấm
        @Transactional(readOnly = true)
        public List<CommitteeTopicAssignmentResponse> getAllByDefenseCommitteeService(Long defenseCommitteeId) {
                findDefenseCommitteeById(defenseCommitteeId);
                return committeeTopicAssignmentRepository.findAllByDefenseCommitteeId(defenseCommitteeId).stream()
                                .map(committeeTopicAssignmentMapper::toResponse).toList();
        }

        // giảng viên xem hội đồng mà mình được tham gia
        @Transactional(readOnly = true)
        public List<CommitteeTopicAssignmentResponse> getMyAssignmentsAsLecturerService(String username) {
                Lecturer lecturer = lecturerRepository.findByUserUsername(username)
                                .orElseThrow(() -> new CommitteeTopicAssignmentException(
                                                EnumCommitteeTopicAssignmentError.LECTURER_NOT_FOUND));
                List<Long> defenseCommitteeIds = committeeMemberRepository.findAllByLecturerUserId(
                                lecturer.getUserId()).stream().map(
                                                committeeMember -> committeeMember.getDefenseCommittee().getId())
                                .distinct().toList();
                if (defenseCommitteeIds.isEmpty()) {
                        return List.of();
                }

                // lấy tất cả phân công của các hội đồng mà giảng viên này tham gia
                return committeeTopicAssignmentRepository.findAllByDefenseCommitteeIdIn(defenseCommitteeIds).stream()
                                .map(committeeTopicAssignmentMapper::toResponse).toList();
        }

        // sinh vien xem lịch của nhóm mình
        @Transactional(readOnly = true)
        public List<CommitteeTopicAssignmentResponse> getMyGroupAssignmentsAsStudentService(String username) {
                Student student = studentRepository.findByUserUsername(username)
                                .orElseThrow(() -> new CommitteeTopicAssignmentException(
                                                EnumCommitteeTopicAssignmentError.STUDENT_NOT_FOUND));
                List<Long> studentGroupIds = groupMemberRepository.findAllByStudentUserId(student.getUserId())
                                .stream().map(
                                                groupMember -> groupMember.getStudentGroup().getId())
                                .distinct().toList();

                if (studentGroupIds.isEmpty()) {
                        return List.of();
                }

                return committeeTopicAssignmentRepository.findAllByTopicRegistrationStudentGroupIdIn(studentGroupIds)
                                .stream()
                                .map(committeeTopicAssignmentMapper::toResponse).toList();
        }

        // function helper
        private DefenseCommittee findDefenseCommitteeById(Long id) {
                return defenseCommitteeRepository.findById(id).orElseThrow(() -> new CommitteeTopicAssignmentException(
                                EnumCommitteeTopicAssignmentError.DEFENSE_COMMITTEE_NOT_FOUND));
        }

        private RegistrationPeriod findRegistrationPeriodById(Long id) {
                return registrationPeriodRepository.findById(id)
                                .orElseThrow(() -> new CommitteeTopicAssignmentException(
                                                EnumCommitteeTopicAssignmentError.REGISTRATION_PERIOD_NOT_FOUND));
        }

        private CommitteeTopicAssignment findCommitteeTopicAssignmentById(Long id) {
                return committeeTopicAssignmentRepository.findById(id)
                                .orElseThrow(() -> new CommitteeTopicAssignmentException(
                                                EnumCommitteeTopicAssignmentError.ASSIGNMENT_NOT_FOUND));
        }

        // // giảng viên hướng dẫn không được nằm trong hội đồng chấm đề tài của mình
        private void validateSupervisorConflict(Long topicId, List<CommitteeMember> committeeMembers) {
                Set<Long> supervisorUserIds = topicSupervisorRepository.findAllByTopicId(topicId).stream()
                                .map(TopicSupervisor::getLecturer)
                                .map(Lecturer::getUserId)
                                .collect(Collectors.toSet());
                if (supervisorUserIds.isEmpty()) {
                        return;
                }

                boolean conflicted = committeeMembers.stream().anyMatch(committeeMember -> supervisorUserIds.contains(
                                committeeMember.getLecturer().getUserId()));
                if (conflicted) {
                        throw new CommitteeTopicAssignmentException(
                                        EnumCommitteeTopicAssignmentError.SUPERVISOR_CONFLICT);
                }
        }

        // giới hạn số đề tài chấm của giảng viên phản biện trong hội đồng
        private void validateReviewLoad(List<CommitteeMember> committeeMembers) {
                for (CommitteeMember committeeMember : committeeMembers) {
                        if (committeeMember.getCommitteeRole() != CommitteeRole.REVIEWER) {
                                continue;
                        }

                        Lecturer lecturer = committeeMember.getLecturer();
                        if (lecturer.getMaxReviewLoad() == null) {
                                continue;
                        }

                        List<Long> defenseCommitteeIds = committeeMemberRepository
                                        .findAllByLecturerUserId(lecturer.getUserId()).stream()
                                        .map(member -> member.getDefenseCommittee().getId())
                                        .distinct()
                                        .toList();
                        if (defenseCommitteeIds.isEmpty()) {
                                continue;
                        }

                        long currentLoad = committeeTopicAssignmentRepository.countByDefenseCommitteeIdInAndStatus(
                                        defenseCommitteeIds, CommitteeTopicAssignmentStatus.ASSIGNED);
                        if (currentLoad + 1 > lecturer.getMaxReviewLoad()) {
                                throw new CommitteeTopicAssignmentException(
                                                EnumCommitteeTopicAssignmentError.REVIEW_LOAD_EXCEEDED);
                        }
                }
        }

        private void validateDefenseTime(LocalDateTime scheduledStart, LocalDateTime scheduledEnd,
                        RegistrationPeriod registrationPeriod) {

                // đã xếp lịch thì phải có đủ cả giờ bắt đầu và giờ kết thúc
                if (scheduledStart == null || scheduledEnd == null) {
                        throw new CommitteeTopicAssignmentException(
                                        EnumCommitteeTopicAssignmentError.DEFENSE_TIME_INVALID);
                }

                if (!scheduledEnd.isAfter(scheduledStart)) {
                        throw new CommitteeTopicAssignmentException(
                                        EnumCommitteeTopicAssignmentError.DEFENSE_TIME_INVALID);
                }

                // không được bảo vệ trước ngày bảo vệ của kỳ đăng ký
                if (registrationPeriod.getDefenseDate() != null
                                && scheduledStart.isBefore(registrationPeriod.getDefenseDate())) {
                        throw new CommitteeTopicAssignmentException(
                                        EnumCommitteeTopicAssignmentError.DEFENSE_TIME_INVALID);
                }
        }

        // excludedId null khi tạo mới, khác null khi cập nhật lịch của chính bản ghi đó
        private void validateRoomAvailable(String room, LocalDateTime scheduledStart, LocalDateTime scheduledEnd,
                        Long excludedId) {

                if (room == null || room.isBlank()) {
                        return;
                }
                boolean conflicted = excludedId == null
                                ? committeeTopicAssignmentRepository
                                                .existsByRoomAndStatusNotAndScheduledStartLessThanAndScheduledEndGreaterThan(
                                                                room, CommitteeTopicAssignmentStatus.CANCELED,
                                                                scheduledEnd, scheduledStart)
                                : committeeTopicAssignmentRepository
                                                .existsByRoomAndStatusNotAndIdNotAndScheduledStartLessThanAndScheduledEndGreaterThan(
                                                                room, CommitteeTopicAssignmentStatus.CANCELED,
                                                                excludedId, scheduledEnd,
                                                                scheduledStart);
                if (conflicted) {
                        throw new CommitteeTopicAssignmentException(
                                        EnumCommitteeTopicAssignmentError.DEFENSE_ROOM_TIME_CONFLICT);
                }
        }

        // lấy thành viên theo đề tài đăng kí
        private List<Long> groupMemberUserIds(TopicRegistration topicRegistration) {
                return groupMemberRepository.findAllByStudentGroupId(topicRegistration.getStudentGroup().getId())
                                .stream().map(gm -> gm.getStudent().getUserId()).toList();
        }

        // cấu hình lịch bảo vệ
        private String buildScheduleMessage(CommitteeTopicAssignment item) {
                StringBuilder stringBuilder = new StringBuilder();
                if (item.getScheduledStart() != null) {
                        stringBuilder.append("Thời gian: ").append(item.getScheduledStart());
                        if (item.getScheduledEnd() != null) {
                                stringBuilder.append(" - ").append(item.getScheduledEnd());
                        }
                        stringBuilder.append(". ");
                }
                if (item.getRoom() != null) {
                        stringBuilder.append("Phòng: ").append(item.getRoom()).append(".");
                }
                return stringBuilder.length() == 0 ? "Đề tài đã được phân công hội đồng." : stringBuilder.toString();
        }
}
