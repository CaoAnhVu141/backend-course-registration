package modules.lecturers.entity;

import jakarta.persistence.*;
import lombok.*;
import modules.expertisetags.entity.ExpertiseTag;

@Entity
@Table(name = "lecturer_expertise")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LecturerExpertise {

    @EmbeddedId
    private LecturerExpertiseId id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId("userId")
    @JoinColumn(name = "user_id", nullable = false)
    private Lecturer lecturer;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId("expertiseTagId")
    @JoinColumn(name = "expertise_tag_id", nullable = false)
    private ExpertiseTag expertiseTag;
}
