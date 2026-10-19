package modules.lecturers.service;

import lombok.RequiredArgsConstructor;
import modules.auth.entity.User;
import modules.auth.service.UserAccountService;
import modules.lecturers.dto.*;
import modules.lecturers.entity.Lecturer;
import modules.lecturers.repository.LecturerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.stream.Collectors;
import modules.lecturers.mapper.LecturerMapper;
import modules.departments.repository.DepartmentRepository;
import modules.lecturers.enums.EnumLecturerError;
import modules.lecturers.exception.LecturerException;
import java.util.List;
import modules.expertisetags.entity.ExpertiseTag;
import modules.expertisetags.enums.EnumExpertiseTagError;
import modules.expertisetags.exception.ExpertiseTagException;
import modules.expertisetags.repository.ExpertiseTagRepository;
import modules.lecturers.entity.LecturerExpertise;
import modules.lecturers.entity.LecturerExpertiseId;
import modules.lecturers.repository.LecturerExpertiseRepository;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class LecturerService {
    private final LecturerRepository lecturerRepository;
    private final UserAccountService userAccountService;
    private final LecturerMapper lecturerMapper;
    private final DepartmentRepository departmentRepository;
    private final ExpertiseTagRepository expertiseTagRepository;
    private final LecturerExpertiseRepository lecturerExpertiseRepository;

    @Transactional
    public LecturerResponse register(RegisterLecturerRequest request) {
        if (lecturerRepository.existsByLecturerCode(request.getLecturerCode())) {
            throw new LecturerException(EnumLecturerError.LECTURER_CODE_EXISTS);
        }

        if (!departmentRepository.existsById(request.getDepartmentId())) {
            throw new LecturerException(EnumLecturerError.DEPARTMENT_NOT_FOUND);
        }

        User user = userAccountService.create(
                request.getUsername(), request.getEmail(), request.getPassword(), "LECTURER");
        Lecturer lecturer = lecturerRepository.save(Lecturer.builder()
                .user(user)
                .lecturerCode(request.getLecturerCode())
                .departmentId(request.getDepartmentId())
                .academicTitle(request.getAcademicTitle())
                .maxReviewLoad(request.getMaxReviewLoad())
                .build());
        saveLecturerExpertiseTags(lecturer, request.getExpertiseTagIds());

        return lecturerMapper.toResponse(lecturer, getExpertiseTagIds(lecturer.getUserId()));
    }

    @Transactional(readOnly = true)
    public List<LecturerResponse> getAllLecturerService() {
        return lecturerRepository.findAll().stream()
                .map(lecturer -> lecturerMapper.toResponse(lecturer, getExpertiseTagIds(lecturer.getUserId())))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public LecturerResponse getLecturerByIdService(Long userId) {
        Lecturer lecturer = findLecturerById(userId);
        return lecturerMapper.toResponse(lecturer, getExpertiseTagIds(lecturer.getUserId()));
    }

    @Transactional(readOnly = true)
    public LecturerResponse getMyProfileService(String username) {
        Lecturer lecturer = lecturerRepository.findByUserUsername(username)
                .orElseThrow(() -> new LecturerException(EnumLecturerError.LECTURER_NOT_FOUND));
        return lecturerMapper.toResponse(lecturer, getExpertiseTagIds(lecturer.getUserId()));
    }

    @Transactional
    public LecturerResponse updateLecturerService(Long userId, UpdateLecturerRequest updateLecturerRequest) {
        Lecturer lecturer = findLecturerById(userId);

        if (!lecturer.getLecturerCode().equals(updateLecturerRequest.getLecturerCode())
                && lecturerRepository.existsByLecturerCode(updateLecturerRequest.getLecturerCode())) {
            throw new LecturerException(EnumLecturerError.LECTURER_CODE_EXISTS);
        }

        if (!departmentRepository.existsById(updateLecturerRequest.getDepartmentId())) {
            throw new LecturerException(EnumLecturerError.DEPARTMENT_NOT_FOUND);
        }

        lecturer.setLecturerCode(updateLecturerRequest.getLecturerCode());
        lecturer.setDepartmentId(updateLecturerRequest.getDepartmentId());
        lecturer.setAcademicTitle(updateLecturerRequest.getAcademicTitle());
        lecturer.setMaxReviewLoad(updateLecturerRequest.getMaxReviewLoad());
        updateLecturerExpertiseTags(lecturer, updateLecturerRequest.getExpertiseTagIds());

        return lecturerMapper.toResponse(lecturer, getExpertiseTagIds(lecturer.getUserId()));
    }

    @Transactional
    public void deleteLecturerService(Long userId) {
        Lecturer lecturer = findLecturerById(userId);
        lecturer.getUser().setStatus(false);
    }

    // function helper
    private Lecturer findLecturerById(Long userId) {
        return lecturerRepository.findById(userId)
                .orElseThrow(() -> new LecturerException(EnumLecturerError.LECTURER_NOT_FOUND));
    }

    private void saveLecturerExpertiseTags(Lecturer lecturer, Set<Long> expertiseTagIds) {
        if (expertiseTagIds == null || expertiseTagIds.isEmpty()) {
            return;
        }

        Set<LecturerExpertise> lecturerExpertises = expertiseTagIds.stream()
                .map(expertiseTagId -> {
                    ExpertiseTag expertiseTag = expertiseTagRepository.findById(expertiseTagId)
                            .orElseThrow(() -> new ExpertiseTagException(
                                    EnumExpertiseTagError.EXPERTISE_TAG_NOT_FOUND));

                    return LecturerExpertise.builder()
                            .id(LecturerExpertiseId.builder()
                                    .userId(lecturer.getUserId())
                                    .expertiseTagId(expertiseTag.getId())
                                    .build())
                            .lecturer(lecturer)
                            .expertiseTag(expertiseTag)
                            .build();
                })
                .collect(Collectors.toSet());

        lecturerExpertiseRepository.saveAll(lecturerExpertises);
    }

    private void updateLecturerExpertiseTags(Lecturer lecturer, Set<Long> expertiseTagIds) {
        lecturerExpertiseRepository.deleteAllByLecturerUserId(lecturer.getUserId());
        saveLecturerExpertiseTags(lecturer, expertiseTagIds);
    }

    private Set<Long> getExpertiseTagIds(Long lecturerUserId) {
        return lecturerExpertiseRepository.findAllByLecturerUserId(lecturerUserId)
                .stream()
                .map(lecturerExpertise -> lecturerExpertise.getExpertiseTag().getId())
                .collect(Collectors.toSet());
    }
}
