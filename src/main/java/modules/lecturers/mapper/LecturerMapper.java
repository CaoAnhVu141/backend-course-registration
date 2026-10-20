package modules.lecturers.mapper;

import modules.auth.entity.Role;
import modules.lecturers.dto.LecturerResponse;
import modules.lecturers.entity.Lecturer;
import org.springframework.stereotype.Component;
import java.util.stream.Collectors;
import java.util.Collections;
import java.util.Set;

@Component
public class LecturerMapper {

    public LecturerResponse toResponse(Lecturer lecturer, Set<Long> expertiseTagIds) {
        return LecturerResponse.builder()
                .userId(lecturer.getUser().getId())
                .username(lecturer.getUser().getUsername())
                .email(lecturer.getUser().getEmail())
                .name(lecturer.getUser().getName())
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
                .expertiseTagIds(expertiseTagIds)
                .build();
    }
}