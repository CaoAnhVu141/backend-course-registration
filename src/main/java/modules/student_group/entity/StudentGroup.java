package modules.student_group.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "student_group")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "student_group_id")
    private Long id;

    @Column(name = "registration_period_id", nullable = false)
    private Long registrationPeriodId;

    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @Column(name = "status", nullable = false)
    private boolean status;

    @Column(name = "create_by", nullable = false, length = 255)
    private String createBy;
}
