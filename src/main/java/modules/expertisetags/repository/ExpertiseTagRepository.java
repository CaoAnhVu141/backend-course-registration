package modules.expertisetags.repository;

import modules.expertisetags.entity.ExpertiseTag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExpertiseTagRepository extends JpaRepository<ExpertiseTag, Long> {

    boolean existsByName(String name);

    ExpertiseTag findExpertiseTagById(Long id);
}
