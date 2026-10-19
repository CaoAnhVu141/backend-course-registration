package modules.student_group.export;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import modules.students.export.ExportExcelService;
import modules.student_group.dto.GroupMemberResponse;
import modules.student_group.dto.StudentGroupResponse;

@Service
@RequiredArgsConstructor
public class StudentGroupExportExcelService {
    private final ExportExcelService excelService;

    public byte[] export(List<StudentGroupResponse> groups, Long registrationPeriodId) {
        List<List<?>> rows = new ArrayList<>();
        int serial = 1;
        for (StudentGroupResponse group : groups) {
            List<GroupMemberResponse> members = group.getMembers();
            if (members == null || members.isEmpty()) {
                rows.add(toRow(serial++, group, null));
                continue;
            }
            for (GroupMemberResponse member : members) {
                rows.add(toRow(serial++, group, member));
            }
        }

        String title = registrationPeriodId == null
                ? "DANH SÁCH NHÓM SINH VIÊN"
                : "DANH SÁCH NHÓM SINH VIÊN - KỲ " + registrationPeriodId;
        return excelService.export("Nhóm sinh viên", title,
                Arrays.asList("STT", "Mã nhóm", "Tên nhóm", "Mã kỳ đăng ký", "Trạng thái",
                        "Người tạo", "Mã sinh viên", "Tên đăng nhập", "Vai trò", "Ngày tham gia"),
                rows);
    }

    private List<?> toRow(int serial, StudentGroupResponse group, GroupMemberResponse member) {
        return Arrays.asList(serial, group.getId(), group.getName(), group.getRegistrationPeriodId(),
                group.isStatus() ? "Đang hoạt động" : "Không hoạt động", group.getCreatedBy(),
                member == null ? null : member.getStudentCode(), member == null ? null : member.getUsername(),
                member == null ? null : member.getRole(), member == null ? null : member.getJoinedAt());
    }
}
