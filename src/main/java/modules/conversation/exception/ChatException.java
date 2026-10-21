package modules.conversation.exception;

import modules.conversation.enums.EnumChatError;

public class ChatException extends RuntimeException {

    public ChatException(EnumChatError error) {
        super(error.getMessage());
    }
}