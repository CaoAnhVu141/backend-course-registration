package modules.lecturers.service;

import lombok.RequiredArgsConstructor;
import modules.auth.entity.Role;
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

@Service
@RequiredArgsConstructor
public class LecturerService {
    private final LecturerRepository lecturerRepository;
    private final UserAccountService userAccountService;
    private final LecturerMapper lecturerMapper;
    private final DepartmentRepository departmentRepository;

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

        return lecturerMapper.toResponse(lecturer);
    }
}
