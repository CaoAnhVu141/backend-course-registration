# Backend — Hệ thống Quản lý Đề tài Sinh viên

Backend cho **Hệ thống quản lý đề tài sinh viên Khoa CNTT** (quản lý đề tài, nhóm, đăng ký, nộp báo cáo, hội đồng, chấm điểm, thông báo realtime), xây dựng bằng **Spring Boot 3.2** và **Java 21**.

## 🚀 Công nghệ sử dụng

| Công nghệ | Phiên bản |
|-----------|-----------|
| Java | 21 |
| Spring Boot | 3.2.0 |
| Build tool | Maven |
| Database | PostgreSQL |
| ORM | Spring Data JPA (Hibernate) |
| Security | Spring Security + JWT (jjwt 0.12.6) |
| Realtime | Spring WebSocket + STOMP (thông báo đẩy realtime) |
| Export Excel | Apache POI (poi-ooxml 5.2.5) |
| Upload file | Spring Multipart (lưu file báo cáo xuống đĩa) |
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
│   ├── storage/
│   │   └── FileStorageService.java                  # Lưu/đọc file báo cáo trên đĩa
│   ├── websocket/
│   │   ├── WebSocketConfig.java                     # Cấu hình STOMP endpoint /ws
│   │   └── WebSocketAuthInterceptor.java            # Xác thực JWT khi CONNECT
│   └── security/
│       ├── JwtAuthenticationFilter.java             # Filter xác thực JWT
│       ├── JwtService.java                          # Tạo/kiểm tra JWT
│       └── SecurityConfig.java                      # Cấu hình Spring Security
└── modules/
    ├── auth/                                        # Module xác thực & phân quyền
    ├── defense_committees/                          # Module quản lý hội đồng bảo vệ
    ├── departments/                                 # Module quản lý khoa/phòng ban
    ├── expertisetags/                               # Module quản lý chuyên môn
    ├── lecturers/                                   # Module quản lý giảng viên
    ├── registration_period/                         # Module quản lý kỳ đăng ký
    ├── student_group/                               # Module quản lý nhóm sinh viên
    ├── students/                                    # Module quản lý sinh viên
    ├── topics/                                      # Module quản lý đề tài (có tìm kiếm/lọc)
    ├── topic_registrations/                         # Module quản lý đăng ký đề tài
    ├── submissions/                                 # Module quản lý bài nộp (upload file)
    ├── committee_topic_assignments/                 # Module phân công hội đồng chấm đề tài
    ├── evaluations/                                 # Module chấm điểm & công bố kết quả
    └── notifications/                               # Module thông báo (REST + WebSocket)
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

#### `modules/defense_committees` — Quản lý hội đồng bảo vệ ✅ (đã hoàn thiện)
```
defense_committees/
├── controller/DefenseCommitteeController.java
├── dto/CreateDefenseCommitteeRequest.java, AddCommitteeMemberRequest.java, DefenseCommitteeResponse.java, CommitteeMemberResponse.java
├── entity/DefenseCommittee.java, CommitteeMember.java
├── enums/EnumDefenseCommitteeError.java, EnumDefenseCommitteeSuccess.java, DefenseCommitteeStatus.java, CommitteeRole.java
├── exception/DefenseCommitteeException.java
├── mapper/DefenseCommitteeMapper.java
├── repository/DefenseCommitteeRepository.java, CommitteeMemberRepository.java
└── service/DefenseCommitteeService.java
```
> ✅ **Trạng thái**: Module này **đã hoàn thiện**. Hỗ trợ: tạo hội đồng (ADMIN), thêm thành viên hội đồng (ADMIN), danh sách hội đồng, chi tiết hội đồng, danh sách hội đồng theo kỳ đăng ký, giảng viên xem hội đồng mình thuộc về (LECTURER), đóng hội đồng (ADMIN). Trạng thái hội đồng gồm: `ACTIVE`, `INACTIVE`, `CLOSED`. Vai trò thành viên gồm: `MEMBER`, `REVIEWER`, `CHAIRMAN`, `SECRETARY`. Một giảng viên không thể thuộc cùng một hội đồng hai lần.

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

