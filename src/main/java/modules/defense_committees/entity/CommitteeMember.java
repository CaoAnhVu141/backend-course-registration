package modules.defense_committees.entity;

import java.time.LocalDateTime;
import jakarta.persistence.*;
import lombok.*;
import modules.defense_committees.enums.CommitteeRole;
import modules.lecturers.entity.Lecturer;

@Entity
@Table(name = "committee_members", uniqueConstraints = @UniqueConstraint(columnNames = { "user_id",
        "defense_committee_id" }))
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommitteeMember {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "committee_member_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private Lecturer lecturer;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "defense_committee_id", nullable = false)
    private DefenseCommittee defenseCommittee;

    @Enumerated(EnumType.STRING)
    @Column(name = "committee_role", nullable = false, length = 50)
    private CommitteeRole committeeRole;

    @Column(name = "joined_at", nullable = false)
    private LocalDateTime joinedAt;
}
