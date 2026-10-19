package modules.evaluations.export;

import java.util.Arrays;
import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import modules.evaluations.dto.FinalResultResponse;
import modules.students.export.ExportExcelService;

@Service
@RequiredArgsConstructor
public class EvaluationExportExcelService {
    private final ExportExcelService excelService;

    public byte[] exportStatistics(List<FinalResultResponse> results) {
        List<List<?>> rows = results.stream()
                .map(result -> Arrays.asList(result.getId(), result.getAssignmentId(), result.getTopicId(),
                        result.getTopicTitle(), result.getStudentGroupId(), result.getStudentGroupName(),
                        result.getFinalScore(), result.isPublished(), result.getPublishedBy(), result.getPublishedAt(),
                        result.getEvaluations() == null ? 0 : result.getEvaluations().size()))
                .toList();
        return excelService.export("Thống kê", "THỐNG KÊ KẾT QUẢ KHÓA LUẬN",
                Arrays.asList("Mã kết quả", "Mã phân công", "Mã đề tài", "Tên đề tài", "Mã nhóm", "Tên nhóm",
                        "Điểm cuối", "Đã công bố", "Người công bố", "Thời gian công bố", "Số lượt đánh giá"), rows);
    }
}
