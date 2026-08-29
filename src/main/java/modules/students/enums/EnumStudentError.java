package modules.students.enums;

import lombok.Getter;

@Getter
public enum EnumStudentError {

    STUDENT_NOT_FOUND("Sinh viên không tồn tại"),
    STUDENT_CODE_EXISTS("Mã sinh viên đã tồn tại"),
    DEPARTMENT_NOT_FOUND("Khoa/phòng ban không tồn tại");

    private final String message;

    EnumStudentError(String message) {
        this.message = message;
    }
}
