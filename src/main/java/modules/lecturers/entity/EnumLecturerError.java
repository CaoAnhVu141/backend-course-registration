package modules.lecturers.entity;

import lombok.Getter;

@Getter
public class EnumLecturerError {

    LECTURER_NOT_FOUND("Giảng viên không tồn tại"),
    LECTURER_CODE_EXISTS("Mã giảng viên đã tồn tại"),
    DEPARTMENT_NOT_FOUND("Khoa/phòng ban không tồn tại");

    private final String message;

    EnumLecturerError(String message) {
        this.message = message;
    }
}
