package modules.registration_period.repository;

import modules.registration_period.entity.RegistrationPeriod;
import modules.registration_period.enums.RegistrationPeriodType;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RegistrationPeriodRepository extends JpaRepository<RegistrationPeriod, Long> {

    boolean existsByName(String name);

    boolean existsByType(RegistrationPeriodType type);
}
