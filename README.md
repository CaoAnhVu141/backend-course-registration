# Backend Course Registration

Backend cho hệ thống đăng ký môn học (Course Registration), xây dựng bằng **Spring Boot 3.2** và **Java 21**.

## 🚀 Công nghệ sử dụng

| Công nghệ | Phiên bản |
|-----------|-----------|
| Java | 21 |
| Spring Boot | 3.2.0 |
| Build tool | Maven |
| Database | PostgreSQL |
| ORM | Spring Data JPA (Hibernate) |
| Security | Spring Security + JWT (jjwt 0.12.6) |
| Lombok | 1.18.34 |
| Validation | Jakarta Validation |

## 📁 Cấu trúc dự án

Dự án áp dụng **kiến trúc module (Modular Monolith)**. Mỗi module nghiệp vụ được tách riêng, bên trong chia theo các tầng: `controller`, `dto`, `entity`, `enums`, `exception`, `mapper`, `repository`, `service`.

```
src/main/java/
├── com/example/backend_course_resgistration/
│   └── BackendCourseResgistrationApplication.java   # Class khởi động chính
├── global/
│   ├── exception/
│   │   └── GlobalExceptionHandler.java              # Xử lý exception tập trung
│   ├── response/
│   │   └── ApiResponse.java                         # Format response chuẩn
│   └── security/
│       ├── JwtAuthenticationFilter.java             # Filter xác thực JWT
│       ├── JwtService.java                          # Tạo/kiểm tra JWT
│       └── SecurityConfig.java                      # Cấu hình Spring Security
└── modules/
    ├── auth/                                        # Module xác thực & phân quyền
    ├── departments/                                 # Module quản lý khoa/phòng ban
    ├── expertisetags/                               # Module quản lý chuyên môn
    ├── lecturers/                                   # Module quản lý giảng viên
    ├── registration_period/                         # Module quản lý kỳ đăng ký
    ├── student_group/                               # Module quản lý nhóm sinh viên
    ├── students/                                    # Module quản lý sinh viên
    ├── topics/                                      # Module quản lý đề tài
    ├── topic_registrations/                         # Module quản lý đăng ký đề tài
    └── submissions/                                 # Module quản lý bài nộp
```

### Giải thích các tầng trong module

| Tầng | Vai trò |
|------|---------|
| `controller` | Xử lý HTTP request/response, chứa các `@RestController` |
| `dto` | Data Transfer Object (request/response) |
| `entity` | JPA entities (bảng database) |
| `enums` | Các enum lỗi/thành công |
| `exception` | Custom exception của module |
| `mapper` | Chuyển đổi entity ↔ DTO |
| `repository` | Spring Data JPA repositories |
| `service` | Business logic |

### Chi tiết từng module

#### `modules/auth` — Xác thực & phân quyền
```
auth/
├── controller/AuthController.java
├── dto/LoginRequest.java, LoginResponse.java, RegisterRequest.java, RegisterResponse.java, RefreshTokenRequest.java
├── entity/User.java, Role.java, RefreshToken.java
├── enums/EnumAuthError.java
├── exception/InvalidCredentialsException.java, InvalidTokenException.java
├── mapper/AuthMapper.java
├── repository/UserRepository.java, RoleRepository.java, RefreshTokenRepository.java
├── security/CustomUserDetailsService.java
└── service/AuthService.java, UserAccountService.java
```

#### `modules/departments` — Quản lý khoa/phòng ban ✅
```
departments/
├── controller/DepartmentController.java
├── dto/CreateDepartmentRequest.java, UpdateDepartmentRequest.java, DepartmentResponse.java
├── entity/Department.java
├── enums/EnumDepartmentError.java
├── exceptions/DepartmentException.java
├── mapper/DepartmentMapper.java
├── repository/DepartmentRepository.java
└── service/DepartmentService.java
```

#### `modules/expertisetags` — Quản lý chuyên môn ✅
```
expertisetags/
├── controller/ExpertiseTagController.java
├── dto/CreateExpertiseTagRequest.java, UpdateExpertiseTagRequest.java, ExpertiseTagResponse.java
├── entity/ExpertiseTag.java
├── enums/EnumExpertiseTagError.java, EnumExpertiseTagSuccess.java
├── exception/ExpertiseTagException.java
├── mapper/ExpertiseTagMapper.java
├── repository/ExpertiseTagRepository.java
└── service/ExpertiseTagService.java
```

