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

@Service
@RequiredArgsConstructor
public class LecturerService {
    private final LecturerRepository lecturerRepository;
    private final UserAccountService userAccountService;

    @Transactional
    public LecturerResponse register(RegisterLecturerRequest request) {
        if (lecturerRepository.existsByLecturerCode(request.getLecturerCode()))
            throw new IllegalArgumentException("Mã giảng viên đã tồn tại");

        User user = userAccountService.create(
                request.getUsername(), request.getEmail(), request.getPassword(), "LECTURER");
        Lecturer lecturer = lecturerRepository.save(Lecturer.builder()
                .user(user).lecturerCode(request.getLecturerCode())
                .departmentId(request.getDepartmentId()).academicTitle(request.getAcademicTitle())
                .maxReviewLoad(request.getMaxReviewLoad()).build());

        return LecturerResponse.builder()
                .userId(user.getId()).username(user.getUsername()).email(user.getEmail())
                .roles(user.getRoles().stream().map(Role::getName).collect(Collectors.toSet()))
                .lecturerCode(lecturer.getLecturerCode()).departmentId(lecturer.getDepartmentId())
                .academicTitle(lecturer.getAcademicTitle()).maxReviewLoad(lecturer.getMaxReviewLoad()).build();
    }
}
