package modules.lecturers.entity;

import jakarta.persistence.*;
import lombok.*;
import modules.auth.entity.User;

@Entity
@Table(name = "lecturers")
@Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor
public class Lecturer {
    @Id
    @Column(name = "user_id")
    private Long userId;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "lecturer_code", nullable = false, unique = true, length = 50)
    private String lecturerCode;

    @Column(name = "department_id", nullable = false)
    private Long departmentId;

    @Column(name = "academic_title", length = 100)
    private String academicTitle;

    @Column(name = "max_review_load", nullable = false)
    private Integer maxReviewLoad;
}
