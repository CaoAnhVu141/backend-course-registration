package modules.defense_committees.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import modules.defense_committees.entity.CommitteeMember;

public interface CommitteeMemberRepository extends JpaRepository<CommitteeMember, Long> {

    // kiểm tra xem giảng viên đã là thành viên của hội đồng bảo vệ nào chưa
    boolean existsByLecturerUserIdAndDefenseCommitteeId(Long userId, Long defenseCommitteeId);

    // danh sách trong hội đồng
    List<CommitteeMember> findAllByDefenseCommitteeId(Long defenseCommitteeId);

    // lấy theo giảng viên
    List<CommitteeMember> findAllByLecturerUserId(Long lecturerId);

    // xoá toàn bộ thành viên trong hội đồng
    void deleteAllByDefenseCommitteeId(Long defenseCommitteeId);

}
