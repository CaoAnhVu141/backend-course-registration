package modules.defense_committees.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import modules.defense_committees.entity.DefenseCommittee;

public interface DefenseCommitteeRepository extends JpaRepository<DefenseCommittee, Long> {

    boolean existsByCode(String code);

    List<DefenseCommittee> findAllByRegistrationPeriodId(Long registrationPeriodId);

}
