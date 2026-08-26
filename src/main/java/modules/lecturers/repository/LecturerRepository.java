package modules.lecturers.repository;

import modules.lecturers.entity.Lecturer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LecturerRepository extends JpaRepository<Lecturer, Long> {
    boolean existsByLecturerCode(String lecturerCode);
}
