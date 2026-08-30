package modules.lecturers.repository;

import modules.lecturers.entity.LecturerExpertise;
import modules.lecturers.entity.LecturerExpertiseId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LecturerExpertiseRepository extends JpaRepository<LecturerExpertise, LecturerExpertiseId> {

    List<LecturerExpertise> findAllByLecturerUserId(Long userId);

    void deleteAllByLecturerUserId(Long userId);
}