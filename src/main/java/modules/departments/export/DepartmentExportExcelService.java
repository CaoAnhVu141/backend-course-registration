package modules.departments.export;

import java.util.Arrays;
import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import modules.departments.dto.DepartmentResponse;
import modules.students.export.ExportExcelService;

@Service
@RequiredArgsConstructor
public class DepartmentExportExcelService {
    private final ExportExcelService excelService;

    public byte[] export(List<DepartmentResponse> departments) {
        List<List<?>> rows = departments.stream()
                .map(department -> Arrays.asList(department.getDepartmentId(), department.getName()))
                .toList();
        return excelService.export("Phòng ban", "DANH SÁCH PHÒNG BAN",
                Arrays.asList("Mã phòng ban", "Tên phòng ban"), rows);
    }
}
