package modules.registration_period.repository;

import modules.registration_period.entity.RegistrationPeriod;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RegistrationPeriodRepository extends JpaRepository<RegistrationPeriod, Long> {

    boolean existsByType(String type);
}