#### `modules/lecturers` — Quản lý giảng viên ✅ (đã hoàn thiện)
```
lecturers/
├── controller/LecturerController.java
├── dto/RegisterLecturerRequest.java, UpdateLecturerRequest.java, LecturerResponse.java
├── entity/Lecturer.java
├── enums/EnumLecturerError.java, EnumLecturerSuccess.java
├── exception/LecturerException.java
├── mapper/LecturerMapper.java
├── repository/LecturerRepository.java
└── service/LecturerService.java
```
> ✅ **Trạng thái**: Module này **đã hoàn thiện**. Hỗ trợ đầy đủ: đăng ký, danh sách, chi tiết, hồ sơ của tôi, cập nhật, xóa. Giảng viên có danh sách chuyên môn (`expertiseTagIds`) lưu ở bảng phụ `lecturer_expertise`.

#### `modules/registration_period` — Quản lý kỳ đăng ký
```
registration_period/
├── controller/RegistrationPeriodController.java
├── dto/CreateRegistrationPeriodRequest.java, UpdateRegistrationPeriodRequest.java, RegistrationPeriodResponse.java
├── entity/RegistrationPeriod.java
├── enums/EnumRegistrationPeriodError.java, EnumRegistrationPeriodSuccess.java
├── exception/RegistrationPeriodException.java
├── mapper/RegistrationPeriodMapper.java
├── repository/RegistrationPeriodRepository.java
├── service/RegistrationPeriodService.java
└── validator/RegistrationPeriodValidator.java
```

#### `modules/student_group` — Quản lý nhóm sinh viên ✅ (đã hoàn thiện)
```
student_group/
├── controller/StudentGroupController.java
├── dto/CreateStudentGroupRequest.java, StudentGroupResponse.java, GroupMemberResponse.java
├── entity/StudentGroup.java, GroupMember.java
├── enums/EnumStudentGroupError.java, EnumStudentGroupSuccess.java, GroupMemberRole.java
├── exception/StudentGroupException.java
├── mapper/StudentGroupMapper.java
├── repository/StudentGroupRepository.java, GroupMemberRepository.java
└── service/StudentGroupService.java
```
> ✅ **Trạng thái**: Module này **đã hoàn thiện**. Hỗ trợ đầy đủ: tạo nhóm (STUDENT), danh sách nhóm, danh sách nhóm theo kỳ đăng ký, chi tiết nhóm (ADMIN, LECTURER), xóa nhóm (ADMIN). Mỗi nhóm có một trưởng nhóm (`LEADER`) và các thành viên (`MEMBER`). Người tạo nhóm tự động trở thành `LEADER`.

#### `modules/students` — Quản lý sinh viên ✅
```
students/
├── controller/StudentController.java
├── dto/RegisterStudentRequest.java, UpdateStudentRequest.java, StudentResponse.java
├── entity/Student.java
├── enums/EnumStudentError.java, EnumStudentSuccess.java
├── exception/StudentException.java
├── mapper/StudentMapper.java
├── repository/StudentRepository.java
└── service/StudentService.java
```

#### `modules/topics` — Quản lý đề tài ✅ (đã hoàn thiện)
```
topics/
├── controller/TopicController.java
├── dto/CreateTopicRequest.java, UpdateTopicRequest.java, RejectTopicRequest.java, TopicResponse.java
├── entity/Topic.java, TopicSupervisor.java, TopicSupervisorId.java
├── enums/EnumTopicError.java, EnumTopicSuccess.java, TopicStatus.java
├── exception/TopicException.java
├── mapper/TopicMapper.java
├── repository/TopicRepository.java, TopicSupervisorRepository.java
├── security/TopicSecurity.java
└── service/TopicService.java
```
> ✅ **Trạng thái**: Module này **đã hoàn thiện**. Hỗ trợ đầy đủ: tạo đề tài (LECTURER), xem danh sách, xem đề tài đã công bố (STUDENT), xem đề tài của tôi (LECTURER), cập nhật, duyệt/từ chối/công bố (ADMIN), xóa (ADMIN). Lý do từ chối (`rejectReason`) được lưu vào database. Trạng thái đề tài gồm: `PENDING`, `APPROVED`, `REJECTED`, `PUBLISHED`, `CLOSED`.

