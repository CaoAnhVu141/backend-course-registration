package modules.committee_topic_assignments.export;

import java.util.Arrays;
import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import modules.committee_topic_assignments.dto.CommitteeTopicAssignmentResponse;
import modules.students.export.ExportExcelService;

@Service
@RequiredArgsConstructor
public class CommitteeTopicAssignmentExportExcelService {
    private final ExportExcelService excelService;

    public byte[] export(List<CommitteeTopicAssignmentResponse> assignments) {
        List<? extends List<?>> rows = assignments.stream()
                .map(assignment -> (List<?>) Arrays.asList(assignment.getId(), assignment.getDefenseCommitteeId(),
                        assignment.getDefenseCommitteeCode(), assignment.getDefenseCommitteeName(),
                        assignment.getRegistrationPeriodId(), assignment.getTopicRegistrationId(),
                        assignment.getTopicId(), assignment.getTopicTitle(), assignment.getStudentGroupId(),
                        assignment.getStudentGroupName(), assignment.getScheduledStart(), assignment.getScheduledEnd(),
                        assignment.getRoom(), assignment.getStatus(), assignment.getAssignedBy(),
                        assignment.getAssignedAt()))
                .toList();
        return excelService.export("Phân công đề tài", "DANH SÁCH PHÂN CÔNG ĐỀ TÀI",
                Arrays.asList("Mã phân công", "Mã hội đồng", "Mã hội đồng bảo vệ", "Tên hội đồng",
                        "Mã kỳ đăng ký", "Mã đăng ký đề tài", "Mã đề tài", "Tên đề tài", "Mã nhóm", "Tên nhóm",
                        "Bắt đầu bảo vệ", "Kết thúc bảo vệ", "Phòng", "Trạng thái", "Người phân công",
                        "Thời gian phân công"), rows);
    }
}
