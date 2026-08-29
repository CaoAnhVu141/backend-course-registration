package modules.students.exception;

public class StudentException extends RuntimeException {

    public StudentException(EnumStudentError error) {
        super(error.getMessage());
    }
}
