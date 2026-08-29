package modules.registration_period.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "registration_periods")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegistrationPeriod {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "registration_period_id")
    private Long id;

    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @Column(name = "type", nullable = false, length = 100)
    private String type;

    @Column(name = "lecturer_registration_start")
    private LocalDateTime lecturerRegistrationStart;

    @Column(name = "lecturer_registration_end")
    private LocalDateTime lecturerRegistrationEnd;

    @Column(name = "student_registration_start")
    private LocalDateTime studentRegistrationStart;

    @Column(name = "student_registration_end")
    private LocalDateTime studentRegistrationEnd;

    @Column(name = "reviewer_score_deadline", nullable = true)
    private LocalDateTime reviewerScoreDeadline;

    @Column(name = "defense_date")
    private LocalDateTime defenseDate;

    @Column(name = "status")
    private boolean status = true;

}