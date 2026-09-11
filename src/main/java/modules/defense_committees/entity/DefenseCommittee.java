package modules.defense_committees.entity;

import jakarta.persistence.*;
import lombok.*;
import modules.defense_committees.enums.DefenseCommitteeStatus;

@Entity
@Table(name = "defense_committees")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DefenseCommittee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "defense_committee_id")
    private Long id;

    @Column(name = "registration_period_id", nullable = false)
    private Long registrationPeriodId;

    @Column(name = "code", nullable = false, unique = true, length = 50)
    private String code;

    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 50)
    private DefenseCommitteeStatus status;

    @Column(name = "created_by", nullable = false, length = 255)
    private String createdBy;
}
