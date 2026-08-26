package modules.students.service;

import lombok.RequiredArgsConstructor;
import modules.auth.entity.Role;
import modules.auth.entity.User;
import modules.auth.service.UserAccountService;
import modules.students.dto.RegisterStudentRequest;
import modules.students.dto.StudentResponse;
import modules.students.entity.Student;
import modules.students.repository.StudentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudentService {
    private final StudentRepository studentRepository;
    private final UserAccountService userAccountService;

    @Transactional
    public StudentResponse register(RegisterStudentRequest request) {
        if (studentRepository.existsByStudentCode(request.getStudentCode()))
            throw new IllegalArgumentException("Mã sinh viên đã tồn tại");

        User user = userAccountService.create(
                request.getUsername(), request.getEmail(), request.getPassword(), "STUDENT");
        Student student = studentRepository.save(Student.builder()
                .user(user)
                .studentCode(request.getStudentCode())
                .departmentId(request.getDepartmentId())
                .className(request.getClassName())
                .build());

        return StudentResponse.builder()
                .userId(user.getId()).username(user.getUsername()).email(user.getEmail())
                .roles(user.getRoles().stream().map(Role::getName).collect(Collectors.toSet()))
                .studentCode(student.getStudentCode()).departmentId(student.getDepartmentId())
                .className(student.getClassName()).build();
    }
}
