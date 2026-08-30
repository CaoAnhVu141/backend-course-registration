package modules.lecturers.exception;

import modules.lecturers.enums.EnumLecturerError;

public class LecturerException extends RuntimeException {

    public LecturerException(EnumLecturerError error) {
        super(error.getMessage());
    }
}