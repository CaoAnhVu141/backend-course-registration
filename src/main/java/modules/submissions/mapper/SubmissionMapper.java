package modules.submissions.mapper;

import modules.submissions.dto.SubmissionResponse;
import modules.submissions.dto.SubmissionVersionResponse;
import modules.submissions.entity.Submission;
import modules.submissions.entity.SubmissionVersion;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SubmissionMapper {

    public SubmissionResponse toResponse(Submission submission, List<SubmissionVersion> submissionVersions) {
        return SubmissionResponse.builder()
                .id(submission.getId())
                .topicRegistrationId(submission.getTopicRegistration().getId())
                .status(submission.isStatus())
                .submittedAt(submission.getSubmittedAt())
                .submissionVersions(submissionVersions.stream().map(this::toSubmissionVersionResponse).toList())
                .build();
    }

    // function helper
    public SubmissionVersionResponse toSubmissionVersionResponse(SubmissionVersion submissionVersion) {
        return SubmissionVersionResponse.builder()
                .id(submissionVersion.getId())
                .submissionId(submissionVersion.getSubmission().getId())
                .fileName(submissionVersion.getFileName())
                .storageKey(submissionVersion.getStorageKey())
                .mimeType(submissionVersion.getMimeType())
                .fileSize(submissionVersion.getFileSize())
                .changeNote(submissionVersion.getChangeNote())
                .uploadedBy(submissionVersion.getUploadedBy())
                .build();
    }
}
