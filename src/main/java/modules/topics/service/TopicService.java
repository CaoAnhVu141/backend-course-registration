package modules.topics.service;

import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import modules.departments.repository.DepartmentRepository;
import modules.lecturers.entity.Lecturer;
import modules.lecturers.repository.LecturerRepository;
import modules.registration_period.repository.RegistrationPeriodRepository;
import modules.topics.dto.CreateTopicRequest;
import modules.topics.dto.TopicResponse;
import modules.topics.dto.UpdateTopicRequest;
import modules.topics.entity.Topic;
import modules.topics.enums.TopicStatus;
import modules.topics.mapper.TopicMapper;
import modules.topics.repository.TopicRepository;
import modules.topics.repository.TopicSupervisorRepository;

import modules.topics.entity.TopicSupervisor;
import modules.topics.entity.TopicSupervisorId;
import modules.topics.enums.EnumTopicError;
import modules.topics.exception.TopicException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TopicService {

    private final TopicRepository topicRepository;
    private final TopicSupervisorRepository topicSupervisorRepository;
    private final LecturerRepository lecturerRepository;
    private final DepartmentRepository departmentRepository;
    private final RegistrationPeriodRepository registrationPeriodRepository;
    private final TopicMapper topicMapper;

    @Transactional
    public TopicResponse createTopicService(CreateTopicRequest request, String username) {
        validateReferences(request.getDepartmentId(), request.getRegistrationPeriodId());

        Lecturer lecturer = lecturerRepository.findByUserUsername(username)
                .orElseThrow(() -> new TopicException(EnumTopicError.LECTURER_NOT_FOUND));

        Topic topic = topicRepository.save(
                Topic.builder()
                        .title(request.getTitle())
                        .requirements(request.getRequirements())
                        .departmentId(request.getDepartmentId())
                        .registrationPeriodId(request.getRegistrationPeriodId())
                        .proposedBy(lecturer.getUser().getEmail())
                        .status(TopicStatus.PENDING)
                        .build());
        saveTopicSupervisor(topic, lecturer);
        return topicMapper.toResponse(topic, Set.of(lecturer.getUserId()));
    }

    @Transactional(readOnly = true)
    public List<TopicResponse> getAllTopicsService() {
        List<Topic> topics = topicRepository.findAll();
        return topicRepository.findAll()
                .stream()
                .map(topic -> topicMapper.toResponse(topic, getSupervisorIds(topic.getId())))
                .toList();
    }

    // Lấy đề tài đã được duyệt
    @Transactional(readOnly = true)
    public List<TopicResponse> getTopicPublishedService() {
        return topicRepository.findAllByStatus(TopicStatus.PUBLISHED)
                .stream()
                .map(topic -> topicMapper.toResponse(topic, getSupervisorIds(topic.getId())))
                .toList();
    }

    @Transactional(readOnly = true)
    public TopicResponse getTopicByIdService(Long topicId) {
        Topic topic = findTopicById(topicId);
        return topicMapper.toResponse(topic, getSupervisorIds(topic.getId()));
    }

    // @dành cho giảng viên
    @Transactional(readOnly = true)
    public List<TopicResponse> getMyTopicService(String username) {
        Lecturer lecturer = lecturerRepository.findByUserUsername(username)
                .orElseThrow(() -> new TopicException(EnumTopicError.LECTURER_NOT_FOUND));
        return topicSupervisorRepository.findAllByLecturerUserId(lecturer.getUserId())
                .stream()
                .map(TopicSupervisor::getTopic)
                .map(topic -> topicMapper.toResponse(topic, getSupervisorIds(topic.getId())))
                .toList();
    }

    @Transactional
    public TopicResponse updateTopicService(Long topicId, UpdateTopicRequest updateTopicRequest) {
        validateReferences(updateTopicRequest.getDepartmentId(), updateTopicRequest.getRegistrationPeriodId());
        Topic topic = findTopicById(topicId);
        if (topic.getStatus() == TopicStatus.PUBLISHED) {
            throw new TopicException(EnumTopicError.TOPIC_ALREADY_PUBLISHED);
        }
        topic.setTitle(updateTopicRequest.getTitle());
        topic.setRequirements(updateTopicRequest.getRequirements());
        topic.setDepartmentId(updateTopicRequest.getDepartmentId());
        topic.setRegistrationPeriodId(updateTopicRequest.getRegistrationPeriodId());
        return topicMapper.toResponse(topic, getSupervisorIds(topic.getId()));
    }

    @Transactional
    public void deleteTopicService(Long topicId) {
        Topic topic = findTopicById(topicId);
        if (topic.getStatus() == TopicStatus.PUBLISHED) {
            throw new TopicException(EnumTopicError.TOPIC_ALREADY_PUBLISHED);
        }
        topicSupervisorRepository.deleteAllByTopicId(topicId);
        topicRepository.delete(topic);
    }

    @Transactional
    public TopicResponse approveTopicService(Long topicId) {
        Topic topic = findTopicById(topicId);
        if (topic.getStatus() != TopicStatus.PENDING) {
            throw new TopicException(EnumTopicError.ONLY_PENDING_TOPIC_CAN_BE_APPROVED);
        }
        topic.setStatus(TopicStatus.APPROVED);
        return topicMapper.toResponse(topic, getSupervisorIds(topic.getId()));
    }

    @Transactional
    public TopicResponse publishTopicService(Long topicId) {
        Topic topic = findTopicById(topicId);
        if (topic.getStatus() != TopicStatus.APPROVED) {
            throw new TopicException(EnumTopicError.INVALID_TOPIC_STATUS);
        }
        topic.setStatus(TopicStatus.PUBLISHED);
        return topicMapper.toResponse(topic, getSupervisorIds(topic.getId()));
    }

    @Transactional
    public TopicResponse rejectTopicService(Long id, String reason) {
        Topic topic = findTopicById(id);

        if (topic.getStatus() != TopicStatus.PENDING) {
            throw new TopicException(EnumTopicError.ONLY_PENDING_TOPIC_CAN_BE_REJECTED);
        }
        topic.setStatus(TopicStatus.REJECTED);
        topic.setRejectionReason(reason.trim());

        return topicMapper.toResponse(topic, getSupervisorIds(topic.getId()));
    }

    // function helper
    private void validateReferences(Long departmentId, Long registrationPeriodId) {
        if (!departmentRepository.existsById(departmentId)) {
            throw new TopicException(EnumTopicError.DEPARTMENT_NOT_FOUND);
        }

        if (!registrationPeriodRepository.existsById(registrationPeriodId)) {
            throw new TopicException(EnumTopicError.REGISTRATION_PERIOD_NOT_FOUND);
        }
    }

    private Topic findTopicById(Long id) {
        return topicRepository.findById(id)
                .orElseThrow(() -> new TopicException(EnumTopicError.TOPIC_NOT_FOUND));
    }

    private void saveTopicSupervisor(Topic topic, Lecturer lecturer) {
        TopicSupervisor topicSupervisor = TopicSupervisor.builder()
                .id(TopicSupervisorId.builder()
                        .userId(lecturer.getUserId())
                        .topicId(topic.getId())
                        .build())
                .lecturer(lecturer)
                .topic(topic)
                .build();

        topicSupervisorRepository.save(topicSupervisor);
    }

    private Set<Long> getSupervisorIds(Long topicId) {
        return topicSupervisorRepository.findAllByTopicId(topicId)
                .stream()
                .map(topicSupervisor -> topicSupervisor.getLecturer().getUserId())
                .collect(Collectors.toSet());
    }
}
