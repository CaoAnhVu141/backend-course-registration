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
    └── students/                                    # Module quản lý sinh viên
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

#### `modules/departments` — Quản lý khoa/phòng ban
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

#### `modules/expertisetags` — Quản lý chuyên môn
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

#### `modules/lecturers` — Quản lý giảng viên ⚠️ (đang phát triển)
```
lecturers/
├── controller/LecturerController.java
├── dto/RegisterLecturerRequest.java, LecturerResponse.java, UpdateLecturerRequest.java (rỗng)
├── entity/Lecturer.java
├── mapper/LecturerMapper.java
├── repository/LecturerRepository.java
└── service/LecturerService.java
```
> ⚠️ **Trạng thái**: Module này **chưa hoàn thiện**. Hiện chỉ có chức năng `register`. Các file `EnumLecturerSuccess.java` và `EnumLecturerError.java` đang bị đặt **sai thư mục** (trong `entity/` thay vì `enums/`) và **nội dung rỗng**. `UpdateLecturerRequest.java` cũng đang rỗng. Cần hoàn thiện thêm: getAll, getById, getMyProfile, update, delete.

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

#### `modules/students` — Quản lý sinh viên
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

### Giảng viên (Lecturers) ⚠️

| Method | Endpoint | Mô tả | Quyền |
|--------|----------|-------|-------|
| POST | `/api/lecturers/register` | Đăng ký giảng viên | Public |

> ⚠️ Module này **chưa hoàn thiện** — hiện chỉ có endpoint đăng ký.

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
  "maxReviewLoad": 5
}
```

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

## 📦 Format Response

Các module dùng `ApiResponse` (students, expertisetags, registration_period) trả về format chuẩn:

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
- **Module lecturers** đang trong quá trình phát triển, chưa hoàn thiện đầy đủ.
