package modules.registration_period.repository;

import modules.registrationperiods.entity.RegistrationPeriod;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RegistrationPeriodRepository extends JpaRepository<RegistrationPeriod, Long> {

    boolean existsByType(String type);
}
