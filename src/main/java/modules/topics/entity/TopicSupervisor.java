package modules.topics.entity;

import jakarta.persistence.*;
import lombok.*;
import modules.lecturers.entity.Lecturer;

@Entity
@Table(name = "topic_supervisors")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TopicSupervisor {

    @EmbeddedId
    private TopicSupervisorId id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId("userId")
    @JoinColumn(name = "user_id", nullable = false)
    private Lecturer lecturer;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId("topicId")
    @JoinColumn(name = "topic_id", nullable = false)
    private Topic topic;
}