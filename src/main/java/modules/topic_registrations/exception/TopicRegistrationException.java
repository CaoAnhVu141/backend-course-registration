package modules.topic_registrations.exception;

import modules.topic_registrations.enums.EnumTopicRegistrationError;

public class TopicRegistrationException extends RuntimeException {

    public TopicRegistrationException(EnumTopicRegistrationError error) {
        super(error.getMessage());
    }
}
