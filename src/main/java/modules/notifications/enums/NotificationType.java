package modules.notifications.enums;

public enum NotificationType {
    ANNOUNCEMENT, // admin đăng tay
    TOPIC_PUBLISHED, // đề tài được công bố
    REGISTRATION_DECISION, // đăng ký đề tài được duyệt/từ chối
    FEEDBACK, // có góp ý báo cáo
    RESULT_PUBLISHED, // công bố điểm cuối
    SUBMISSION_SUBMITTED, // nộp báo cáo thành công
    DEFENSE_SCHEDULED // đưa ra hội đồng / có lịch bảo vệ
}
