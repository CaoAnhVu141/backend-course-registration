package modules.departments.enums;

import lombok.Getter;

@Getter
public enum EnumDepartmentError {

    DEPARTMENT_NOT_FOUND("Khoa/phòng ban không tồn tại"),
    DEPARTMENT_NAME_EXISTS("Tên khoa/phòng ban đã tồn tại");

    private final String message;

    EnumDepartmentError(String message) {
        this.message = message;
    }
}