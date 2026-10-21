package modules.student_group.enums;

import lombok.Getter;

@Getter
public enum EnumStudentGroupError {
    STUDENT_GROUP_NOT_FOUND("Nhóm sinh viên không tồn tại"),
    REGISTRATION_PERIOD_NOT_FOUND("Kỳ đăng ký không tồn tại"),
    STUDENT_NOT_FOUND("Sinh viên không tồn tại"),
    GROUP_MEMBERS_REQUIRED("Danh sách thành viên nhóm không được để trống"),
    STUDENT_ALREADY_IN_GROUP("Sinh viên đã thuộc nhóm trong kỳ đăng ký này"),
    GROUP_MEMBER_LIMIT_EXCEEDED("Mỗi nhóm tối đa 5 thành viên"),
    GROUP_HAS_REGISTRATIONS("Không thể xoá nhóm vì nhóm đã có đăng ký đề tài. Hãy huỷ đăng ký trước khi xoá.");

    private final String message;

    EnumStudentGroupError(String message) {
        this.message = message;
    }
}
