package modules.topics.entity;

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
public class TopicSupervisorId implements Serializable {

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "topic_id")
    private Long topicId;
}