package modules.students.mapper;

import modules.auth.entity.Role;
import modules.students.dto.StudentResponse;
import modules.students.entity.Student;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class StudentMapper {

    public StudentResponse toResponse(Student student) {
        return StudentResponse.builder()
                .userId(student.getUser().getId())
                .username(student.getUser().getUsername())
                .email(student.getUser().getEmail())
                .roles(
                        student.getUser()
                                .getRoles()
                                .stream()
                                .map(Role::getName)
                                .collect(Collectors.toSet()))
                .studentCode(student.getStudentCode())
                .departmentId(student.getDepartmentId())
                .className(student.getClassName())
                .build();
    }
}