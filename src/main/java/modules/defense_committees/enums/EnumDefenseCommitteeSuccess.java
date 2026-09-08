package modules.defense_committees.enums;

import lombok.Getter;

@Getter
public enum EnumDefenseCommitteeSuccess {

    CREATED("Tạo hội đồng thành công"),
    MEMBER_ADDED("Thêm thành viên hội đồng thành công"),
    CLOSED("Đóng hội đồng thành công"),
    GET_ALL("Lấy danh sách hội đồng thành công"),
    GET_DETAIL("Lấy thông tin hội đồng thành công");

    private final String message;

    EnumDefenseCommitteeSuccess(String message) {
        this.message = message;
    }
}