package modules.students.exception;

import modules.students.enums.EnumStudentError;

public class StudentException extends RuntimeException {

    public StudentException(EnumStudentError error) {
        super(error.getMessage());
    }
}
