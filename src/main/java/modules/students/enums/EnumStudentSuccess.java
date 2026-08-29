package modules.students.enums;

import lombok.Getter;

@Getter
public enum EnumStudentSuccess {
    CREATED("Tạo sinh viên thành công"),
    UPDATED("Cập nhật sinh viên thành công"),
    DELETED("Xóa sinh viên thành công"),
    GET_ALL("Lấy danh sách sinh viên thành công"),
    GET_DETAIL("Lấy thông tin sinh viên thành công"),
    GET_PROFILE("Lấy thông tin cá nhân sinh viên thành công");

    private final String message;

    EnumStudentSuccess(String message) {
        this.message = message;
    }
}
