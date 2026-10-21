package modules.conversation.enums;

import lombok.Getter;

@Getter
public enum EnumChatError {

    CONVERSATION_NOT_FOUND("Cuộc trò chuyện không tồn tại"),

    NOT_CONVERSATION_MEMBER("Bạn không thuộc cuộc trò chuyện này"),

    USER_NOT_FOUND("Người dùng không tồn tại"),

    MESSAGE_CONTENT_EMPTY("Nội dung tin nhắn không được để trống");

    private final String message;

    EnumChatError(String message) {
        this.message = message;
    }
}