#### `modules/topic_registrations` — Quản lý đăng ký đề tài ✅ (đã hoàn thiện)
```
topic_registrations/
├── controller/TopicRegistrationController.java
├── dto/CreateTopicRegistrationRequest.java, RejectTopicRegistrationRequest.java, TopicRegistrationResponse.java
├── entity/TopicRegistration.java
├── enums/EnumTopicRegistrationError.java, EnumTopicRegistrationSuccess.java, TopicRegistrationStatus.java
├── exception/TopicRegistrationException.java
├── mapper/TopicRegistrationMapper.java
├── repository/TopicRegistrationRepository.java
├── security/TopicRegistrationSecurity.java
└── service/TopicRegistrationService.java
```
> ✅ **Trạng thái**: Module này **đã hoàn thiện**. Hỗ trợ đầy đủ: đăng ký đề tài (STUDENT), danh sách đăng ký, chi tiết đăng ký, danh sách theo nhóm sinh viên, danh sách theo đề tài, duyệt/từ chối (ADMIN, LECTURER), hủy đăng ký (STUDENT). Một nhóm sinh viên đăng ký một đề tài đã được công bố. Trạng thái đăng ký gồm: `PENDING`, `APPROVED`, `REJECTED`, `CANCELLED`. Lý do từ chối (`rejectedReason`) và người quyết định (`decidedBy`) được lưu vào database.

#### `modules/submissions` — Quản lý bài nộp ✅ (đã hoàn thiện)
```
submissions/
├── controller/SubmissionController.java
├── dto/CreateSubmissionRequest.java, CreateSubmissionVersionRequest.java, SubmissionResponse.java, SubmissionVersionResponse.java, CreateSubmissionFeedbackRequest.java, SubmissionFeedbackResponse.java
├── entity/Submission.java, SubmissionVersion.java, SubmissionFeedback.java
├── enums/EnumSubmissionError.java, EnumSubmissionSuccess.java
├── exception/SubmissionException.java
├── mapper/SubmissionMapper.java
├── repository/SubmissionRepository.java, SubmissionVersionRepository.java, SubmissionFeedbackRepository.java
└── service/SubmissionService.java
```
> ✅ **Trạng thái**: Module này **đã hoàn thiện**. Hỗ trợ: tạo bài nộp (STUDENT), danh sách bài nộp (ADMIN, LECTURER), chi tiết bài nộp, bài nộp theo đăng ký đề tài, và **góp ý bài nộp** (LECTURER tạo góp ý, xem góp ý theo phiên bản / theo giảng viên). Một đăng ký đề tài đã được duyệt (`APPROVED`) chỉ có **một** bài nộp duy nhất. Mỗi bài nộp có thể có nhiều phiên bản (`SubmissionVersion`) lưu file báo cáo, và mỗi phiên bản có thể nhận nhiều góp ý (`SubmissionFeedback`) từ giảng viên.

## 🛠️ Cách chạy dự án

### Yêu cầu
- **JDK 21**
- **Maven 3.8+**
- **PostgreSQL** (đang chạy)

### 1. Cấu hình môi trường


| Biến | Mô tả |
|------|-------|
| `DB_HOST` | Host PostgreSQL  |
| `DB_PORT` | Port PostgreSQL  |
| `DB_DATABASE` | Tên database |
| `DB_USERNAME` | Username PostgreSQL |
| `DB_PASSWORD` | Password PostgreSQL |
| `JWT_SECRET` | Khóa bí mật JWT (tối thiểu 32 ký tự) |
| `JWT_ACCESS_TOKEN_EXPIRATION` | Thời gian hết hạn access token (ms) |
| `JWT_REFRESH_TOKEN_EXPIRATION` | Thời gian hết hạn refresh token (ms) |


### 2. Cài đặt thư viện

```bash
mvn clean install
```

### 3. Chạy ứng dụng

```bash
mvn spring-boot:run
```

Hoặc build JAR rồi chạy:

```bash
mvn clean package
java -jar target/backend_course_resgistration-0.0.1-SNAPSHOT.jar
```

Ứng dụng khởi động tại: `http://localhost:8080`

### 4. Chỉ compile (kiểm tra lỗi)

```bash
mvn clean compile
```

## 🔗 API

### Xác thực (Auth)

