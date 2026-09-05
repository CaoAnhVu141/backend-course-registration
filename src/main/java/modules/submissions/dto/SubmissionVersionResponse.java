package modules.submissions.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class SubmissionVersionResponse {

    private Long id;
    private Long submissionId;
    private String fileName;
    private String storageKey;
    private String mimeType;
    private Long fileSize;
    private String changeNote;
    private String uploadedBy;
}
