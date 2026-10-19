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
import modules.students.mapper.StudentMapper;
import modules.departments.repository.DepartmentRepository;
import modules.students.dto.UpdateStudentRequest;
import modules.students.enums.EnumStudentError;
import modules.students.exception.StudentException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudentService {
    private final StudentRepository studentRepository;
    private final UserAccountService userAccountService;
    private final StudentMapper studentMapper;
    private final DepartmentRepository departmentRepository;

    @Transactional
    public StudentResponse register(RegisterStudentRequest request) {
        if (studentRepository.existsByStudentCode(request.getStudentCode())) {
            throw new StudentException(EnumStudentError.STUDENT_CODE_EXISTS);
        }

        // @check if department exists
        if (!departmentRepository.existsById(request.getDepartmentId())) {
            throw new StudentException(EnumStudentError.DEPARTMENT_NOT_FOUND);
        }

        User user = userAccountService.create(
                request.getUsername(), request.getEmail(), request.getPassword(), "STUDENT");
        Student student = studentRepository.save(Student.builder()
                .user(user)
                .studentCode(request.getStudentCode())
                .departmentId(request.getDepartmentId())
                .className(request.getClassName())
                .build());

        return studentMapper.toResponse(student);
    }

    @Transactional(readOnly = true)
    public List<StudentResponse> getAllStudentsService() {
        return studentRepository.findAll().stream()
                .map(studentMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public StudentResponse getStudentByIdService(Long userId) {
        Student student = findStudentById(userId);
        return studentMapper.toResponse(student);
    }

    @Transactional(readOnly = true)
    public StudentResponse getMyProfileService(String username) {
        Student student = studentRepository.findByUserUsername(username)
                .orElseThrow(() -> new StudentException(EnumStudentError.STUDENT_NOT_FOUND));
        return studentMapper.toResponse(student);
    }

    @Transactional
    public StudentResponse updateStudentService(UpdateStudentRequest updateStudentRequest, Long userId) {
        Student student = findStudentById(userId);
        // check student code exists
        if (!student.getStudentCode().equals(updateStudentRequest.getStudentCode())
                && studentRepository.existsByStudentCode(updateStudentRequest.getStudentCode())) {
            throw new StudentException(EnumStudentError.STUDENT_CODE_EXISTS);
        }
        // check department exists
        if (!departmentRepository.existsById(updateStudentRequest.getDepartmentId())) {
            throw new StudentException(EnumStudentError.DEPARTMENT_NOT_FOUND);
        }
        student.setStudentCode(updateStudentRequest.getStudentCode());
        student.setDepartmentId(updateStudentRequest.getDepartmentId());
        student.setClassName(updateStudentRequest.getClassName());
        return studentMapper.toResponse(student);
    }

    @Transactional
    public void deleteStudentService(Long userId) {
        Student student = findStudentById(userId);
        student.getUser().setStatus(false);
    }

    // function helper
    private Student findStudentById(Long userId) {
        return studentRepository.findById(userId)
                .orElseThrow(() -> new StudentException(EnumStudentError.STUDENT_NOT_FOUND));
    }
}
