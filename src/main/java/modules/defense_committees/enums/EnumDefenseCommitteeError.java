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
    DEFENSE_COMMITTEE_CLOSED("Hội đồng đã đóng"),
    COMMITTEE_FULL("Hội đồng đã đủ số lượng thành viên tối đa (5)"),
    CHAIRMAN_ALREADY_EXISTS("Hội đồng đã có chủ tịch"),
    SECRETARY_ALREADY_EXISTS("Hội đồng đã có thư ký"),
    INVALID_COMMITTEE_SIZE("Hội đồng phải có từ 3 đến 5 giảng viên"),
    COMMITTEE_NO_CHAIRMAN("Hội đồng phải có đúng một chủ tịch"),
    COMMITTEE_NO_SECRETARY("Hội đồng phải có đúng một thư ký");

    private final String message;

    EnumDefenseCommitteeError(String message) {
        this.message = message;
    }
}