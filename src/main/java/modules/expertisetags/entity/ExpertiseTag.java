package modules.expertisetags.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "expertise_tags")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@AllArgsConstructor
public class ExpertiseTag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "expertise_tag_id")
    private Long id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "status", nullable = false)
    private boolean status;
}
