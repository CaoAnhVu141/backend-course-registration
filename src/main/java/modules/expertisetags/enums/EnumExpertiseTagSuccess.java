package modules.expertisetags.enums;

import lombok.Getter;

@Getter
public enum EnumExpertiseTagSuccess {

    CREATED("Tạo chuyên môn thành công"),
    UPDATED("Cập nhật chuyên môn thành công"),
    DELETED("Xóa chuyên môn thành công"),
    GET_ALL("Lấy danh sách chuyên môn thành công"),
    GET_DETAIL("Lấy thông tin chuyên môn thành công");

    private final String message;

    EnumExpertiseTagSuccess(String message) {
        this.message = message;
    }
}