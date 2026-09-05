package global.exception;

import org.springframework.http.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import modules.auth.exception.InvalidCredentialsException;
import modules.auth.exception.InvalidTokenException;
import modules.departments.exceptions.DepartmentException;
import modules.expertisetags.exception.ExpertiseTagException;
import modules.registration_period.exception.RegistrationPeriodException;
import modules.student_group.exception.StudentGroupException;
import modules.students.exception.StudentException;
import modules.submissions.exception.SubmissionException;
import modules.topic_registrations.exception.TopicRegistrationException;
import modules.topics.exception.TopicException;
import modules.lecturers.exception.LecturerException;
import org.springframework.web.HttpRequestMethodNotSupportedException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleConflict(IllegalArgumentException exception) {
        return response(HttpStatus.CONFLICT, exception.getMessage(), null);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<Map<String, Object>> handleConfiguration(IllegalStateException exception) {
        return response(HttpStatus.UNPROCESSABLE_ENTITY, exception.getMessage(), null);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidation(MethodArgumentNotValidException exception) {
        Map<String, String> errors = new LinkedHashMap<>();
        exception.getBindingResult().getFieldErrors()
                .forEach(error -> errors.putIfAbsent(error.getField(), error.getDefaultMessage()));
        return response(HttpStatus.BAD_REQUEST, "Dữ liệu không hợp lệ", errors);
    }

    private ResponseEntity<Map<String, Object>> response(HttpStatus status, String message, Object details) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", status.value());
        body.put("message", message);
        if (details != null)
            body.put("details", details);
        return ResponseEntity.status(status).body(body);
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<Map<String, Object>> handleInvalidCredentials(InvalidCredentialsException exception) {
        return response(HttpStatus.UNAUTHORIZED, exception.getMessage(), null);
    }

    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<Map<String, Object>> handleInvalidToken(InvalidTokenException exception) {
        return response(HttpStatus.UNAUTHORIZED, exception.getMessage(), null);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<Map<String, Object>> handleMethodNotSupported(
            HttpRequestMethodNotSupportedException exception) {
        return response(HttpStatus.METHOD_NOT_ALLOWED, "Phương thức request không được hỗ trợ", null);
    }

    // DepartmentException handler
    @ExceptionHandler(DepartmentException.class)
    public ResponseEntity<Map<String, Object>> handleDepartmentException(DepartmentException exception) {
        return response(HttpStatus.BAD_REQUEST, exception.getMessage(), null);
    }

    // ExpertiseTagException handler
    // @ExceptionHandler(ExpertiseTagException.class)
    // public ResponseEntity<Map<String, Object>>
    // handleExpertiseTagException(ExpertiseTagException exception) {
    // return response(HttpStatus.BAD_REQUEST, exception.getMessage(), null);
    // }

    @ExceptionHandler(ExpertiseTagException.class)
    public ResponseEntity<Map<String, Object>> handleExpertiseTagException(ExpertiseTagException exception) {
        return response(HttpStatus.NOT_FOUND, exception.getMessage(), null);
    }

    // RegistrationPeriodException handler
    @ExceptionHandler(RegistrationPeriodException.class)
    public ResponseEntity<Map<String, Object>> handleRegistrationPeriodException(
            RegistrationPeriodException exception) {
        return response(HttpStatus.BAD_REQUEST, exception.getMessage(), null);
    }

    // StudentException handler
    @ExceptionHandler(StudentException.class)
    public ResponseEntity<Map<String, Object>> handleStudentException(StudentException exception) {
        return response(HttpStatus.BAD_REQUEST, exception.getMessage(), null);
    }

    // LecturerException handler
    @ExceptionHandler(LecturerException.class)
    public ResponseEntity<Map<String, Object>> handleLecturerException(LecturerException exception) {
        return response(HttpStatus.BAD_REQUEST, exception.getMessage(), null);
    }

    // TopicException handler
    @ExceptionHandler(TopicException.class)
    public ResponseEntity<Map<String, Object>> handleTopicException(TopicException exception) {
        return response(HttpStatus.BAD_REQUEST, exception.getMessage(), null);
    }

    // Student Group Exception handler
    @ExceptionHandler(StudentGroupException.class)
    public ResponseEntity<Map<String, Object>> handleStudentGroupException(StudentGroupException exception) {
        return response(HttpStatus.CONFLICT, exception.getMessage(), null);
    }

    // Topic Registration Exception handler
    @ExceptionHandler(TopicRegistrationException.class)
    public ResponseEntity<Map<String, Object>> handleTopicRegistrationException(
            TopicRegistrationException exception) {
        return response(HttpStatus.CONFLICT, exception.getMessage(), null);
    }

    // Submission Exception handler
    @ExceptionHandler(SubmissionException.class)
    public ResponseEntity<Map<String, Object>> handleSubmissionException(SubmissionException exception) {
        return response(HttpStatus.CONFLICT, exception.getMessage(), null);
    }
}
