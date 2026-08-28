package modules.departments.repository;

import modules.departments.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartmentRepository extends JpaRepository<Department, Long> {
    boolean existsByName(String name);

    Department findDepartmentById(Long id);
}
