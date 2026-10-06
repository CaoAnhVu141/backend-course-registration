package modules.student_group.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import modules.student_group.entity.GroupMember;
import modules.student_group.enums.GroupMemberRole;

public interface GroupMemberRepository extends JpaRepository<GroupMember, Long> {

    // kiểm tra sinh viên đã tham gia nhóm nào trong kỳ đăng ký hay chưa
    boolean existsByRegistrationPeriodIdAndStudentUserId(Long registrationPeriodId, Long studentUserId);

    List<GroupMember> findAllByStudentGroupId(Long studentGroupId);

    List<GroupMember> findAllByStudentUserId(Long studentUserId);

    void deleteAllByStudentGroupId(Long studentGroupId);

    // kiểm tra người gửi báo cáo có phải là nhóm trưởng hay không
    boolean existsByStudentGroupIdAndStudentUserIdAndRole(Long studentGroup, Long studentUserId, GroupMemberRole role);
}
