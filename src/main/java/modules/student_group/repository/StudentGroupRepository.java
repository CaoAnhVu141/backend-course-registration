package modules.student_group.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import modules.student_group.entity.StudentGroup;

public interface StudentGroupRepository extends JpaRepository<StudentGroup, Long> {

    List<StudentGroup> findAllByRegistrationPeriodId(Long registrationPeriodId);
}
