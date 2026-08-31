package modules.students.enums;

import lombok.Getter;

@Getter
public enum EnumStudentGroupError {
    STUDENT_GROUP_NOT_FOUND("Nhóm sinh viên không tồn tại"),
    REGISTRATION_PERIOD_NOT_FOUND("Kỳ đăng ký không tồn tại"),
    STUDENT_NOT_FOUND("Sinh viên không tồn tại"),
    STUDENT_ALREADY_IN_GROUP("Sinh viên đã thuộc nhóm trong kỳ đăng ký này");

    private final String message;

    EnumStudentGroupError(String message) {
        this.message = message;
    }