package modules.students.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import modules.students.entity.StudentGroup;

public interface StudentGroupRepository extends JpaRepository<StudentGroup, Long> {

    List<StudentGroup> findAllByRegistrationPeriodId(Long registrationPeriodId);
}