#### `modules/committee_topic_assignments` — Phân công hội đồng chấm đề tài ✅ (đã hoàn thiện)
```
committee_topic_assignments/
├── controller/CommitteeTopicAssignmentController.java
├── dto/CreateCommitteeTopicAssignmentRequest.java, UpdateDefenseScheduleRequest.java, CommitteeTopicAssignmentResponse.java
├── entity/CommitteeTopicAssignment.java
├── enums/CommitteeTopicAssignmentStatus.java, EnumCommitteeTopicAssignmentError.java, EnumCommitteeTopicAssignmentSuccess.java
├── exception/CommitteeTopicAssignmentException.java
├── mapper/CommitteeTopicAssignmentMapper.java
├── repository/CommitteeTopicAssignmentRepository.java
└── service/CommitteeTopicAssignmentService.java
```
> ✅ **Trạng thái**: **Đã hoàn thiện**. Gán một đăng ký đề tài (đã `APPROVED`) cho một hội đồng, kèm lịch + phòng bảo vệ. Mỗi đăng ký đề tài chỉ được phân công cho **đúng một** hội đồng. Ràng buộc khi phân công: hội đồng chưa `CLOSED` và **đã có chủ tịch**; hội đồng và đề tài cùng kỳ đăng ký; **GVHD không được chấm đề tài mình hướng dẫn**; thời gian bảo vệ không sớm hơn `defense_date` của kỳ; phòng không trùng giờ. Trạng thái phân công: `ASSIGNED`, `COMPLETED`, `CANCELED`. Hỗ trợ đầy đủ: tạo phân công, cập nhật lịch, complete/cancel, và các API GET (tất cả / chi tiết / theo hội đồng / GV xem lịch chấm của mình / nhóm SV xem lịch bảo vệ). Khi tạo phân công hoặc đổi lịch, hệ thống **tự gửi thông báo** (`DEFENSE_SCHEDULED`) cho nhóm sinh viên.

#### `modules/evaluations` — Chấm điểm & công bố kết quả ✅ (đã hoàn thiện)
```
evaluations/
├── controller/EvaluationController.java
├── dto/CreateEvaluationRequest.java, EvaluationResponse.java, FinalResultResponse.java
├── entity/Evaluation.java, FinalResult.java
├── enums/EvaluationStatus.java, EnumEvaluationError.java, EnumEvaluationSuccess.java
├── exception/EvaluationException.java
├── mapper/EvaluationMapper.java
├── repository/EvaluationRepository.java, FinalResultRepository.java
└── service/EvaluationService.java
```
> ✅ **Trạng thái**: **Đã hoàn thiện**. Mỗi thành viên hội đồng chấm điểm (0–10) cho một phân công đề tài (mỗi GV chỉ chấm 1 lần, phải thuộc hội đồng). **Chủ tịch (CHAIRMAN)** công bố kết quả: hệ thống tính **điểm trung bình cộng** của tất cả thành viên (yêu cầu đã chấm đủ) và lưu `FinalResult`. Sinh viên chỉ xem được điểm của nhóm mình. Khi công bố, nhóm sinh viên **nhận thông báo** (`RESULT_PUBLISHED`).

#### `modules/notifications` — Thông báo (REST + WebSocket realtime) ✅ (đã hoàn thiện)
```
notifications/
├── controller/NotificationController.java
├── dto/CreateNotificationRequest.java, NotificationResponse.java
├── entity/Notification.java
├── enums/NotificationType.java, NotificationTarget.java, EnumNotificationError.java, EnumNotificationSuccess.java
├── exception/NotificationException.java
├── mapper/NotificationMapper.java
├── repository/NotificationRepository.java
└── service/NotificationService.java
```
> ✅ **Trạng thái**: **Đã hoàn thiện**. Admin đăng thông báo tay (gửi `ALL` / theo role `STUDENT` / `LECTURER`). Các sự kiện nghiệp vụ tự sinh thông báo qua hàm dùng chung `notifyUsers(...)`. Mỗi thông báo vừa **lưu DB** vừa **đẩy realtime qua WebSocket** tới đúng người nhận (`/user/queue/notifications`). Loại thông báo (`NotificationType`): `ANNOUNCEMENT`, `TOPIC_PUBLISHED`, `REGISTRATION_DECISION`, `FEEDBACK`, `RESULT_PUBLISHED`, `SUBMISSION_SUBMITTED`, `DEFENSE_SCHEDULED`.



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
| GET | `/api/students/by-code/{studentCode}` | Tra cứu sinh viên theo mã số (dùng khi thêm thành viên nhóm) | ADMIN, LECTURER, STUDENT |
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
| GET | `/api/topics/search` | Tìm kiếm/lọc đề tài (keyword, departmentId, registrationPeriodId, status) | ADMIN, LECTURER |
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
> **Ràng buộc số lượng **: mỗi nhóm **tối đa 5 thành viên** (chặn khi tạo nhóm) và **tối thiểu 3 thành viên** mới được đăng ký đề tài (chặn khi đăng ký). Không thể xoá nhóm nếu nhóm đã có đăng ký đề tài.

