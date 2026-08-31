package modules.student_group.enums;

import lombok.Getter;

@Getter
public enum EnumStudentGroupSuccess {

    CREATED("Tạo nhóm sinh viên thành công"),
    DELETED("Xóa nhóm sinh viên thành công"),
    GET_ALL("Lấy danh sách nhóm sinh viên thành công"),
    GET_DETAIL("Lấy thông tin nhóm sinh viên thành công"),
    GET_MY_GROUP("Lấy nhóm sinh viên của tôi thành công");

    private final String message;

    EnumStudentGroupSuccess(String message) {
        this.message = message;
    }
}