| Method | Endpoint | Mô tả | Quyền |
|--------|----------|-------|-------|
| POST | `/api/auth/register/student` | Đăng ký sinh viên | Public |
| POST | `/api/auth/register/lecturer` | Đăng ký giảng viên | Public |
| POST | `/api/auth/register/admin` | Đăng ký admin | Public |
| POST | `/api/auth/login` | Đăng nhập | Public |
| POST | `/api/auth/refresh-token` | Làm mới token | Public |

**Ví dụ đăng nhập:**
```json
POST /api/auth/login
{
  "username": "admin",
  "password": "123456"
}
```

**Response:**
```json
{
  "accessToken": "eyJhbGciOi...",
  "refreshToken": "eyJhbGciOi...",
  "tokenType": "Bearer"
}
```

> Sau khi đăng nhập, thêm header `Authorization: Bearer <accessToken>` cho các request cần quyền.

### Sinh viên (Students)

| Method | Endpoint | Mô tả | Quyền |
|--------|----------|-------|-------|
| POST | `/api/students/register` | Đăng ký sinh viên | Public |
| GET | `/api/students` | Lấy danh sách sinh viên | ADMIN, LECTURER |
| GET | `/api/students/{userId}` | Lấy sinh viên theo id | ADMIN, LECTURER |
| GET | `/api/students/me` | Lấy thông tin cá nhân | STUDENT |
| PUT | `/api/students/{userId}` | Cập nhật sinh viên | ADMIN |
| DELETE | `/api/students/{userId}` | Xóa sinh viên (soft delete) | ADMIN |

**Ví dụ đăng ký sinh viên:**
```json
POST /api/students/register
{
  "username": "student1",
  "email": "student1@example.com",
  "password": "123456",
  "studentCode": "SV001",
  "departmentId": 1,
  "className": "D21CQCN01"
}
```

**Ví dụ cập nhật sinh viên:**
```json
PUT /api/students/1
{
  "studentCode": "SV001",
  "departmentId": 1,
  "className": "D21CQCN02"
}
```

### Giảng viên (Lecturers) ✅

| Method | Endpoint | Mô tả | Quyền |
|--------|----------|-------|-------|
| POST | `/api/lecturers/register` | Đăng ký giảng viên | Public |
| GET | `/api/lecturers` | Lấy danh sách giảng viên | ADMIN |
| GET | `/api/lecturers/{userId}` | Lấy giảng viên theo id | ADMIN |
| GET | `/api/lecturers/me` | Lấy thông tin cá nhân | LECTURER |
| PUT | `/api/lecturers/{userId}` | Cập nhật giảng viên | ADMIN |
| DELETE | `/api/lecturers/{userId}` | Xóa giảng viên (soft delete) | ADMIN |

**Ví dụ đăng ký giảng viên:**
```json
POST /api/lecturers/register
{
  "username": "lecturer1",
  "email": "lecturer1@example.com",
  "password": "123456",
  "lecturerCode": "GV001",
  "departmentId": 1,
  "academicTitle": "Thạc sĩ",
  "maxReviewLoad": 5,
  "expertiseTagIds": [1, 2, 3]
}
```

> **Lưu ý**: `expertiseTagIds` là danh sách ID chuyên môn của giảng viên — **bắt buộc** khi đăng ký, không được để trống.

### Khoa/Phòng ban (Departments)

| Method | Endpoint | Mô tả | Quyền |
|--------|----------|-------|-------|
| POST | `/api/departments` | Tạo khoa | ADMIN |
| GET | `/api/departments` | Lấy danh sách khoa | Public |
| GET | `/api/departments/{id}` | Lấy khoa theo id | Public |
| PUT | `/api/departments/{id}` | Cập nhật khoa | ADMIN |
| DELETE | `/api/departments/{id}` | Xóa khoa | ADMIN |

**Ví dụ tạo khoa:**
```json
POST /api/departments
{
  "name": "Công nghệ thông tin"
}
```

### Chuyên môn (Expertise Tags)

| Method | Endpoint | Mô tả | Quyền |
|--------|----------|-------|-------|
| POST | `/api/expertise-tags` | Tạo chuyên môn | ADMIN |
| GET | `/api/expertise-tags` | Lấy danh sách chuyên môn | Public |
| GET | `/api/expertise-tags/{id}` | Lấy chuyên môn theo id | Public |
| PUT | `/api/expertise-tags/{id}` | Cập nhật chuyên môn | ADMIN |
| DELETE | `/api/expertise-tags/{id}` | Xóa chuyên môn | ADMIN |