### Bài nộp (Submissions) ✅

| Method | Endpoint | Mô tả | Quyền |
|--------|----------|-------|-------|
| POST | `/api/submissions` | Tạo bài nộp cho đăng ký đề tài (chỉ nhóm trưởng) | STUDENT |
| GET | `/api/submissions` | Lấy danh sách tất cả bài nộp | ADMIN, LECTURER |
| GET | `/api/submissions/{id}` | Lấy chi tiết bài nộp theo id | ADMIN, LECTURER, STUDENT |
| POST | `/api/submissions/versions` | **Upload file** phiên bản báo cáo (multipart: `submissionId`, `changeNote`, `file`) | STUDENT |
| GET | `/api/submissions/versions/{versionId}/download` | **Tải file** phiên bản báo cáo | ADMIN, LECTURER, STUDENT |
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

### Hội đồng bảo vệ (Defense Committees) ✅

| Method | Endpoint | Mô tả | Quyền |
|--------|----------|-------|-------|
| POST | `/api/defense-committees` | Tạo hội đồng bảo vệ | ADMIN |
| POST | `/api/defense-committees/{id}/members` | Thêm thành viên vào hội đồng | ADMIN |
| GET | `/api/defense-committees` | Lấy danh sách tất cả hội đồng | ADMIN, LECTURER |
| GET | `/api/defense-committees/{id}` | Lấy chi tiết hội đồng theo id | ADMIN, LECTURER |
| GET | `/api/defense-committees/registration-period/{registrationPeriodId}` | Lấy danh sách hội đồng theo kỳ đăng ký | ADMIN, LECTURER |
| GET | `/api/defense-committees/me` | Lấy danh sách hội đồng mà giảng viên đang đăng nhập thuộc về | LECTURER |
| PATCH | `/api/defense-committees/{id}/close` | Đóng hội đồng | ADMIN |

**Ví dụ tạo hội đồng:**
```json
POST /api/defense-committees
{
  "registrationPeriodId": 1,
  "code": "HD01",
  "name": "Hội đồng bảo vệ số 1"
}
```

**Ví dụ thêm thành viên vào hội đồng:**
```json
POST /api/defense-committees/1/members
{
  "lecturerId": 5,
  "committeeRole": "CHAIRMAN"
}
```

> **Lưu ý**: Hội đồng ở trạng thái `CLOSED` không thể thêm thành viên. Một giảng viên không thể được thêm vào cùng một hội đồng hai lần. Trạng thái hội đồng: `ACTIVE` (đang hoạt động), `INACTIVE` (ngừng hoạt động), `CLOSED` (đã đóng). Vai trò thành viên: `MEMBER`, `REVIEWER`, `CHAIRMAN`, `SECRETARY`.

### Đăng ký đề tài (Topic Registrations) ✅

| Method | Endpoint | Mô tả | Quyền |
|--------|----------|-------|-------|
| POST | `/api/topic-registrations` | Đăng ký đề tài cho nhóm (nhóm ≥ 3 thành viên) | STUDENT |
| GET | `/api/topic-registrations` | Danh sách tất cả đăng ký | ADMIN, LECTURER |
| GET | `/api/topic-registrations/{id}` | Chi tiết đăng ký | ADMIN, LECTURER, STUDENT |
| GET | `/api/topic-registrations/student-group/{studentGroupId}` | Đăng ký theo nhóm | ADMIN, LECTURER |
| GET | `/api/topic-registrations/topic/{topicId}` | Đăng ký theo đề tài | ADMIN, LECTURER |
| PATCH | `/api/topic-registrations/{id}/approve` | Duyệt đăng ký | ADMIN, LECTURER |
| PATCH | `/api/topic-registrations/{id}/reject` | Từ chối đăng ký | ADMIN, LECTURER |
| PATCH | `/api/topic-registrations/{id}/cancel` | Huỷ đăng ký | STUDENT |
| GET | `/api/topic-registrations/me` | Lịch sử đăng ký đề tài của các nhóm mình tham gia | STUDENT |

