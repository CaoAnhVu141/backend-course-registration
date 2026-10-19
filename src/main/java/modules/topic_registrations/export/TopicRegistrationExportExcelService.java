package modules.topic_registrations.export;

import java.util.Arrays;
import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import modules.students.export.ExportExcelService;
import modules.topic_registrations.dto.TopicRegistrationResponse;

@Service
@RequiredArgsConstructor
public class TopicRegistrationExportExcelService {
    private final ExportExcelService excelService;

    public byte[] export(List<TopicRegistrationResponse> registrations) {
        List<? extends List<?>> rows = registrations.stream()
                .map(registration -> (List<?>) Arrays.asList(registration.getId(), registration.getStudentGroupId(),
                        registration.getStudentGroupName(), registration.getTopicId(), registration.getTopicTitle(),
                        registration.getStatus(), registration.getRegisteredBy(), registration.getRegisteredAt(),
                        registration.getDecidedBy(), registration.getRejectionReason()))
                .toList();
        return excelService.export("Đăng ký đề tài", "DANH SÁCH ĐĂNG KÝ ĐỀ TÀI",
                Arrays.asList("Mã đăng ký", "Mã nhóm", "Tên nhóm", "Mã đề tài", "Tên đề tài", "Trạng thái",
                        "Người đăng ký", "Thời gian đăng ký", "Người quyết định", "Lý do từ chối"), rows);
    }
}
