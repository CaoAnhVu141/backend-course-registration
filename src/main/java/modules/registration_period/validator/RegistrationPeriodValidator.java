package modules.registration_period.validator;

import modules.registration_period.dto.CreateRegistrationPeriodRequest;
import modules.registration_period.dto.UpdateRegistrationPeriodRequest;
import modules.registration_period.enums.EnumRegistrationPeriodError;
import modules.registration_period.exception.RegistrationPeriodException;
import org.springframework.stereotype.Component;
import modules.registration_period.enums.RegistrationPeriodType;

import java.time.LocalDateTime;

@Component
public class RegistrationPeriodValidator {

    public void validateForCreate(CreateRegistrationPeriodRequest createRegistrationPeriodRequest) {
        validateTimeRange(
                createRegistrationPeriodRequest.getType(),
                createRegistrationPeriodRequest.getLecturerRegistrationStart(),
                createRegistrationPeriodRequest.getLecturerRegistrationEnd(),
                createRegistrationPeriodRequest.getStudentRegistrationStart(),
                createRegistrationPeriodRequest.getStudentRegistrationEnd(),
                createRegistrationPeriodRequest.getReviewerScoreDeadline(),
                createRegistrationPeriodRequest.getDefenseDate());
    }

    public void validateForUpdate(UpdateRegistrationPeriodRequest updateRegistrationPeriodRequest) {
        validateTimeRange(
                updateRegistrationPeriodRequest.getType(),
                updateRegistrationPeriodRequest.getLecturerRegistrationStart(),
                updateRegistrationPeriodRequest.getLecturerRegistrationEnd(),
                updateRegistrationPeriodRequest.getStudentRegistrationStart(),
                updateRegistrationPeriodRequest.getStudentRegistrationEnd(),
                updateRegistrationPeriodRequest.getReviewerScoreDeadline(),
                updateRegistrationPeriodRequest.getDefenseDate());
    }

    private void validateTimeRange(
            RegistrationPeriodType type,
            LocalDateTime lecturerStart,
            LocalDateTime lecturerEnd,
            LocalDateTime studentStart,
            LocalDateTime studentEnd,
            LocalDateTime reviewerScoreDeadline,
            LocalDateTime defenseDate) {
        if (!lecturerEnd.isAfter(lecturerStart)) {
            throw new RegistrationPeriodException(EnumRegistrationPeriodError.INVALID_LECTURER_REGISTRATION_TIME);
        }

        if (!studentEnd.isAfter(studentStart)) {
            throw new RegistrationPeriodException(EnumRegistrationPeriodError.INVALID_STUDENT_REGISTRATION_TIME);
        }

        if (studentStart.isBefore(lecturerStart)) {
            throw new RegistrationPeriodException(EnumRegistrationPeriodError.INVALID_STUDENT_REGISTRATION_TIME);
        }

        if (reviewerScoreDeadline.isBefore(studentEnd)) {
            throw new RegistrationPeriodException(EnumRegistrationPeriodError.INVALID_REVIEWER_SCORE_DEADLINE);
        }

        // deadline chấm phản biện TLCN/KLTN mới có
        boolean needReviewerDeadline = type == RegistrationPeriodType.TLCN || type == RegistrationPeriodType.KLTN;
        if (needReviewerDeadline) {
            if (reviewerScoreDeadline == null) {
                throw new RegistrationPeriodException(EnumRegistrationPeriodError.REVIEWER_SCORE_DEADLINE_REQUIRED);
            }
            if (reviewerScoreDeadline.isBefore(studentEnd)) {
                throw new RegistrationPeriodException(EnumRegistrationPeriodError.INVALID_REVIEWER_SCORE_DEADLINE);
            }
        } else if (reviewerScoreDeadline != null) {
            throw new RegistrationPeriodException(EnumRegistrationPeriodError.REVIEWER_SCORE_DEADLINE_NOT_ALLOWED);
        }

        // ngày báo cáo hội đồng: chỉ KLTN mới có
        if (type == RegistrationPeriodType.KLTN) {
            if (defenseDate == null) {
                throw new RegistrationPeriodException(EnumRegistrationPeriodError.DEFENSE_DATE_REQUIRED);
            }
            if (defenseDate.isBefore(reviewerScoreDeadline)) {
                throw new RegistrationPeriodException(EnumRegistrationPeriodError.INVALID_DEFENSE_DATE);
            }
        } else if (defenseDate != null) {
            throw new RegistrationPeriodException(EnumRegistrationPeriodError.DEFENSE_DATE_NOT_ALLOWED);
        }
    }
}
