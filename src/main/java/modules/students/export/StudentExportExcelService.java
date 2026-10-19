package modules.students.export;

import java.util.Arrays;
import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import modules.students.dto.StudentResponse;

@Service
@RequiredArgsConstructor
public class StudentExportExcelService {
    private final ExportExcelService excelService;

    public byte[] export(List<StudentResponse> students) {
        List<? extends List<?>> rows = students.stream()
                .map(student -> (List<?>) Arrays.asList(student.getUserId(), student.getUsername(), student.getEmail(),
                        student.getStudentCode(), student.getDepartmentId(), student.getClassName(), student.getRoles()))
                .toList();
        return excelService.export("Sinh viên", "DANH SÁCH SINH VIÊN",
                Arrays.asList("Mã người dùng", "Tên đăng nhập", "Email", "Mã sinh viên", "Mã phòng ban",
                        "Lớp", "Vai trò"), rows);
    }
}