> Khi **duyệt/từ chối** đăng ký, nhóm sinh viên nhận thông báo `REGISTRATION_DECISION`.

### Phân công hội đồng chấm đề tài (Committee Topic Assignments) ✅

| Method | Endpoint | Mô tả | Quyền |
|--------|----------|-------|-------|
| POST | `/api/committee-topic-assignments` | Phân công đề tài cho hội đồng | ADMIN |
| PUT | `/api/committee-topic-assignments/{id}/schedule` | Cập nhật lịch + phòng bảo vệ | ADMIN |
| PATCH | `/api/committee-topic-assignments/{id}/complete` | Xác nhận đã bảo vệ xong | ADMIN |
| PATCH | `/api/committee-topic-assignments/{id}/cancel` | Huỷ phân công | ADMIN |
| GET | `/api/committee-topic-assignments` | Danh sách tất cả phân công | ADMIN |
| GET | `/api/committee-topic-assignments/{id}` | Chi tiết phân công | ADMIN, LECTURER |
| GET | `/api/committee-topic-assignments/defense-committee/{defenseCommitteeId}` | Đề tài một hội đồng chấm | ADMIN, LECTURER |
| GET | `/api/committee-topic-assignments/me` | Lịch chấm của GV đang đăng nhập | LECTURER |
| GET | `/api/committee-topic-assignments/my-group` | Lịch bảo vệ của nhóm SV | STUDENT |

```json
POST /api/committee-topic-assignments
{
  "defenseCommitteeId": 1,
  "topicRegistrationId": 1,
  "scheduledStart": "2026-12-20T08:00:00",
  "scheduledEnd": "2026-12-20T09:00:00",
  "room": "A5-101"
}
```

### Chấm điểm & Kết quả (Evaluations) ✅

| Method | Endpoint | Mô tả | Quyền |
|--------|----------|-------|-------|
| POST | `/api/evaluations` | Thành viên hội đồng chấm điểm (0–10) cho 1 phân công | LECTURER |
| POST | `/api/evaluations/assignments/{assignmentId}/publish` | **Chủ tịch** công bố kết quả (tính điểm TB) | LECTURER (CHAIRMAN) |
| GET | `/api/evaluations/assignments/{assignmentId}` | Danh sách điểm của một phân công | ADMIN, LECTURER |
| GET | `/api/evaluations/assignments/{assignmentId}/result` | Kết quả cuối cùng của một phân công | ADMIN, LECTURER, STUDENT |
| GET | `/api/evaluations/my-group` | Điểm của các nhóm mà SV tham gia | STUDENT |

```json
POST /api/evaluations
{
  "assignmentId": 1,
  "score": 8.5,
  "comment": "Báo cáo tốt, cần bổ sung phần kiểm thử"
}
```

> Công bố kết quả yêu cầu **tất cả thành viên hội đồng đã chấm**; điểm cuối = trung bình cộng. Khi công bố, nhóm SV nhận thông báo `RESULT_PUBLISHED`.

### Thông báo (Notifications) ✅

| Method | Endpoint | Mô tả | Quyền |
|--------|----------|-------|-------|
| POST | `/api/notifications` | Admin đăng thông báo tay (ALL / theo role) | ADMIN |
| GET | `/api/notifications/me` | Thông báo của tôi (mới nhất trước) | ADMIN, LECTURER, STUDENT |
| GET | `/api/notifications/me/unread-count` | Số thông báo chưa đọc | ADMIN, LECTURER, STUDENT |
| PATCH | `/api/notifications/{id}/read` | Đánh dấu đã đọc | ADMIN, LECTURER, STUDENT |

```json
POST /api/notifications
{
  "target": "STUDENT",
  "title": "Thông báo lịch bảo vệ",
  "content": "Các nhóm chuẩn bị bảo vệ tuần sau."
}
```

