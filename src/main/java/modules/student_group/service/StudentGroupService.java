package modules.student_group.service;

import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import modules.registration_period.repository.RegistrationPeriodRepository;
import modules.student_group.dto.CreateStudentGroupRequest;
import modules.student_group.entity.GroupMember;
import modules.student_group.entity.StudentGroup;
import modules.student_group.enums.EnumStudentGroupError;
import modules.student_group.enums.GroupMemberRole;
import modules.student_group.exception.StudentGroupException;
import modules.student_group.mapper.StudentGroupMapper;
import modules.student_group.repository.GroupMemberRepository;
import modules.student_group.repository.StudentGroupRepository;
import modules.students.entity.Student;
import modules.students.repository.StudentRepository;
import modules.topic_registrations.repository.TopicRegistrationRepository;
import modules.student_group.dto.StudentGroupResponse;
import java.util.HashSet;

import java.time.LocalDateTime;

@Service

@RequiredArgsConstructor
public class StudentGroupService {

        private final StudentGroupRepository studentGroupRepository;
        private final GroupMemberRepository groupMemberRepository;
        private final StudentGroupMapper studentGroupMapper;
        private final RegistrationPeriodRepository registrationPeriodRepository;
        private final StudentRepository studentRepository;
        private static final int maxGroupMembers = 5;
        private final TopicRegistrationRepository topicRegistrationRepository;

        @Transactional
        public StudentGroupResponse createStudentGroupService(CreateStudentGroupRequest createStudentGroupRequest,
                        String username) {
                if (!registrationPeriodRepository.existsById(createStudentGroupRequest.getRegistrationPeriodId())) {
                        throw new StudentGroupException(EnumStudentGroupError.REGISTRATION_PERIOD_NOT_FOUND);
                }
                Student leaderStudent = studentRepository.findByUserUsername(username)
                                .orElseThrow(() -> new StudentGroupException(EnumStudentGroupError.STUDENT_NOT_FOUND));
                Set<Long> memberUserIds = new HashSet<>(createStudentGroupRequest.getMemberUserIds());
                memberUserIds.add(leaderStudent.getUserId());
                if (memberUserIds.size() > maxGroupMembers) {
                        throw new StudentGroupException(EnumStudentGroupError.GROUP_MEMBER_LIMIT_EXCEEDED);
                }

                for (Long userId : memberUserIds) {
                        if (!studentRepository.existsById(userId)) {
                                throw new StudentGroupException(EnumStudentGroupError.STUDENT_NOT_FOUND);
                        }
                        if (groupMemberRepository.existsByRegistrationPeriodIdAndStudentUserId(
                                        createStudentGroupRequest.getRegistrationPeriodId(), userId)) {
                                throw new StudentGroupException(EnumStudentGroupError.STUDENT_ALREADY_IN_GROUP);
                        }
                }

                StudentGroup studentGroup = studentGroupRepository.save(
                                StudentGroup.builder()
                                                .registrationPeriodId(
                                                                createStudentGroupRequest.getRegistrationPeriodId())
                                                .name(createStudentGroupRequest.getName())
                                                .status(true)
                                                .createBy(leaderStudent.getUser().getEmail())
                                                .build());

                List<GroupMember> members = memberUserIds.stream().map(userId -> {
                        Student student = studentRepository.findById(userId)
                                        .orElseThrow(() -> new StudentGroupException(
                                                        EnumStudentGroupError.STUDENT_NOT_FOUND));

                        return GroupMember.builder()
                                        .studentGroup(studentGroup)
                                        .registrationPeriodId(createStudentGroupRequest.getRegistrationPeriodId())
                                        .student(student)
                                        .role(userId.equals(leaderStudent.getUserId()) ? GroupMemberRole.LEADER
                                                        : GroupMemberRole.MEMBER)
                                        .joinedAt(LocalDateTime.now())
                                        .build();
                }).toList();
                groupMemberRepository.saveAll(members);
                return studentGroupMapper.toResponse(studentGroup, members);

        }

        @Transactional(readOnly = true)
        public List<StudentGroupResponse> getAllStudentGroupService() {
                return studentGroupRepository.findAll().stream()
                                .map(studentGroup -> studentGroupMapper.toResponse(studentGroup,
                                                groupMemberRepository.findAllByStudentGroupId(studentGroup.getId())))
                                .toList();
        }

        // Lấy danh sách sinh viên theo nhóm đăng kí
        @Transactional(readOnly = true)
        public List<StudentGroupResponse> getAllByRegistrationPeriodService(Long registrationPeriodId) {
                return studentGroupRepository.findAllByRegistrationPeriodId(registrationPeriodId).stream()
                                .map(studentGroup -> studentGroupMapper.toResponse(studentGroup,
                                                groupMemberRepository.findAllByStudentGroupId(studentGroup.getId())))
                                .toList();
        }

        // Sinh viên xem các nhóm của chính mình
        @Transactional(readOnly = true)
        public List<StudentGroupResponse> getMyStudentGroupsService(String username) {
                Student student = studentRepository.findByUserUsername(username)
                                .orElseThrow(() -> new StudentGroupException(EnumStudentGroupError.STUDENT_NOT_FOUND));
                return groupMemberRepository.findAllByStudentUserId(student.getUserId()).stream()
                                .map(GroupMember::getStudentGroup)
                                .distinct()
                                .map(studentGroup -> studentGroupMapper.toResponse(studentGroup,
                                                groupMemberRepository.findAllByStudentGroupId(studentGroup.getId())))
                                .toList();
        }

        @Transactional(readOnly = true)
        public StudentGroupResponse getStudentGroupByIdService(Long id) {
                StudentGroup studentGroup = findStudentGroupById(id);
                return studentGroupMapper.toResponse(studentGroup,
                                groupMemberRepository.findAllByStudentGroupId(studentGroup.getId()));
        }

        @Transactional
        public void deleteStudentGroupService(Long id) {
                StudentGroup studentGroup = findStudentGroupById(id);
                if (topicRegistrationRepository.existsByStudentGroupId(id)) {
                        throw new StudentGroupException(EnumStudentGroupError.GROUP_HAS_REGISTRATIONS);
                }
                groupMemberRepository.deleteAllByStudentGroupId(id);
                studentGroupRepository.delete(studentGroup);
        }

        // function helper
        private StudentGroup findStudentGroupById(Long id) {
                return studentGroupRepository.findById(id)
                                .orElseThrow(() -> new StudentGroupException(
                                                EnumStudentGroupError.STUDENT_GROUP_NOT_FOUND));
        }
}
