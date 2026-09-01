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

    @Transactional
    public TopicRegistrationResponse createTopicRegistrationService(
            CreateTopicRegistrationRequest cretaeTopicRegistrationRequest, String username) {

        User users = findUserByUsername(username);
        StudentGroup studentGroup = studentGroupRepository.findById(cretaeTopicRegistrationRequest.getStudentGroupId())
                .orElseThrow(() -> new TopicRegistrationException(
                        EnumTopicRegistrationError.STUDENT_GROUP_NOT_FOUND));

        if(!studentGroup.isStatus() {
            throw new TopicRegistrationException(EnumTopicRegistrationError.STUDENT_GROUP_INACTIVE);
        });

    Topic topic = topicRepository.findById(cretaeTopicRegistrationRequest.getTopicId())
                .orElseThrow(() -> new TopicRegistrationException(
                        EnumTopicRegistrationError.TOPIC_NOT_FOUND));

    if(topic.getStatus()!=TopicStatus.PUBLISHED)
    {
        throw new TopicRegistrationException(EnumTopicRegistrationError.TOPIC_NOT_PUBLISHED);
    }

    if(topicRegistrationRepository.existsByStudentGroupIdAndStatusIn(studentGroup.getId(),List.of(TopicRegistrationStatus.PENDING,TopicRegistrationStatus.APPROVED)))
    {
        throw new TopicRegistrationException(EnumTopicRegistrationError.STUDENT_GROUP_ALREADY_REGISTERED);
    }

    if(topicRegistrationRepository.existsByTopicIdAndStatus(topic.getId(),TopicRegistrationStatus.APPROVED))
    {
        throw new TopicRegistrationException(EnumTopicRegistrationError.TOPIC_ALREADY_APPROVED);
    }

    TopicRegistration topicRegistration = topicRegistrationRepository.save(
            TopicRegistration.builder()
                    .studentGroup(studentGroup)
                    .topic(topic)
                    .status(TopicRegistrationStatus.PENDING)
                    .registeredBy(user.getEmail())
                    .registeredAt(LocalDateTime.now())
                    .build());return topicRegistrationMapper.toResponse(topicRegistration);

    }

    @Transactional(readOnly = true)
    public List<TopicRegistrationResponse> getAllTopicRegistrationsService() {
        return topicRegistrationRepository.findAll()
                .stream()
                .map(topicRegistrationMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<TopicRegistrationResponse> getTopicRegistrationByIdService(Long id) {
        TopicRegistration topicRegistration = findTopicRegistrationById(id);
        return topicRegistrationMapper.toResponse(topicRegistration);
    }

    // function helper
    private TopicRegistration ;findTopicRegistrationById(Long id) {
        return topicRegistrationRepository.findById(id)
                .orElseThrow(() -> new TopicRegistrationException(
                        EnumTopicRegistrationError.TOPIC_REGISTRATION_NOT_FOUND));
    }

    private User findUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new TopicRegistrationException(EnumTopicRegistrationError.USER_NOT_FOUND));
    }
}