> `target`: `ALL` / `STUDENT` / `LECTURER`.

### WebSocket (Thông báo realtime)

| Thành phần | Giá trị |
|------------|---------|
| Endpoint (SockJS) | `GET /ws` (kèm header `Authorization: Bearer <accessToken>` lúc CONNECT) |
| Kênh nhận thông báo | subscribe `/user/queue/notifications` |
| Prefix broker | `/topic`, `/queue`, `/user` |
| Prefix client → server | `/app` |

Ví dụ FE (SockJS + STOMP):
```js
const sock = new SockJS('http://localhost:8080/ws');
const stomp = Stomp.over(sock);
stomp.connect({ Authorization: 'Bearer ' + token }, () => {
  stomp.subscribe('/user/queue/notifications', msg => console.log(JSON.parse(msg.body)));
});
```

> Mỗi khi có sự kiện (nộp bài, nhận xét, phân công/đổi lịch bảo vệ, duyệt/từ chối đăng ký, công bố đề tài, công bố điểm), hệ thống vừa lưu DB vừa **đẩy realtime** tới đúng người nhận đang online.

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
| Submissions (bài nộp) | ✅ Hoàn thiện | Tạo bài nộp (nhóm trưởng), **upload/tải file** báo cáo, góp ý bài nộp |
| Defense Committees (hội đồng bảo vệ) | ✅ Hoàn thiện | Tạo hội đồng, thêm thành viên, xem danh sách/chi tiết, xem theo kỳ đăng ký, xem hội đồng của tôi, đóng hội đồng |
| Committee Topic Assignments (phân công hội đồng) | ✅ Hoàn thiện | Phân công đề tài, lịch/phòng bảo vệ, complete/cancel, các API GET |
| Evaluations (chấm điểm) | ✅ Hoàn thiện | Chấm điểm 0–10, chủ tịch công bố điểm TB, SV tra cứu điểm nhóm mình |
| Notifications (thông báo) | ✅ Hoàn thiện | REST + **WebSocket realtime**, admin đăng tay + 7 loại sự kiện tự động |
| Export Excel | ✅ Hoàn thiện | Xuất danh sách (SV, GV, đề tài, đăng ký, phân công, chấm điểm...) ra file `.xlsx` |

### 🆕 Nghiệp vụ nổi bật

- **Kỳ đăng ký — `type` là enum 4 giá trị**: `SUBJECT` (môn học), `NCKH`, `TLCN`, `KLTN`. `reviewerScoreDeadline` chỉ thiết lập khi `TLCN`/`KLTN`; `defenseDate` chỉ khi `KLTN`. Gửi sai loại (vd `SUBJECT` kèm `defenseDate`) → báo lỗi.
- **Đề tài — 1 đến 2 GVHD**: người tạo là GVHD chính; truyền thêm `coSupervisorId` (tùy chọn, phải khác người tạo) để có GVHD thứ 2.
- **Tìm kiếm/lọc đề tài** nâng cao: `GET /api/topics/search` theo keyword + bộ môn + kỳ + trạng thái.
- **Nhóm sinh viên — 3 đến 5 thành viên**: tối đa 5 khi tạo nhóm, tối thiểu 3 khi đăng ký đề tài. SV đã thuộc nhóm A không vào nhóm B cùng kỳ. Mỗi nhóm đúng 1 trưởng nhóm.
- **Bài nộp — chỉ nhóm trưởng (LEADER)** mới được nộp; hỗ trợ **upload file** báo cáo nhiều phiên bản và **tải file** về.
- **Hội đồng bảo vệ — 3 đến 5 GV**, phải có **đúng 1 CHAIRMAN và 1 SECRETARY**. GVHD **không được chấm** đề tài mình hướng dẫn.
- **Chấm điểm**: tất cả thành viên hội đồng chấm 0–10; **chủ tịch** công bố điểm trung bình cộng; SV chỉ xem điểm nhóm mình theo đợt đã đăng ký.
- **Thông báo realtime (WebSocket)**: 7 loại sự kiện tự đẩy tới người nhận online (thay cho gửi email).

> 📌 **Không triển khai**: gửi email công bố kết quả (đã thay bằng **thông báo realtime qua WebSocket** — ghi rõ trong báo cáo ở mục tính năng mở rộng).

