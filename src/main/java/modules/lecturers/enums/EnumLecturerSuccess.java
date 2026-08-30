package modules.lecturers.enums;

import lombok.Getter;

@Getter
public enum EnumLecturerSuccess {

    CREATED("Tạo giảng viên thành công"),
    UPDATED("Cập nhật giảng viên thành công"),
    DELETED("Xóa giảng viên thành công"),
    GET_ALL("Lấy danh sách giảng viên thành công"),
    GET_DETAIL("Lấy thông tin giảng viên thành công"),
    GET_PROFILE("Lấy thông tin cá nhân giảng viên thành công");

    private final String message;

    EnumLecturerSuccess(String message) {
        this.message = message;
    }
}
