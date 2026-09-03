package modules.topic_registrations.service;

import lombok.RequiredArgsConstructor;
import modules.auth.entity.User;
import modules.auth.repository.UserRepository;
import modules.student_group.entity.StudentGroup;
import modules.student_group.repository.StudentGroupRepository;
import modules.topic_registrations.dto.CreateTopicRegistrationRequest;
import modules.topic_registrations.dto.RejectTopicRegistrationRequest;
import modules.topic_registrations.dto.TopicRegistrationResponse;
import modules.topic_registrations.entity.TopicRegistration;
import modules.topic_registrations.enums.EnumTopicRegistrationError;
import modules.topic_registrations.enums.TopicRegistrationStatus;
import modules.topic_registrations.exception.TopicRegistrationException;
import modules.topic_registrations.mapper.TopicRegistrationMapper;
import modules.topic_registrations.repository.TopicRegistrationRepository;
import modules.topics.entity.Topic;
import modules.topics.enums.TopicStatus;
import modules.topics.repository.TopicRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import modules.registration_period.entity.RegistrationPeriod;
import modules.registration_period.repository.RegistrationPeriodRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TopicRegistrationService {

    private final TopicRegistrationRepository topicRegistrationRepository;
    private final StudentGroupRepository studentGroupRepository;
    private final TopicRepository topicRepository;
    private final UserRepository userRepository;
    private final TopicRegistrationMapper topicRegistrationMapper;
    private final RegistrationPeriodRepository registrationPeriodRepository;

    @Transactional
    public TopicRegistrationResponse createTopicRegistrationService(
            CreateTopicRegistrationRequest createTopicRegistrationRequest,
            String username) {
        User user = findUserByUsername(username);

        StudentGroup studentGroup = studentGroupRepository.findById(createTopicRegistrationRequest.getStudentGroupId())
                .orElseThrow(() -> new TopicRegistrationException(
                        EnumTopicRegistrationError.STUDENT_GROUP_NOT_FOUND));

        if (!studentGroup.isStatus()) {
            throw new TopicRegistrationException(EnumTopicRegistrationError.STUDENT_GROUP_INACTIVE);
        }

        Topic topic = topicRepository.findById(createTopicRegistrationRequest.getTopicId())
                .orElseThrow(() -> new TopicRegistrationException(
                        EnumTopicRegistrationError.TOPIC_NOT_FOUND));

        if (topic.getStatus() != TopicStatus.PUBLISHED) {
            throw new TopicRegistrationException(EnumTopicRegistrationError.TOPIC_NOT_PUBLISHED);
        }

        if (!topic.getRegistrationPeriodId().equals(studentGroup.getRegistrationPeriodId())) {
            throw new TopicRegistrationException(EnumTopicRegistrationError.TOPIC_NOT_IN_REGISTRATION_PERIOD);
        }

        RegistrationPeriod registrationPeriod = registrationPeriodRepository
                .findById(studentGroup.getRegistrationPeriodId())
                .orElseThrow(() -> new TopicRegistrationException(
                        EnumTopicRegistrationError.REGISTRATION_PERIOD_NOT_FOUND));

        LocalDateTime now = LocalDateTime.now();

        if (now.isBefore(registrationPeriod.getStudentRegistrationStart())
                || now.isAfter(registrationPeriod.getStudentRegistrationEnd())) {
            throw new TopicRegistrationException(EnumTopicRegistrationError.STUDENT_REGISTRATION_NOT_OPEN);
        }

        if (topicRegistrationRepository.existsByStudentGroupIdAndStatusIn(
                studentGroup.getId(),
                List.of(TopicRegistrationStatus.PENDING, TopicRegistrationStatus.APPROVED))) {
            throw new TopicRegistrationException(EnumTopicRegistrationError.STUDENT_GROUP_ALREADY_REGISTERED);
        }

        if (topicRegistrationRepository.existsByTopicIdAndStatus(
                topic.getId(),
                TopicRegistrationStatus.APPROVED)) {
            throw new TopicRegistrationException(EnumTopicRegistrationError.TOPIC_ALREADY_APPROVED);
        }

        TopicRegistration topicRegistration = topicRegistrationRepository.save(
                TopicRegistration.builder()
                        .studentGroup(studentGroup)
                        .topic(topic)
                        .status(TopicRegistrationStatus.PENDING)
                        .registeredBy(user.getEmail())
                        .registeredAt(LocalDateTime.now())
                        .build());

        return topicRegistrationMapper.toResponse(topicRegistration);
    }

    @Transactional(readOnly = true)
    public List<TopicRegistrationResponse> getAllTopicRegistrationsService() {
        return topicRegistrationRepository.findAll()
                .stream()
                .map(topicRegistrationMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public TopicRegistrationResponse getTopicRegistrationByIdService(Long id) {
        TopicRegistration topicRegistration = findTopicRegistrationById(id);
        return topicRegistrationMapper.toResponse(topicRegistration);
    }

    // Lấy theo danh sách đăng kí nhóm
    @Transactional(readOnly = true)
    public List<TopicRegistrationResponse> getByStudentGroupService(Long studentGroupId) {
        return topicRegistrationRepository.findAllByStudentGroupId(studentGroupId)
                .stream()
                .map(topicRegistrationMapper::toResponse)
                .toList();
    }

    // Lấy danh sách theo đề tài
    @Transactional(readOnly = true)
    public List<TopicRegistrationResponse> getByTopicService(Long topicId) {
        return topicRegistrationRepository.findAllByTopicId(topicId)
                .stream()
                .map(topicRegistrationMapper::toResponse)
                .toList();
    }

    // Chấp nhận đề tài đăng kí
    @Transactional
    public TopicRegistrationResponse approveTopicRegistrationService(Long id, String username) {
        User user = findUserByUsername(username);
        TopicRegistration topicRegistration = findTopicRegistrationById(id);
        if (topicRegistration.getStatus() != TopicRegistrationStatus.PENDING) {
            throw new TopicRegistrationException(EnumTopicRegistrationError.INVALID_TOPIC_REGISTRATION_STATUS);
        }
        if (topicRegistrationRepository.existsByTopicIdAndStatus(topicRegistration.getTopic().getId(),
                TopicRegistrationStatus.APPROVED)) {
            throw new TopicRegistrationException(EnumTopicRegistrationError.TOPIC_ALREADY_APPROVED);
        }

        topicRegistration.setStatus(TopicRegistrationStatus.APPROVED);
        topicRegistration.setDecidedBy(user.getEmail());
        topicRegistration.setRejectedReason(null);
        return topicRegistrationMapper.toResponse(topicRegistration);
    }

    // Từ chối đề tài đăng kí
    @Transactional
    public TopicRegistrationResponse rejectTopicRegistrationService(Long id,
            RejectTopicRegistrationRequest rejectTopicRegistrationRequest,
            String username) {
        User user = findUserByUsername(username);
        TopicRegistration topicRegistration = findTopicRegistrationById(id);
        if (topicRegistration.getStatus() != TopicRegistrationStatus.PENDING) {
            throw new TopicRegistrationException(EnumTopicRegistrationError.INVALID_TOPIC_REGISTRATION_STATUS);
        }
        topicRegistration.setStatus(TopicRegistrationStatus.REJECTED);
        topicRegistration.setDecidedBy(user.getEmail());
        topicRegistration.setRejectedReason(rejectTopicRegistrationRequest.getRejectionReason().trim());
        return topicRegistrationMapper.toResponse(topicRegistration);
    }

    // Xóa đề tài đăng kí
    // @Transactional
    // public TopicRegistrationResponse cancelTopicRegistrationService(Long id,
    // String username) {
    // TopicRegistration topicRegistration = findTopicRegistrationById(id);
    // if (topicRegistration.getStatus() != TopicRegistrationStatus.PENDING) {
    // throw new
    // TopicRegistrationException(EnumTopicRegistrationError.INVALID_TOPIC_REGISTRATION_STATUS);
    // }
    // topicRegistration.setStatus(TopicRegistrationStatus.CANCELLED);
    // return topicRegistrationMapper.toResponse(topicRegistration);
    // }
    @Transactional
    public TopicRegistrationResponse cancelTopicRegistrationService(Long id, String username) {
        User user = findUserByUsername(username);
        TopicRegistration topicRegistration = findTopicRegistrationById(id);

        if (!topicRegistration.getRegisteredBy().equals(user.getEmail())) {
            throw new TopicRegistrationException(EnumTopicRegistrationError.NOT_REGISTRATION_OWNER);
        }

        if (topicRegistration.getStatus() != TopicRegistrationStatus.PENDING) {
            throw new TopicRegistrationException(EnumTopicRegistrationError.INVALID_TOPIC_REGISTRATION_STATUS);
        }

        topicRegistration.setStatus(TopicRegistrationStatus.CANCELLED);

        return topicRegistrationMapper.toResponse(topicRegistration);
    }

    // function helper
    private TopicRegistration findTopicRegistrationById(Long id) {
        return topicRegistrationRepository.findById(id)
                .orElseThrow(() -> new TopicRegistrationException(
                        EnumTopicRegistrationError.TOPIC_REGISTRATION_NOT_FOUND));
    }

    private User findUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new TopicRegistrationException(EnumTopicRegistrationError.USER_NOT_FOUND));
    }
}
