package modules.students.entity;

import jakarta.persistence.*;
import lombok.*;
import modules.auth.entity.User;

@Entity
@Table(name = "students")
@Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor
public class Student {
    @Id
    @Column(name = "user_id")
    private Long userId;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "student_code", nullable = false, unique = true, length = 50)
    private String studentCode;

    @Column(name = "department_id", nullable = false)
    private Long departmentId;

    @Column(name = "class_name", nullable = false, length = 100)
    private String className;
}
