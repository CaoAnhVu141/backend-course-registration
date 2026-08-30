package modules.topics.exception;

import modules.topics.enums.EnumTopicError;

public class TopicException extends RuntimeException {

    public TopicException(EnumTopicError error) {
        super(error.getMessage());
    }
}