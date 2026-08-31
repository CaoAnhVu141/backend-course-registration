package modules.student_group.exception;

import modules.student_group.enums.EnumStudentGroupError;

public class StudentGroupException extends RuntimeException {
    public StudentGroupException(EnumStudentGroupError error) {
        super(error.getMessage());
    }
}
