package modules.lecturers.export;

import java.util.Arrays;
import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import modules.lecturers.dto.LecturerResponse;
import modules.students.export.ExportExcelService;

@Service
@RequiredArgsConstructor
public class LecturerExportExcelService {
    private final ExportExcelService excelService;

    public byte[] export(List<LecturerResponse> lecturers) {
        List<? extends List<?>> rows = lecturers.stream()
                .map(lecturer -> (List<?>) Arrays.asList(lecturer.getUserId(), lecturer.getUsername(), lecturer.getEmail(),
                        lecturer.getLecturerCode(), lecturer.getDepartmentId(), lecturer.getAcademicTitle(),
                        lecturer.getMaxReviewLoad(), lecturer.getExpertiseTagIds(), lecturer.getRoles()))
                .toList();
        return excelService.export("Giảng viên", "DANH SÁCH GIẢNG VIÊN",
                Arrays.asList("Mã người dùng", "Tên đăng nhập", "Email", "Mã giảng viên", "Mã phòng ban",
                        "Học hàm/Học vị", "Tải phản biện tối đa", "Mã chuyên môn", "Vai trò"), rows);
    }
}
