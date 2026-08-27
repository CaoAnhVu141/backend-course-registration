package modules.lecturers.mapper;

import modules.auth.entity.Role;
import modules.lecturers.dto.LecturerResponse;
import modules.lecturers.entity.Lecturer;
import org.springframework.stereotype.Component;
import java.util.stream.Collectors;
import modules.auth.entity.Role;

@Component
public class LecturerMapper {

    public LecturerResponse toResponse(Lecturer lecturer) {
        return LecturerResponse.builder()
                .userId(lecturer.getUser().getId())
                .username(lecturer.getUser().getUsername())
                .email(lecturer.getUser().getEmail())
                .roles(
                        lecturer.getUser()
                                .getRoles()
                                .stream()
                                .map(Role::getName)
                                .collect(Collectors.toSet()))
                .lecturerCode(lecturer.getLecturerCode())
                .departmentId(lecturer.getDepartmentId())
                .academicTitle(lecturer.getAcademicTitle())
                .maxReviewLoad(lecturer.getMaxReviewLoad())
                .build();
    }
}