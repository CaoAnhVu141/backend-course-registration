package modules.defense_committees.enums;

import lombok.Getter;

@Getter
public enum EnumDefenseCommitteeError {

    DEFENSE_COMMITTEE_NOT_FOUND("Hội đồng không tồn tại"),
    DEFENSE_COMMITTEE_CODE_EXISTS("Mã hội đồng đã tồn tại"),
    REGISTRATION_PERIOD_NOT_FOUND("Kỳ đăng ký không tồn tại"),
    LECTURER_NOT_FOUND("Giảng viên không tồn tại"),
    USER_NOT_FOUND("Người dùng không tồn tại"),
    COMMITTEE_MEMBER_ALREADY_EXISTS("Giảng viên đã thuộc hội đồng này"),
    DEFENSE_COMMITTEE_CLOSED("Hội đồng đã đóng");

    private final String message;

    EnumDefenseCommitteeError(String message) {
        this.message = message;
    }
}