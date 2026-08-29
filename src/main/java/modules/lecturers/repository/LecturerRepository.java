package modules.lecturers.repository;

import modules.lecturers.entity.Lecturer;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface LecturerRepository extends JpaRepository<Lecturer, Long> {
    boolean existsByLecturerCode(String lecturerCode);

    Optional<Lecturer> findByUserUsername(String username);
}
