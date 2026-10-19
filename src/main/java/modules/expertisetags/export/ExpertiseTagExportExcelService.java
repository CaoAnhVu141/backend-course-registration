package modules.expertisetags.export;

import java.util.Arrays;
import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import modules.expertisetags.dto.ExpertiseTagResponse;
import modules.students.export.ExportExcelService;

@Service
@RequiredArgsConstructor
public class ExpertiseTagExportExcelService {
    private final ExportExcelService excelService;

    public byte[] export(List<ExpertiseTagResponse> tags) {
        List<List<?>> rows = tags.stream()
                .map(tag -> Arrays.asList(tag.getId(), tag.getName(), tag.isStatus()))
                .toList();
        return excelService.export("Chuyên môn", "DANH SÁCH CHUYÊN MÔN",
                Arrays.asList("Mã chuyên môn", "Tên chuyên môn", "Trạng thái"), rows);
    }
}