**Ví dụ tạo chuyên môn:**
```json
POST /api/expertise-tags
{
  "name": "Trí tuệ nhân tạo"
}
```

### Kỳ đăng ký (Registration Periods)

| Method | Endpoint | Mô tả | Quyền |
|--------|----------|-------|-------|
| POST | `/api/registration-periods` | Tạo kỳ đăng ký | ADMIN |
| GET | `/api/registration-periods` | Lấy danh sách kỳ đăng ký | Public |
| GET | `/api/registration-periods/{id}` | Lấy kỳ đăng ký theo id | Public |
| PUT | `/api/registration-periods/{id}` | Cập nhật kỳ đăng ký | ADMIN |
| DELETE | `/api/registration-periods/{id}` | Xóa kỳ đăng ký | ADMIN |

**Ví dụ tạo kỳ đăng ký:**
```json
POST /api/registration-periods
{
  "name": "Kỳ đăng ký HK1 2026",
  "type": "HK1",
  "lecturerRegistrationStart": "2026-09-01T08:00:00",
  "lecturerRegistrationEnd": "2026-09-10T17:00:00",
  "studentRegistrationStart": "2026-09-15T08:00:00",
  "studentRegistrationEnd": "2026-09-30T17:00:00",
  "reviewerScoreDeadline": "2026-10-15T17:00:00",
  "defenseDate": "2026-11-01T08:00:00"
}
```

### Đề tài (Topics) ✅

| Method | Endpoint | Mô tả | Quyền |
|--------|----------|-------|-------|
| POST | `/api/topics` | Tạo đề tài | LECTURER |
| GET | `/api/topics` | Lấy danh sách tất cả đề tài | ADMIN, LECTURER |
| GET | `/api/topics/published` | Lấy danh sách đề tài đã công bố | STUDENT |
| GET | `/api/topics/{id}` | Lấy đề tài theo id | ADMIN, LECTURER |
| GET | `/api/topics/me` | Lấy đề tài của giảng viên đang đăng nhập | LECTURER |
| PUT | `/api/topics/{id}` | Cập nhật đề tài | ADMIN, LECTURER |
| PATCH | `/api/topics/{id}/approve` | Duyệt đề tài | ADMIN |
| PATCH | `/api/topics/{id}/reject` | Từ chối đề tài | ADMIN |
| PATCH | `/api/topics/{id}/publish` | Công bố đề tài | ADMIN |
| DELETE | `/api/topics/{id}` | Xóa đề tài | ADMIN |

**Ví dụ tạo đề tài:**
```json
POST /api/topics
{
  "title": "Xây dựng hệ thống quản lý thư viện",
  "requirements": "Sinh viên cần nắm vững Java, Spring Boot, PostgreSQL",
  "departmentId": 1,
  "registrationPeriodId": 1
}
```

**Ví dụ từ chối đề tài:**
```json
PATCH /api/topics/1/reject
{
  "reason": "Đề tài trùng với đề tài đã có"
}
```

> **Trạng thái đề tài**: `PENDING` (chờ duyệt) → `APPROVED` (đã duyệt) / `REJECTED` (bị từ chối) → `PUBLISHED` (đã công bố) → `CLOSED` (đã đóng).

### Nhóm sinh viên (Student Groups) ✅

| Method | Endpoint | Mô tả | Quyền |
|--------|----------|-------|-------|
| POST | `/api/student-groups` | Tạo nhóm sinh viên | STUDENT |
| GET | `/api/student-groups` | Lấy danh sách tất cả nhóm | ADMIN, LECTURER |
| GET | `/api/student-groups/registration-period/{registrationPeriodId}` | Lấy danh sách nhóm theo kỳ đăng ký | ADMIN, LECTURER |
| GET | `/api/student-groups/{id}` | Lấy chi tiết nhóm theo id | ADMIN, LECTURER |
| DELETE | `/api/student-groups/{id}` | Xóa nhóm | ADMIN |

**Ví dụ tạo nhóm sinh viên:**
```json
POST /api/student-groups
{
  "registrationPeriodId": 1,
  "name": "Nhóm 1 - Khóa luận",
  "memberUserIds": [2, 3, 4]
}
```

