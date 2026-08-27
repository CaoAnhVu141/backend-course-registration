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

@Service
@RequiredArgsConstructor
public class LecturerService {
    private final LecturerRepository lecturerRepository;
    private final UserAccountService userAccountService;
    private final LecturerMapper lecturerMapper;

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

        return lecturerMapper.toResponse(lecturer);
    }
}
