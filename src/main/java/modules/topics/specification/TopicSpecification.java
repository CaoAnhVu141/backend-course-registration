package modules.topics.specification;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.Predicate;
import modules.topics.entity.Topic;
import modules.topics.enums.TopicStatus;

public class TopicSpecification {

    public static Specification<Topic> build(String keyword, Long departmentId,
            Long registrationPeriodId, TopicStatus status) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (keyword != null && !keyword.isBlank()) {
                String like = "%" + keyword.trim().toLowerCase() + "%";
                predicates.add(cb.or(
                        cb.like(cb.lower(root.get("title")), like),
                        cb.like(cb.lower(root.get("requirements")), like)));
            }
            if (departmentId != null) {
                predicates.add(cb.equal(root.get("departmentId"), departmentId));
            }
            if (registrationPeriodId != null) {
                predicates.add(cb.equal(root.get("registrationPeriodId"), registrationPeriodId));
            }
            if (status != null) {
                predicates.add(cb.equal(root.get("status"), status));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