> **Lưu ý**: Người tạo nhóm (từ token đăng nhập) tự động được thêm vào nhóm với vai trò `LEADER`. Các thành viên trong `memberUserIds` có vai trò `MEMBER`. Tất cả thành viên phải chưa thuộc nhóm nào trong cùng kỳ đăng ký.

### Bài nộp (Submissions) ✅

| Method | Endpoint | Mô tả | Quyền |
|--------|----------|-------|-------|
| POST | `/api/submissions` | Tạo bài nộp cho đăng ký đề tài | STUDENT |
| GET | `/api/submissions` | Lấy danh sách tất cả bài nộp | ADMIN, LECTURER |
| GET | `/api/submissions/{id}` | Lấy chi tiết bài nộp theo id | ADMIN, LECTURER, STUDENT |
| GET | `/api/submissions/topic-registration/{topicRegistrationId}` | Lấy bài nộp theo đăng ký đề tài | ADMIN, LECTURER, STUDENT |
| POST | `/api/submissions/feedbacks` | Tạo góp ý cho phiên bản bài nộp | LECTURER |
| GET | `/api/submissions/versions/{submissionVersionId}/feedbacks` | Lấy góp ý theo phiên bản bài nộp | ADMIN, LECTURER, STUDENT |
| GET | `/api/submissions/feedbacks/me` | Lấy góp ý của giảng viên đang đăng nhập | LECTURER |

**Ví dụ tạo bài nộp:**
```json
POST /api/submissions
{
  "topicRegistrationId": 1
}
```

> **Lưu ý**: Chỉ đăng ký đề tài ở trạng thái `APPROVED` mới được tạo bài nộp. Mỗi đăng ký đề tài chỉ có **một** bài nộp duy nhất (nếu đã có sẽ báo lỗi). Bài nộp có thể chứa nhiều phiên bản báo cáo (`SubmissionVersion`). Giảng viên có thể góp ý (`SubmissionFeedback`) cho từng phiên bản bài nộp.

## 📦 Format Response

Các module dùng `ApiResponse` (students, lecturers, expertisetags, registration_period, topics) trả về format chuẩn:

```json
{
  "timestamp": "2026-08-30T04:00:00",
  "status": 200,
  "message": "Lấy danh sách sinh viên thành công",
  "data": [ ... ]
}
```

> **Lưu ý**: Module `auth` và `departments` hiện **chưa** dùng `ApiResponse` — trả về DTO trực tiếp.

## 📝 Ghi chú

- **Database**: Dùng PostgreSQL, `spring.jpa.hibernate.ddl-auto=update` tự tạo/cập nhật bảng.
- **JWT**: Access token hết hạn sau 1 ngày (86400000ms), refresh token sau 1 tuần (604800000ms).
- **Soft delete**: Sinh viên/giảng viên khi xóa chỉ set `status = false`, không xóa khỏi database.

## ✅ Tổng quan tính năng

| Module | Trạng thái | Ghi chú |
|--------|-----------|---------|
| Auth (xác thực & phân quyền) | ✅ Hoàn thiện | Đăng ký SV/GV/Admin, đăng nhập, refresh token |
| Students (sinh viên) | ✅ Hoàn thiện | CRUD đầy đủ |
| Lecturers (giảng viên) | ✅ Hoàn thiện | CRUD đầy đủ, gồm chuyên môn (`expertiseTagIds`) |
| Departments (khoa) | ✅ Hoàn thiện | CRUD đầy đủ |
| Expertise Tags (chuyên môn) | ✅ Hoàn thiện | CRUD đầy đủ |
| Registration Periods (kỳ đăng ký) | ✅ Hoàn thiện | CRUD đầy đủ |
| Topics (đề tài) | ✅ Hoàn thiện | Tạo, xem, cập nhật, duyệt/từ chối/công bố, xóa. Lưu lý do từ chối |
| Topic Registrations (đăng ký đề tài) | ✅ Hoàn thiện | Đăng ký, duyệt/từ chối/hủy, xem theo nhóm/đề tài |
| Submissions (bài nộp) | ✅ Hoàn thiện | Tạo bài nộp, xem danh sách/chi tiết, xem theo đăng ký đề tài, góp ý bài nộp |

> 📌 **Tính năng đang chờ phát triển / chưa hoàn thiện:**
> - **Phân công giảng viên hướng dẫn** (`supervisorIds` hiện chưa có endpoint để gán) — chưa làm
> - **Chấm điểm phản biện** (reviewer score) — chưa làm
