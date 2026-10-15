package modules.notifications.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import modules.notifications.entity.Notification;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    // thông báo của 1 user, mới nhất trước
    List<Notification> findAllByUserIdOrderByCreatedAtDesc(Long userId);

    // đếm số lượng thông báo chưa có đọc
    long countByUserIdAndReadedFalse(Long userId);
}
