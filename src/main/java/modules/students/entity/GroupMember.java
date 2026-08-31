package modules.students.entity;

import jakarta.persistence.*;
import lombok.*;
import modules.students.entity.Student;
import modules.student_group.enums.GroupMemberRole;

import java.time.LocalDateTime;

@Entity
@Table(name = "group_members", uniqueConstraints = @UniqueConstraint(columnNames = { "student_group_id",
        "registration_period_id", "user_id" }))
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GroupMember {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "group_member_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "student_group_id", nullable = false)
    private StudentGroup studentGroup;

    @Column(name = "registration_period_id", nullable = false)
    private Long registrationPeriodId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private Student student;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false, length = 50)
    private GroupMemberRole role;

    @Column(name = "joined_at", nullable = false)
    private LocalDateTime joinedAt;
}
