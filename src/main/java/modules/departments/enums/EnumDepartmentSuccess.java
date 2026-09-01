package modules.departments.enums;

import lombok.Getter;

@Getter
public enum EnumDepartmentSuccess {

    CREATED("Tạo khoa/phòng ban thành công"),
    UPDATED("Cập nhật khoa/phòng ban thành công"),
    DELETED("Xóa khoa/phòng ban thành công"),
    GET_ALL("Lấy danh sách khoa/phòng ban thành công"),
    GET_DETAIL("Lấy thông tin khoa/phòng ban thành công");

    private final String message;

    EnumDepartmentSuccess(String message) {
        this.message = message;
    }
}