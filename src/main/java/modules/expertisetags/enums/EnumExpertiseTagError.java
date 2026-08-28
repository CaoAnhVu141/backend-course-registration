package modules.expertisetags.enums;

import lombok.Getter;

@Getter
public enum EnumExpertiseTagError {

    EXPERTISE_TAG_NOT_FOUND("Chuyên môn không tồn tại"),
    EXPERTISE_TAG_NAME_EXISTS("Tên chuyên môn đã tồn tại");

    private final String message;

    EnumExpertiseTagError(String message) {
        this.message = message;
    }
}