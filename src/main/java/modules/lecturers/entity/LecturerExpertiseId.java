package modules.lecturers.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Embeddable
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class LecturerExpertiseId implements Serializable {

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "expertise_tag_id")
    private Long expertiseTagId;

}
