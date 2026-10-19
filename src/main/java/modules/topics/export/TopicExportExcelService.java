package modules.topics.export;

import java.util.Arrays;
import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import modules.students.export.ExportExcelService;
import modules.topics.dto.TopicResponse;

@Service
@RequiredArgsConstructor
public class TopicExportExcelService {
    private final ExportExcelService excelService;

    public byte[] export(List<TopicResponse> topics) {
        List<? extends List<?>> rows = topics.stream()
                .map(topic -> (List<?>) Arrays.asList(topic.getId(), topic.getTitle(), topic.getRequirements(),
                        topic.getDepartmentId(), topic.getRegistrationPeriodId(), topic.getProposedBy(),
                        topic.getStatus(), topic.getSupervisorIds(), topic.getRejectionReason()))
                .toList();
        return excelService.export("Đề tài", "DANH SÁCH ĐỀ TÀI",
                Arrays.asList("Mã đề tài", "Tên đề tài", "Yêu cầu", "Mã phòng ban", "Mã kỳ đăng ký",
                        "Người đề xuất", "Trạng thái", "Mã giảng viên hướng dẫn", "Lý do từ chối"), rows);
    }
}
