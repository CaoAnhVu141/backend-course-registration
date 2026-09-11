package modules.defense_committees.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import modules.defense_committees.entity.CommitteeMember;

public interface CommitteeMemberRepository extends JpaRepository<CommitteeMember, Long> {

    boolean existsByLecturerIdAndDefenseCommitteeId(Long userId, Long defenseCommitteeId);

    List<CommitteeMember> findAllByDefenseCommitteeId(Long defenseCommitteeId, Long userId);

    List<CommitteeMember> findAllByLecturerId(Long lecturerId);

    void deleteAllByDefenseCommitteeId(Long defenseCommitteeId);

}
