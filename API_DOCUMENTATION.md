# 📘 API Documentation — Hệ thống Đăng ký Khóa luận

> Tài liệu tổng hợp toàn bộ API đã triển khai ở backend, dành cho đội Frontend.
>
> **Base URL:** `http://localhost:8080`
> **Định dạng dữ liệu:** JSON

---

## 🔐 Xác thực (Authentication)

Hầu hết các endpoint yêu cầu **JWT Bearer Token**. Sau khi đăng nhập, lấy `accessToken` và gửi kèm trong mọi request:

```
Authorization: Bearer <accessToken>
```

**Các vai trò (Roles) trong hệ thống:**

| Role | Mô tả |
|------|-------|
| `ADMIN` | Quản trị viên — quản lý toàn hệ thống |
| `LECTURER` | Giảng viên |
| `STUDENT` | Sinh viên |

> 💡 **Lưu ý:** Nếu gọi endpoint yêu cầu role mà token không có quyền, server trả về **403 Forbidden**.

---

## 📦 Định dạng Response chuẩn

Hầu hết các endpoint trả về wrapper `ApiResponse`:

```json
{
  "timestamp": "2026-08-30T13:10:34",
  "status": 200,
  "message": "Lấy danh sách sinh viên thành công",
  "data": { ... }
}
```

| Field | Kiểu | Mô tả |
|-------|------|-------|
| `timestamp` | string | Thời gian server trả response |
| `status` | int | HTTP status code |
| `message` | string | Thông báo tiếng Việt |
| `data` | object/array | Payload chính |


---

# 1. 🔑 Auth — `/api/auth`

## 1.1 Đăng ký sinh viên
- **`POST /api/auth/register/student`** — Công khai (không cần token)
- **Body (JSON):**
```json
{
  "username": "sv001",
  "email": "sv001@hcmute.edu.vn",
  "password": "123456",
  "studentCode": "20110001",
  "departmentId": 1,
  "className": "D20CQCN01"
}
```
- **Response (201):** `StudentResponse` (trả trực tiếp, không bọc ApiResponse)

## 1.2 Đăng ký giảng viên
- **`POST /api/auth/register/lecturer`** — Công khai
- **Body (JSON):**
```json
{
  "username": "gv001",
  "email": "gv001@hcmute.edu.vn",
  "password": "123456",
  "lecturerCode": "GV001",
  "departmentId": 1,
  "academicTitle": "ThS",
  "maxReviewLoad": 10,
  "expertiseTagIds": [1, 2, 3]
}
```
- **Giải thích field:**
  - `expertiseTagIds`: danh sách ID chuyên môn (expertise tag) của giảng viên — **bắt buộc**, không được để trống
- **Response (201):** `LecturerResponse`

## 1.3 Đăng ký Admin
- **`POST /api/auth/register/admin`** — Công khai
- **Body (JSON):**
```json
{
  "username": "admin",
  "email": "admin@hcmute.edu.vn",
  "password": "123456"
}
```
- **Response (201):** `RegisterResponse`

## 1.4 Đăng nhập
- **`POST /api/auth/login`** — Công khai
- **Body (JSON):**
```json
{
  "username": "admin",
  "password": "123456"
}
```
- **Response (200):**
```json
{
  "accessToken": "eyJhbGciOi...",
  "refreshToken": "eyJhbGciOi...",
  "tokenType": "Bearer",
  "id": 1,
  "username": "admin",
  "email": "admin@hcmute.edu.vn",
  "roles": ["ADMIN"],
  "expiresIn": 3600
}
```
- **Giải thích response:**
  - `accessToken`: dùng cho mọi request (gửi trong header `Authorization`)
  - `refreshToken`: dùng để lấy access token mới khi hết hạn
  - `expiresIn`: thời gian sống của access token (giây)

## 1.5 Refresh token
- **`POST /api/auth/refresh-token`** — Công khai
- **Body (JSON):**
```json
{
  "refreshToken": "eyJhbGciOi..."
}
```
- **Response (200):** `LoginResponse` (cấp lại access token mới)

---

# 2. 🎓 Sinh viên — `/api/students`

| Method | Endpoint | Role | Mô tả |
|--------|----------|------|-------|
| POST | `/api/students/register` | Công khai | Đăng ký sinh viên |
| GET | `/api/students` | ADMIN, LECTURER | Danh sách sinh viên |
| GET | `/api/students/{userId}` | ADMIN, LECTURER | Chi tiết sinh viên theo userId |
| GET | `/api/students/me` | STUDENT | Hồ sơ của sinh viên đang đăng nhập |
| PUT | `/api/students/{userId}` | ADMIN | Cập nhật sinh viên |
| DELETE | `/api/students/{userId}` | ADMIN | Xóa sinh viên |

## 2.1 Đăng ký sinh viên
- **`POST /api/students/register`** — Công khai
- **Body:**
```json
{
  "username": "sv001",
  "email": "sv001@hcmute.edu.vn",
  "password": "123456",
  "studentCode": "20110001",
  "departmentId": 1,
  "className": "D20CQCN01"
}
```
- **Response (201):** `ApiResponse<StudentResponse>`

## 2.2 Danh sách sinh viên
- **`GET /api/students`** — ADMIN, LECTURER
- **Response (200):** `ApiResponse<List<StudentResponse>>`

## 2.3 Chi tiết sinh viên
- **`GET /api/students/{userId}`** — ADMIN, LECTURER
- **Path param:** `userId` (Long)
- **Response (200):** `ApiResponse<StudentResponse>`

## 2.4 Hồ sơ của tôi (My Profile)
- **`GET /api/students/me`** — STUDENT
- **Không cần truyền tham số** — server tự lấy username từ token đăng nhập
- **Response (200):** `ApiResponse<StudentResponse>`

## 2.5 Cập nhật sinh viên
- **`PUT /api/students/{userId}`** — ADMIN
- **Path param:** `userId` (Long)
- **Body:**
```json
{
  "studentCode": "20110002",
  "departmentId": 2,
  "className": "D20CQCN02"
}
```
- **Response (200):** `ApiResponse<StudentResponse>`

## 2.6 Xóa sinh viên
- **`DELETE /api/students/{userId}`** — ADMIN
- **Path param:** `userId` (Long)
- **Response (200):** `ApiResponse<Void>` (`data` = null)

## 📄 Cấu trúc `StudentResponse`
```json
{
  "userId": 1,
  "username": "sv001",
  "email": "sv001@hcmute.edu.vn",
  "roles": ["STUDENT"],
  "studentCode": "20110001",
  "departmentId": 1,
  "className": "D20CQCN01"
}
```

---

# 3. 👨‍🏫 Giảng viên — `/api/lecturers`

| Method | Endpoint | Role | Mô tả |
|--------|----------|------|-------|
| POST | `/api/lecturers/register` | Công khai | Đăng ký giảng viên |
| GET | `/api/lecturers` | ADMIN | Danh sách giảng viên |
| GET | `/api/lecturers/{userId}` | ADMIN | Chi tiết giảng viên theo userId |
| GET | `/api/lecturers/me` | LECTURER | Hồ sơ của giảng viên đang đăng nhập |
| PUT | `/api/lecturers/{userId}` | ADMIN | Cập nhật giảng viên |
| DELETE | `/api/lecturers/{userId}` | ADMIN | Xóa giảng viên |

## 3.1 Đăng ký giảng viên
- **`POST /api/lecturers/register`** — Công khai
- **Body:**
```json
{
  "username": "gv001",
  "email": "gv001@hcmute.edu.vn",
  "password": "123456",
  "lecturerCode": "GV001",
  "departmentId": 1,
  "academicTitle": "ThS",
  "maxReviewLoad": 10,
  "expertiseTagIds": [1, 2, 3]
}
```
- **Giải thích field:**
  - `expertiseTagIds`: danh sách ID chuyên môn của giảng viên — **bắt buộc**, không được để trống
- **Response (201):** `LecturerResponse`

## 3.2 Danh sách giảng viên
- **`GET /api/lecturers`** — ADMIN
- **Response (200):** `ApiResponse<List<LecturerResponse>>`

## 3.3 Chi tiết giảng viên
- **`GET /api/lecturers/{userId}`** — ADMIN
- **Path param:** `userId` (Long)
- **Response (200):** `ApiResponse<LecturerResponse>`

## 3.4 Hồ sơ của tôi (My Profile)
- **`GET /api/lecturers/me`** — LECTURER
- **Không cần truyền tham số** — server tự lấy username từ token đăng nhập
- **Response (200):** `ApiResponse<LecturerResponse>`

## 3.5 Cập nhật giảng viên
- **`PUT /api/lecturers/{userId}`** — ADMIN
- **Path param:** `userId` (Long)
- **Body:**
```json
{
  "lecturerCode": "GV002",
  "departmentId": 2,
  "academicTitle": "TS",
  "maxReviewLoad": 12,
  "expertiseTagIds": [2, 4]
}
```
- **Giải thích field:**
  - `expertiseTagIds`: danh sách ID chuyên môn mới — sẽ **thay thế toàn bộ** danh sách chuyên môn cũ của giảng viên
- **Response (200):** `ApiResponse<LecturerResponse>`

## 3.6 Xóa giảng viên
- **`DELETE /api/lecturers/{userId}`** — ADMIN
- **Path param:** `userId` (Long)
- **Response (200):** `ApiResponse<Void>` (`data` = null)

## 📄 Cấu trúc `LecturerResponse`
```json
{
  "userId": 1,
  "username": "gv001",
  "email": "gv001@hcmute.edu.vn",
  "roles": ["LECTURER"],
  "lecturerCode": "GV001",
  "departmentId": 1,
  "academicTitle": "ThS",
  "maxReviewLoad": 10,
  "expertiseTagIds": [1, 2, 3]
}
```
- **Giải thích field:**
  - `expertiseTagIds`: danh sách ID chuyên môn của giảng viên (lấy từ bảng phụ `lecturer_expertise`)

---

# 4. 🏢 Khoa/Phòng ban — `/api/departments`

| Method | Endpoint | Role | Mô tả |
|--------|----------|------|-------|
| POST | `/api/departments` | ADMIN | Tạo khoa |
| GET | `/api/departments` | Công khai | Danh sách khoa |
| GET | `/api/departments/{id}` | Công khai | Chi tiết khoa |
| PUT | `/api/departments/{id}` | ADMIN | Cập nhật khoa |
| DELETE | `/api/departments/{id}` | ADMIN | Xóa khoa |

## 4.1 Tạo khoa
- **`POST /api/departments`** — ADMIN
- **Body:**
```json
{ "name": "Khoa Công nghệ Thông tin" }
```
- **Response (201):** `DepartmentResponse` (trả trực tiếp, không bọc ApiResponse)

## 4.2 Danh sách khoa
- **`GET /api/departments`** — Công khai
- **Response (200):** `List<DepartmentResponse>`

## 4.3 Chi tiết khoa
- **`GET /api/departments/{id}`** — Công khai
- **Path param:** `id` (Long)
- **Response (200):** `DepartmentResponse`

## 4.4 Cập nhật khoa
- **`PUT /api/departments/{id}`** — ADMIN
- **Path param:** `id` (Long)
- **Body:**
```json
{ "name": "Khoa Công nghệ Thông tin" }
```
- **Response (200):** `DepartmentResponse`

## 4.5 Xóa khoa
- **`DELETE /api/departments/{id}`** — ADMIN
- **Path param:** `id` (Long)
- **Response (204):** No Content

## 📄 Cấu trúc `DepartmentResponse`
```json
{
  "departmentId": 1,
  "name": "Khoa Công nghệ Thông tin"
}
```

---

# 5. 🏷️ Chuyên môn (Expertise Tag) — `/api/expertise-tags`

| Method | Endpoint | Role | Mô tả |
|--------|----------|------|-------|
| POST | `/api/expertise-tags` | ADMIN | Tạo chuyên môn |
| GET | `/api/expertise-tags` | Công khai | Danh sách chuyên môn |
| GET | `/api/expertise-tags/{id}` | Công khai | Chi tiết chuyên môn |
| PUT | `/api/expertise-tags/{id}` | ADMIN | Cập nhật chuyên môn |
| DELETE | `/api/expertise-tags/{id}` | ADMIN | Xóa chuyên môn |

## 5.1 Tạo chuyên môn
- **`POST /api/expertise-tags`** — ADMIN
- **Body:**
```json
{ "name": "Trí tuệ nhân tạo" }
```
- **Response (201):** `ApiResponse<ExpertiseTagResponse>`

## 5.2 Danh sách chuyên môn
- **`GET /api/expertise-tags`** — Công khai
- **Response (200):** `ApiResponse<List<ExpertiseTagResponse>>`

## 5.3 Chi tiết chuyên môn
- **`GET /api/expertise-tags/{id}`** — Công khai
- **Path param:** `id` (Long)
- **Response (200):** `ApiResponse<ExpertiseTagResponse>`

## 5.4 Cập nhật chuyên môn
- **`PUT /api/expertise-tags/{id}`** — ADMIN
- **Path param:** `id` (Long)
- **Body:**
```json
{ "name": "Trí tuệ nhân tạo" }
```
- **Response (200):** `ApiResponse<ExpertiseTagResponse>`

## 5.5 Xóa chuyên môn
- **`DELETE /api/expertise-tags/{id}`** — ADMIN
- **Path param:** `id` (Long)
- **Response (200):** `ApiResponse<Void>` (`data` = null)

## 📄 Cấu trúc `ExpertiseTagResponse`
```json
{
  "id": 1,
  "name": "Trí tuệ nhân tạo",
  "status": true
}
```

---

# 6. 📅 Kỳ đăng ký — `/api/registration-periods`

| Method | Endpoint | Role | Mô tả |
|--------|----------|------|-------|
| POST | `/api/registration-periods` | ADMIN | Tạo kỳ đăng ký |
| GET | `/api/registration-periods` | Công khai | Danh sách kỳ đăng ký |
| GET | `/api/registration-periods/{id}` | Công khai | Chi tiết kỳ đăng ký |
| PUT | `/api/registration-periods/{id}` | ADMIN | Cập nhật kỳ đăng ký |
| DELETE | `/api/registration-periods/{id}` | ADMIN | Xóa kỳ đăng ký |

## 6.1 Tạo kỳ đăng ký
- **`POST /api/registration-periods`** — ADMIN
- **Body** (thời gian dạng ISO `yyyy-MM-ddTHH:mm:ss`):
```json
{
  "name": "HK1 2026-2027",
  "type": "KHOA_LUAN",
  "lecturerRegistrationStart": "2026-09-01T08:00:00",
  "lecturerRegistrationEnd": "2026-09-15T17:00:00",
  "studentRegistrationStart": "2026-09-20T08:00:00",
  "studentRegistrationEnd": "2026-10-05T17:00:00",
  "reviewerScoreDeadline": "2026-11-01T17:00:00",
  "defenseDate": "2026-12-01T08:00:00"
}
```
- **Response (201):** `ApiResponse<RegistrationPeriodResponse>`

## 6.2 Danh sách kỳ đăng ký
- **`GET /api/registration-periods`** — Công khai
- **Response (200):** `ApiResponse<List<RegistrationPeriodResponse>>`

## 6.3 Chi tiết kỳ đăng ký
- **`GET /api/registration-periods/{id}`** — Công khai
- **Path param:** `id` (Long)
- **Response (200):** `ApiResponse<RegistrationPeriodResponse>`

## 6.4 Cập nhật kỳ đăng ký
- **`PUT /api/registration-periods/{id}`** — ADMIN
- **Path param:** `id` (Long)
- **Body:** giống Create + thêm `status`
```json
{
  "name": "HK1 2026-2027",
  "type": "KHOA_LUAN",
  "lecturerRegistrationStart": "2026-09-01T08:00:00",
  "lecturerRegistrationEnd": "2026-09-15T17:00:00",
  "studentRegistrationStart": "2026-09-20T08:00:00",
  "studentRegistrationEnd": "2026-10-05T17:00:00",
  "reviewerScoreDeadline": "2026-11-01T17:00:00",
  "defenseDate": "2026-12-01T08:00:00",
  "status": true
}
```
- **Response (200):** `ApiResponse<RegistrationPeriodResponse>`

## 6.5 Xóa kỳ đăng ký
- **`DELETE /api/registration-periods/{id}`** — ADMIN
- **Path param:** `id` (Long)
- **Response (200):** `ApiResponse<Void>` (`data` = null)

## 📄 Cấu trúc `RegistrationPeriodResponse`
```json
{
  "id": 1,
  "name": "HK1 2026-2027",
  "type": "KHOA_LUAN",
  "lecturerRegistrationStart": "2026-09-01T08:00:00",
  "lecturerRegistrationEnd": "2026-09-15T17:00:00",
  "studentRegistrationStart": "2026-09-20T08:00:00",
  "studentRegistrationEnd": "2026-10-05T17:00:00",
  "reviewerScoreDeadline": "2026-11-01T17:00:00",
  "defenseDate": "2026-12-01T08:00:00",
  "status": true
}
```

---

# 7. 📝 Đề tài (Topic) — `/api/topics`

| Method | Endpoint | Role | Mô tả |
|--------|----------|------|-------|
| POST | `/api/topics` | LECTURER | Tạo đề tài |
| GET | `/api/topics` | ADMIN, LECTURER | Danh sách tất cả đề tài |
| GET | `/api/topics/published` | STUDENT | Danh sách đề tài đã công bố |
| GET | `/api/topics/{id}` | ADMIN, LECTURER | Chi tiết đề tài theo id |
| GET | `/api/topics/me` | LECTURER | Danh sách đề tài của giảng viên đang đăng nhập |
| PUT | `/api/topics/{id}` | ADMIN, LECTURER | Cập nhật đề tài |
| PATCH | `/api/topics/{id}/approve` | ADMIN | Duyệt đề tài |
| PATCH | `/api/topics/{id}/reject` | ADMIN | Từ chối đề tài |
| PATCH | `/api/topics/{id}/publish` | ADMIN | Công bố đề tài |
| DELETE | `/api/topics/{id}` | ADMIN | Xóa đề tài |

## 7.1 Tạo đề tài
- **`POST /api/topics`** — LECTURER
- **Body:**
```json
{
  "title": "Xây dựng hệ thống quản lý thư viện",
  "requirements": "Sinh viên cần nắm vững Java, Spring Boot, PostgreSQL",
  "departmentId": 1,
  "registrationPeriodId": 1
}
```
- **Giải thích field:**
  - `title`: tên đề tài — **bắt buộc**, tối đa 255 ký tự
  - `requirements`: yêu cầu của đề tài (không bắt buộc)
  - `departmentId`: ID khoa/phòng ban — **bắt buộc**
  - `registrationPeriodId`: ID kỳ đăng ký — **bắt buộc**
- **Lưu ý:** `proposedBy` (người đề xuất) và `status` được server tự gán — `proposedBy` = username giảng viên đang đăng nhập, `status` = `PENDING`
- **Response (201):** `ApiResponse<TopicResponse>`

## 7.2 Danh sách tất cả đề tài
- **`GET /api/topics`** — ADMIN, LECTURER
- **Response (200):** `ApiResponse<List<TopicResponse>>`

## 7.3 Danh sách đề tài đã công bố
- **`GET /api/topics/published`** — STUDENT
- **Response (200):** `ApiResponse<List<TopicResponse>>` (chỉ các đề tài có `status = PUBLISHED`)

## 7.4 Chi tiết đề tài
- **`GET /api/topics/{id}`** — ADMIN, LECTURER
- **Path param:** `id` (Long)
- **Response (200):** `ApiResponse<TopicResponse>`

## 7.5 Đề tài của tôi (My Topics)
- **`GET /api/topics/me`** — LECTURER
- **Không cần truyền tham số** — server tự lấy username từ token đăng nhập
- **Response (200):** `ApiResponse<List<TopicResponse>>`

## 7.6 Cập nhật đề tài
- **`PUT /api/topics/{id}`** — ADMIN, LECTURER
- **Path param:** `id` (Long)
- **Body:** giống Create
```json
{
  "title": "Xây dựng hệ thống quản lý thư viện (nâng cấp)",
  "requirements": "Sinh viên cần nắm vững Java, Spring Boot, PostgreSQL, Docker",
  "departmentId": 1,
  "registrationPeriodId": 1
}
```
- **Response (200):** `ApiResponse<TopicResponse>`

## 7.7 Duyệt đề tài
- **`PATCH /api/topics/{id}/approve`** — ADMIN
- **Path param:** `id` (Long)
- **Không cần body**
- **Response (200):** `ApiResponse<TopicResponse>` (status → `APPROVED`)

## 7.8 Từ chối đề tài
- **`PATCH /api/topics/{id}/reject`** — ADMIN
- **Path param:** `id` (Long)
- **Body:**
```json
{
  "reason": "Đề tài trùng với đề tài đã có"
}
```
- **Giải thích field:**
  - `reason`: lý do từ chối — **bắt buộc**, không được để trống. Lý do này sẽ được **lưu vào database** và trả về trong `TopicResponse.rejectReason`
- **Response (200):** `ApiResponse<TopicResponse>` (status → `REJECTED`)

## 7.9 Công bố đề tài
- **`PATCH /api/topics/{id}/publish`** — ADMIN
- **Path param:** `id` (Long)
- **Không cần body**
- **Response (200):** `ApiResponse<TopicResponse>` (status → `PUBLISHED`)

## 7.10 Xóa đề tài
- **`DELETE /api/topics/{id}`** — ADMIN
- **Path param:** `id` (Long)
- **Response (200):** `ApiResponse<Void>` (`data` = null)

## 📄 Cấu trúc `TopicResponse`
```json
{
  "id": 1,
  "title": "Xây dựng hệ thống quản lý thư viện",
  "requirements": "Sinh viên cần nắm vững Java, Spring Boot, PostgreSQL",
  "departmentId": 1,
  "registrationPeriodId": 1,
  "proposedBy": "gv001",
  "status": "PENDING",
  "rejectReason": null,
  "supervisorIds": [1, 2]
}
```
- **Giải thích field:**
  - `proposedBy`: username của giảng viên đề xuất đề tài
  - `status`: trạng thái đề tài — một trong các giá trị: `PENDING`, `APPROVED`, `REJECTED`, `PUBLISHED`, `CLOSED`
  - `rejectReason`: lý do từ chối — chỉ có giá trị khi `status = REJECTED`, các trạng thái khác là `null`
  - `supervisorIds`: danh sách ID giảng viên hướng dẫn (có thể rỗng)

---

# 8. 👥 Nhóm sinh viên — `/api/student-groups`

| Method | Endpoint | Role | Mô tả |
|--------|----------|------|-------|
| POST | `/api/student-groups` | STUDENT | Tạo nhóm sinh viên |
| GET | `/api/student-groups` | ADMIN, LECTURER | Danh sách tất cả nhóm |
| GET | `/api/student-groups/registration-period/{registrationPeriodId}` | ADMIN, LECTURER | Danh sách nhóm theo kỳ đăng ký |
| GET | `/api/student-groups/{id}` | ADMIN, LECTURER | Chi tiết nhóm theo id |
| DELETE | `/api/student-groups/{id}` | ADMIN | Xóa nhóm |

## 8.1 Tạo nhóm sinh viên
- **`POST /api/student-groups`** — STUDENT
- **Body:**
```json
{
  "registrationPeriodId": 1,
  "name": "Nhóm 1 - Khóa luận",
  "memberUserIds": [2, 3, 4]
}
```
- **Giải thích field:**
  - `registrationPeriodId`: ID kỳ đăng ký — **bắt buộc**, phải là số dương
  - `name`: tên nhóm — **bắt buộc**, tối đa 255 ký tự
  - `memberUserIds`: danh sách ID sinh viên (userId) trong nhóm — **bắt buộc**, không được để trống
- **Lưu ý quan trọng:**
  - Người tạo nhóm (lấy từ token đăng nhập) **tự động được thêm** vào nhóm với vai trò `LEADER` — không cần khai báo trong `memberUserIds`.
  - Các thành viên trong `memberUserIds` có vai trò `MEMBER`.
  - Tất cả thành viên (kể cả leader) phải **chưa thuộc nhóm nào** trong cùng kỳ đăng ký, nếu không sẽ báo lỗi `STUDENT_ALREADY_IN_GROUP`.
- **Response (201):** `ApiResponse<StudentGroupResponse>`
- **Ví dụ response:**
```json
{
  "code": 201,
  "message": "Tạo nhóm thành công",
  "data": {
    "id": 1,
    "registrationPeriodId": 1,
    "name": "Nhóm 1 - Khóa luận",
    "status": true,
    "createdBy": "sv001",
    "members": [
      { "userId": 1, "username": "sv001", "studentCode": "20110001", "role": "LEADER", "joinedAt": "2026-09-01T14:00:00" },
      { "userId": 2, "username": "sv002", "studentCode": "20110002", "role": "MEMBER", "joinedAt": "2026-09-01T14:00:00" }
    ]
  }
}
```

## 8.2 Danh sách tất cả nhóm
- **`GET /api/student-groups`** — ADMIN, LECTURER
- **Response (200):** `ApiResponse<List<StudentGroupResponse>>`
- **Ví dụ response:**
```json
{
  "code": 200,
  "message": "Lấy danh sách nhóm thành công",
  "data": [
    {
      "id": 1,
      "registrationPeriodId": 1,
      "name": "Nhóm 1 - Khóa luận",
      "status": true,
      "createdBy": "sv001",
      "members": [
        { "userId": 1, "username": "sv001", "studentCode": "20110001", "role": "LEADER", "joinedAt": "2026-09-01T14:00:00" }
      ]
    },
    {
      "id": 2,
      "registrationPeriodId": 1,
      "name": "Nhóm 2 - Khóa luận",
      "status": true,
      "createdBy": "sv003",
      "members": [
        { "userId": 3, "username": "sv003", "studentCode": "20110003", "role": "LEADER", "joinedAt": "2026-09-01T15:00:00" }
      ]
    }
  ]
}
```

## 8.3 Danh sách nhóm theo kỳ đăng ký
- **`GET /api/student-groups/registration-period/{registrationPeriodId}`** — ADMIN, LECTURER
- **Path param:** `registrationPeriodId` (Long)
- **Response (200):** `ApiResponse<List<StudentGroupResponse>>` (chỉ các nhóm thuộc kỳ đăng ký đó)
- **Ví dụ response:** giống 8.2, nhưng `data` chỉ chứa các nhóm có `registrationPeriodId` khớp với path param.

## 8.4 Chi tiết nhóm
- **`GET /api/student-groups/{id}`** — ADMIN, LECTURER
- **Path param:** `id` (Long)
- **Response (200):** `ApiResponse<StudentGroupResponse>`
- **Ví dụ response:**
```json
{
  "code": 200,
  "message": "Lấy chi tiết nhóm thành công",
  "data": {
    "id": 1,
    "registrationPeriodId": 1,
    "name": "Nhóm 1 - Khóa luận",
    "status": true,
    "createdBy": "sv001",
    "members": [
      { "userId": 1, "username": "sv001", "studentCode": "20110001", "role": "LEADER", "joinedAt": "2026-09-01T14:00:00" },
      { "userId": 2, "username": "sv002", "studentCode": "20110002", "role": "MEMBER", "joinedAt": "2026-09-01T14:00:00" }
    ]
  }
}
```

## 8.5 Xóa nhóm
- **`DELETE /api/student-groups/{id}`** — ADMIN
- **Path param:** `id` (Long)
- **Response (200):** `ApiResponse<Void>` (`data` = null)
- **Ví dụ response:**
```json
{
  "code": 200,
  "message": "Xóa nhóm thành công",
  "data": null
}
```

## 📄 Cấu trúc `StudentGroupResponse`

### Bảng kiểu dữ liệu trả ra

| Field | Kiểu dữ liệu | Mô tả |
|-------|-------------|-------|
| `id` | `Long` | ID của nhóm |
| `registrationPeriodId` | `Long` | ID kỳ đăng ký mà nhóm thuộc về |
| `name` | `String` | Tên nhóm |
| `status` | `boolean` | Trạng thái nhóm (`true` = hoạt động, `false` = đã đóng) |
| `createdBy` | `String` | Username của người tạo nhóm |
| `members` | `List<GroupMemberResponse>` | Danh sách thành viên trong nhóm |

### Cấu trúc `GroupMemberResponse`

| Field | Kiểu dữ liệu | Mô tả |
|-------|-------------|-------|
| `userId` | `Long` | ID của tài khoản sinh viên |
| `username` | `String` | Username đăng nhập của sinh viên |
| `studentCode` | `String` | Mã số sinh viên |
| `role` | `GroupMemberRole` (enum) | Vai trò trong nhóm: `LEADER` hoặc `MEMBER` |
| `joinedAt` | `LocalDateTime` | Thời gian thành viên tham gia nhóm (định dạng ISO-8601, ví dụ `2026-09-01T14:00:00`) |

### Ví dụ response đầy đủ
```json
{
  "code": 200,
  "message": "Lấy danh sách nhóm thành công",
  "data": {
    "id": 1,
    "registrationPeriodId": 1,
    "name": "Nhóm 1 - Khóa luận",
    "status": true,
    "createdBy": "sv001",
    "members": [
      {
        "userId": 1,
        "username": "sv001",
        "studentCode": "20110001",
        "role": "LEADER",
        "joinedAt": "2026-09-01T14:00:00"
      },
      {
        "userId": 2,
        "username": "sv002",
        "studentCode": "20110002",
        "role": "MEMBER",
        "joinedAt": "2026-09-01T14:00:00"
      }
    ]
  }
}
```
- **Giải thích field:**
  - `status`: trạng thái nhóm (`true` = hoạt động)
  - `createdBy`: username của người tạo nhóm
  - `members`: danh sách thành viên trong nhóm
  - `role`: vai trò thành viên — `LEADER` (trưởng nhóm) hoặc `MEMBER` (thành viên)
  - `joinedAt`: thời gian thành viên tham gia nhóm

---

# 9. 📋 Đăng ký đề tài — `/api/topic-registrations`

| Method | Endpoint | Role | Mô tả |
|--------|----------|------|-------|
| POST | `/api/topic-registrations` | STUDENT | Đăng ký đề tài cho nhóm |
| GET | `/api/topic-registrations` | ADMIN, LECTURER | Danh sách tất cả đăng ký đề tài |
| GET | `/api/topic-registrations/{id}` | ADMIN, LECTURER, STUDENT | Chi tiết đăng ký theo id |
| GET | `/api/topic-registrations/student-group/{studentGroupId}` | ADMIN, LECTURER | Danh sách đăng ký theo nhóm sinh viên |
| GET | `/api/topic-registrations/topic/{topicId}` | ADMIN, LECTURER | Danh sách đăng ký theo đề tài |
| PATCH | `/api/topic-registrations/{id}/approve` | ADMIN, LECTURER | Duyệt đăng ký đề tài |
| PATCH | `/api/topic-registrations/{id}/reject` | ADMIN, LECTURER | Từ chối đăng ký đề tài |
| PATCH | `/api/topic-registrations/{id}/cancel` | STUDENT | Hủy đăng ký đề tài |

## 9.1 Đăng ký đề tài
- **`POST /api/topic-registrations`** — STUDENT
- **Body:**
```json
{
  "studentGroupId": 1,
  "topicId": 5
}
```
- **Giải thích field:**
  - `studentGroupId`: ID nhóm sinh viên — **bắt buộc**, phải là số dương
  - `topicId`: ID đề tài — **bắt buộc**, phải là số dương
- **Lưu ý quan trọng:**
  - Người đăng ký (lấy từ token đăng nhập) phải là thành viên của nhóm sinh viên.
  - Đề tài phải ở trạng thái `PUBLISHED` (đã công bố), nếu không sẽ báo lỗi `TOPIC_NOT_PUBLISHED`.
  - Nhóm sinh viên phải đang hoạt động (`status = true`), nếu không sẽ báo lỗi `STUDENT_GROUP_INACTIVE`.
  - Nhóm sinh viên **chưa đăng ký đề tài nào** trong kỳ đăng ký hiện tại, nếu không sẽ báo lỗi `STUDENT_GROUP_ALREADY_REGISTERED`.
  - Đề tài **chưa có nhóm nào được duyệt**, nếu không sẽ báo lỗi `TOPIC_ALREADY_APPROVED`.
- **Response (201):** `ApiResponse<TopicRegistrationResponse>`
- **Ví dụ response:**
```json
{
  "code": 201,
  "message": "Đăng ký đề tài thành công",
  "data": {
    "id": 1,
    "studentGroupId": 1,
    "studentGroupName": "Nhóm 1 - Khóa luận",
    "topicId": 5,
    "topicTitle": "Xây dựng hệ thống quản lý thư viện",
    "status": "PENDING",
    "registeredBy": "sv001",
    "registeredAt": "2026-09-01T14:00:00",
    "decidedBy": null,
    "rejectionReason": null
  }
}
```

## 9.2 Danh sách tất cả đăng ký đề tài
- **`GET /api/topic-registrations`** — ADMIN, LECTURER
- **Response (200):** `ApiResponse<List<TopicRegistrationResponse>>`
- **Ví dụ response:**
```json
{
  "code": 200,
  "message": "Lấy danh sách đăng ký đề tài thành công",
  "data": [
    {
      "id": 1,
      "studentGroupId": 1,
      "studentGroupName": "Nhóm 1 - Khóa luận",
      "topicId": 5,
      "topicTitle": "Xây dựng hệ thống quản lý thư viện",
      "status": "PENDING",
      "registeredBy": "sv001",
      "registeredAt": "2026-09-01T14:00:00",
      "decidedBy": null,
      "rejectionReason": null
    },
    {
      "id": 2,
      "studentGroupId": 2,
      "studentGroupName": "Nhóm 2 - Khóa luận",
      "topicId": 6,
      "topicTitle": "Ứng dụng AI nhận diện khuôn mặt",
      "status": "APPROVED",
      "registeredBy": "sv003",
      "registeredAt": "2026-09-01T15:00:00",
      "decidedBy": "gv001",
      "rejectionReason": null
    }
  ]
}
```

## 9.3 Chi tiết đăng ký đề tài
- **`GET /api/topic-registrations/{id}`** — ADMIN, LECTURER, STUDENT
- **Path param:** `id` (Long)
- **Response (200):** `ApiResponse<TopicRegistrationResponse>`
- **Ví dụ response:**
```json
{
  "code": 200,
  "message": "Lấy thông tin đăng ký đề tài thành công",
  "data": {
    "id": 1,
    "studentGroupId": 1,
    "studentGroupName": "Nhóm 1 - Khóa luận",
    "topicId": 5,
    "topicTitle": "Xây dựng hệ thống quản lý thư viện",
    "status": "PENDING",
    "registeredBy": "sv001",
    "registeredAt": "2026-09-01T14:00:00",
    "decidedBy": null,
    "rejectionReason": null
  }
}
```

## 9.4 Danh sách đăng ký theo nhóm sinh viên
- **`GET /api/topic-registrations/student-group/{studentGroupId}`** — ADMIN, LECTURER
- **Path param:** `studentGroupId` (Long)
- **Response (200):** `ApiResponse<List<TopicRegistrationResponse>>` (chỉ các đăng ký thuộc nhóm sinh viên đó)
- **Ví dụ response:** giống 9.2, nhưng `data` chỉ chứa các đăng ký có `studentGroupId` khớp với path param.

## 9.5 Danh sách đăng ký theo đề tài
- **`GET /api/topic-registrations/topic/{topicId}`** — ADMIN, LECTURER
- **Path param:** `topicId` (Long)
- **Response (200):** `ApiResponse<List<TopicRegistrationResponse>>` (chỉ các đăng ký thuộc đề tài đó)
- **Ví dụ response:** giống 9.2, nhưng `data` chỉ chứa các đăng ký có `topicId` khớp với path param.

## 9.6 Duyệt đăng ký đề tài
- **`PATCH /api/topic-registrations/{id}/approve`** — ADMIN, LECTURER
- **Path param:** `id` (Long)
- **Không cần body** — server tự lấy username từ token đăng nhập làm `decidedBy`
- **Lưu ý:** Chỉ duyệt được đăng ký ở trạng thái `PENDING`. Nếu không sẽ báo lỗi `INVALID_TOPIC_REGISTRATION_STATUS`.
- **Response (200):** `ApiResponse<TopicRegistrationResponse>` (trạng thái chuyển thành `APPROVED`)
- **Ví dụ response:**
```json
{
  "code": 200,
  "message": "Duyệt đăng ký đề tài thành công",
  "data": {
    "id": 1,
    "studentGroupId": 1,
    "studentGroupName": "Nhóm 1 - Khóa luận",
    "topicId": 5,
    "topicTitle": "Xây dựng hệ thống quản lý thư viện",
    "status": "APPROVED",
    "registeredBy": "sv001",
    "registeredAt": "2026-09-01T14:00:00",
    "decidedBy": "gv001",
    "rejectionReason": null
  }
}
```

## 9.7 Từ chối đăng ký đề tài
- **`PATCH /api/topic-registrations/{id}/reject`** — ADMIN, LECTURER
- **Path param:** `id` (Long)
- **Body:**
```json
{
  "rejectionReason": "Đề tài đã hết chỉ tiêu"
}
```
- **Giải thích field:**
  - `rejectionReason`: lý do từ chối — **bắt buộc**, không được để trống
- **Lưu ý:** Chỉ từ chối được đăng ký ở trạng thái `PENDING`. Nếu không sẽ báo lỗi `INVALID_TOPIC_REGISTRATION_STATUS`.
- **Response (200):** `ApiResponse<TopicRegistrationResponse>` (trạng thái chuyển thành `REJECTED`)
- **Ví dụ response:**
```json
{
  "code": 200,
  "message": "Từ chối đăng ký đề tài thành công",
  "data": {
    "id": 1,
    "studentGroupId": 1,
    "studentGroupName": "Nhóm 1 - Khóa luận",
    "topicId": 5,
    "topicTitle": "Xây dựng hệ thống quản lý thư viện",
    "status": "REJECTED",
    "registeredBy": "sv001",
    "registeredAt": "2026-09-01T14:00:00",
    "decidedBy": "gv001",
    "rejectionReason": "Đề tài đã hết chỉ tiêu"
  }
}
```

## 9.8 Hủy đăng ký đề tài
- **`PATCH /api/topic-registrations/{id}/cancel`** — STUDENT
- **Path param:** `id` (Long)
- **Không cần body** — server tự lấy username từ token đăng nhập
- **Lưu ý:** Chỉ hủy được đăng ký ở trạng thái `PENDING`. Nếu không sẽ báo lỗi `INVALID_TOPIC_REGISTRATION_STATUS`.
- **Response (200):** `ApiResponse<TopicRegistrationResponse>` (trạng thái chuyển thành `CANCELLED`)
- **Ví dụ response:**
```json
{
  "code": 200,
  "message": "Hủy đăng ký đề tài thành công",
  "data": {
    "id": 1,
    "studentGroupId": 1,
    "studentGroupName": "Nhóm 1 - Khóa luận",
    "topicId": 5,
    "topicTitle": "Xây dựng hệ thống quản lý thư viện",
    "status": "CANCELLED",
    "registeredBy": "sv001",
    "registeredAt": "2026-09-01T14:00:00",
    "decidedBy": null,
    "rejectionReason": null
  }
}
```

## 📄 Cấu trúc `TopicRegistrationResponse`

### Bảng kiểu dữ liệu trả ra

| Field | Kiểu dữ liệu | Mô tả |
|-------|-------------|-------|
| `id` | `Long` | ID của đăng ký đề tài |
| `studentGroupId` | `Long` | ID nhóm sinh viên đăng ký |
| `studentGroupName` | `String` | Tên nhóm sinh viên đăng ký |
| `topicId` | `Long` | ID đề tài được đăng ký |
| `topicTitle` | `String` | Tiêu đề đề tài được đăng ký |
| `status` | `TopicRegistrationStatus` (enum) | Trạng thái đăng ký: `PENDING`, `APPROVED`, `REJECTED`, `CANCELLED` |
| `registeredBy` | `String` | Username của người đăng ký |
| `registeredAt` | `LocalDateTime` | Thời gian đăng ký (định dạng ISO-8601, ví dụ `2026-09-01T14:00:00`) |
| `decidedBy` | `String` | Username của người duyệt/từ chối (null nếu chưa quyết định) |
| `rejectionReason` | `String` | Lý do từ chối (null nếu không bị từ chối) |

### Giải thích trạng thái `status`

| Giá trị | Mô tả |
|---------|-------|
| `PENDING` | Đang chờ duyệt (mặc định khi mới đăng ký) |
| `APPROVED` | Đã được duyệt |
| `REJECTED` | Đã bị từ chối (kèm `rejectionReason`) |
| `CANCELLED` | Đã bị hủy bởi sinh viên |

### Ví dụ response đầy đủ
```json
{
  "code": 200,
  "message": "Lấy danh sách đăng ký đề tài thành công",
  "data": {
    "id": 1,
    "studentGroupId": 1,
    "studentGroupName": "Nhóm 1 - Khóa luận",
    "topicId": 5,
    "topicTitle": "Xây dựng hệ thống quản lý thư viện",
    "status": "PENDING",
    "registeredBy": "sv001",
    "registeredAt": "2026-09-01T14:00:00",
    "decidedBy": null,
    "rejectionReason": null
  }
}
```
- **Giải thích field:**
  - `status`: trạng thái đăng ký đề tài
  - `registeredBy`: username của người đăng ký (thành viên nhóm)
  - `registeredAt`: thời gian đăng ký
  - `decidedBy`: username của người duyệt/từ chối (null khi chưa quyết định)
  - `rejectionReason`: lý do từ chối (null khi không bị từ chối)

---

# 10. 📤 Bài nộp — `/api/submissions`

> Module quản lý **bài nộp** (submission) của nhóm sinh viên sau khi đăng ký đề tài được duyệt. Mỗi đăng ký đề tài đã duyệt (`APPROVED`) có **một** bài nộp duy nhất, và mỗi bài nộp có thể chứa nhiều **phiên bản báo cáo** (`SubmissionVersion`).

| Method | Endpoint | Role | Mô tả |
|--------|----------|------|-------|
| POST | `/api/submissions` | STUDENT | Tạo bài nộp cho đăng ký đề tài |
| GET | `/api/submissions` | ADMIN, LECTURER | Lấy danh sách tất cả bài nộp |
| GET | `/api/submissions/{id}` | ADMIN, LECTURER, STUDENT | Lấy chi tiết bài nộp theo id |
| GET | `/api/submissions/topic-registration/{topicRegistrationId}` | ADMIN, LECTURER, STUDENT | Lấy bài nộp theo đăng ký đề tài |
| POST | `/api/submissions/feedbacks` | LECTURER | Tạo góp ý (feedback) cho phiên bản bài nộp |
| GET | `/api/submissions/versions/{submissionVersionId}/feedbacks` | ADMIN, LECTURER, STUDENT | Lấy danh sách góp ý theo phiên bản bài nộp |
| GET | `/api/submissions/feedbacks/me` | LECTURER | Lấy danh sách góp ý của giảng viên đang đăng nhập |

---

## 10.1 Tạo bài nộp

- **`POST /api/submissions`** — STUDENT
- **Body (JSON):**
```json
{
  "topicRegistrationId": 1
}
```
- **Giải thích field:**
  - `topicRegistrationId` (bắt buộc): ID của đăng ký đề tài đã được duyệt (`APPROVED`).
- **Lưu ý:**
  - Đăng ký đề tài phải tồn tại, nếu không → lỗi `TOPIC_REGISTRATION_NOT_FOUND`.
  - Đăng ký đề tài phải ở trạng thái `APPROVED`, nếu không → lỗi `TOPIC_REGISTRATION_NOT_APPROVED`.
  - Mỗi đăng ký đề tài chỉ có **một** bài nộp. Nếu đã có bài nộp → lỗi `SUBMISSION_ALREADY_EXISTS`.
  - Khi tạo, bài nộp có `status = true` và `submittedAt` = thời điểm hiện tại.
- **Response (201):**
```json
{
  "timestamp": "2026-09-06T10:00:00",
  "status": 201,
  "message": "Nộp phiên bản báo cáo thành công",
  "data": {
    "id": 1,
    "topicRegistrationId": 1,
    "status": true,
    "submittedAt": "2026-09-06T10:00:00",
    "submissionVersions": []
  }
}
```

---

## 10.2 Lấy danh sách bài nộp

- **`GET /api/submissions`** — ADMIN, LECTURER
- **Response (200):**
```json
{
  "timestamp": "2026-09-06T10:00:00",
  "status": 200,
  "message": "Lấy danh sách bài nộp thành công",
  "data": [
    {
      "id": 1,
      "topicRegistrationId": 1,
      "status": true,
      "submittedAt": "2026-09-06T10:00:00",
      "submissionVersions": [
        {
          "id": 1,
          "submissionId": 1,
          "fileName": "bao-cao-lan-1.pdf",
          "storageKey": "uploads/submission-1/bao-cao-lan-1.pdf",
          "mimeType": "application/pdf",
          "fileSize": 204800,
          "changeNote": "Bản báo cáo lần 1",
          "uploadedBy": "sv001@hcmute.edu.vn"
        }
      ]
    }
  ]
}
```

---

## 10.3 Lấy chi tiết bài nộp theo id

- **`GET /api/submissions/{id}`** — ADMIN, LECTURER, STUDENT
- **Path param:** `id` — ID bài nộp.
- **Lưu ý:** Nếu bài nộp không tồn tại → lỗi `SUBMISSION_NOT_FOUND`.
- **Response (200):** Tương tự cấu trúc `SubmissionResponse` bên dưới.

---

## 10.4 Lấy bài nộp theo đăng ký đề tài

- **`GET /api/submissions/topic-registration/{topicRegistrationId}`** — ADMIN, LECTURER, STUDENT
- **Path param:** `topicRegistrationId` — ID đăng ký đề tài.
- **Lưu ý:** Nếu không có bài nộp cho đăng ký đề tài này → lỗi `SUBMISSION_NOT_FOUND`.
- **Response (200):** Tương tự cấu trúc `SubmissionResponse` bên dưới.

---

## 10.5 Tạo góp ý cho phiên bản bài nộp

- **`POST /api/submissions/feedbacks`** — LECTURER
- **Body (JSON):**
```json
{
  "submissionVersionId": 1,
  "comment": "Cần bổ sung phần đánh giá kết quả thực nghiệm"
}
```
- **Giải thích field:**
  - `submissionVersionId` (bắt buộc): ID phiên bản bài nộp cần góp ý.
  - `comment` (bắt buộc): Nội dung góp ý của giảng viên.
- **Lưu ý:**
  - Giảng viên được xác định từ token đăng nhập (không cần truyền `lecturerId`).
  - Nếu giảng viên không tồn tại → lỗi `LECTURER_NOT_FOUND`.
  - Nếu phiên bản bài nộp không tồn tại → lỗi `SUBMISSION_VERSION_NOT_FOUND`.
- **Response (201):**
```json
{
  "timestamp": "2026-09-07T10:00:00",
  "status": 201,
  "message": "Góp ý bài nộp thành công",
  "data": {
    "id": 1,
    "submissionVersionId": 1,
    "lecturerUserId": 2,
    "lecturerEmail": "gv001@hcmute.edu.vn",
    "lecturerCode": "GV001",
    "comment": "Cần bổ sung phần đánh giá kết quả thực nghiệm"
  }
}
```

---

## 10.6 Lấy danh sách góp ý theo phiên bản bài nộp

- **`GET /api/submissions/versions/{submissionVersionId}/feedbacks`** — ADMIN, LECTURER, STUDENT
- **Path param:** `submissionVersionId` — ID phiên bản bài nộp.
- **Lưu ý:** Nếu phiên bản bài nộp không tồn tại → lỗi `SUBMISSION_VERSION_NOT_FOUND`.
- **Response (200):**
```json
{
  "timestamp": "2026-09-07T10:00:00",
  "status": 200,
  "message": "Lấy danh sách góp ý bài nộp thành công",
  "data": [
    {
      "id": 1,
      "submissionVersionId": 1,
      "lecturerUserId": 2,
      "lecturerEmail": "gv001@hcmute.edu.vn",
      "lecturerCode": "GV001",
      "comment": "Cần bổ sung phần đánh giá kết quả thực nghiệm"
    }
  ]
}
```

---

## 10.7 Lấy danh sách góp ý của giảng viên đang đăng nhập

- **`GET /api/submissions/feedbacks/me`** — LECTURER
- **Lưu ý:** Giảng viên được xác định từ token đăng nhập. Trả về tất cả góp ý mà giảng viên này đã viết.
- **Response (200):** Tương tự cấu trúc `SubmissionFeedbackResponse` bên dưới (dạng danh sách).

---

## 📄 Cấu trúc `SubmissionFeedbackResponse`

| Field | Kiểu dữ liệu | Mô tả |
|-------|--------------|-------|
| `id` | `Long` | ID góp ý |
| `submissionVersionId` | `Long` | ID phiên bản bài nộp được góp ý |
| `lecturerUserId` | `Long` | ID giảng viên viết góp ý |
| `lecturerEmail` | `String` | Email giảng viên viết góp ý |
| `lecturerCode` | `String` | Mã giảng viên viết góp ý |
| `comment` | `String` | Nội dung góp ý |

---

## 📄 Cấu trúc `SubmissionResponse`

### Bảng kiểu dữ liệu trả ra

| Field | Kiểu dữ liệu | Mô tả |
|-------|--------------|-------|
| `id` | `Long` | ID bài nộp |
| `topicRegistrationId` | `Long` | ID đăng ký đề tài tương ứng |
| `status` | `boolean` | Trạng thái bài nộp (`true` = đang hoạt động) |
| `submittedAt` | `LocalDateTime` | Thời gian tạo bài nộp (ISO-8601) |
| `submissionVersions` | `List<SubmissionVersionResponse>` | Danh sách phiên bản báo cáo |

### Cấu trúc `SubmissionVersionResponse`

| Field | Kiểu dữ liệu | Mô tả |
|-------|--------------|-------|
| `id` | `Long` | ID phiên bản |
| `submissionId` | `Long` | ID bài nộp chứa phiên bản |
| `fileName` | `String` | Tên file báo cáo |
| `storageKey` | `String` | Đường dẫn lưu file (storage) |
| `mimeType` | `String` | Loại MIME của file |
| `fileSize` | `Long` | Kích thước file (byte) |
| `changeNote` | `String` | Ghi chú thay đổi của phiên bản (có thể null) |
| `uploadedBy` | `String` | Email người nộp phiên bản |

### Ví dụ response đầy đủ
```json
{
  "timestamp": "2026-09-06T10:00:00",
  "status": 200,
  "message": "Lấy thông tin bài nộp thành công",
  "data": {
    "id": 1,
    "topicRegistrationId": 1,
    "status": true,
    "submittedAt": "2026-09-06T10:00:00",
    "submissionVersions": [
      {
        "id": 1,
        "submissionId": 1,
        "fileName": "bao-cao-lan-1.pdf",
        "storageKey": "uploads/submission-1/bao-cao-lan-1.pdf",
        "mimeType": "application/pdf",
        "fileSize": 204800,
        "changeNote": "Bản báo cáo lần 1",
        "uploadedBy": "sv001@hcmute.edu.vn"
      }
    ]
  }
}
```
- **Giải thích field:**
  - `status`: trạng thái bài nộp (`true` = đang hoạt động)
  - `submittedAt`: thời gian tạo bài nộp
  - `submissionVersions`: danh sách các phiên bản báo cáo đã nộp
  - `uploadedBy`: email của người nộp phiên bản

---

# 11. 🛡️ Hội đồng bảo vệ — `/api/defense-committees`

> Module quản lý **hội đồng bảo vệ** (defense committee). Mỗi hội đồng thuộc một kỳ đăng ký và gồm nhiều **thành viên** (`CommitteeMember`) là giảng viên, mỗi người giữ một vai trò trong hội đồng.

| Method | Endpoint | Role | Mô tả |
|--------|----------|------|-------|
| POST | `/api/defense-committees` | ADMIN | Tạo hội đồng bảo vệ |
| POST | `/api/defense-committees/{id}/members` | ADMIN | Thêm thành viên vào hội đồng |
| GET | `/api/defense-committees` | ADMIN, LECTURER | Lấy danh sách tất cả hội đồng |
| GET | `/api/defense-committees/{id}` | ADMIN, LECTURER | Lấy chi tiết hội đồng theo id |
| GET | `/api/defense-committees/registration-period/{registrationPeriodId}` | ADMIN, LECTURER | Lấy danh sách hội đồng theo kỳ đăng ký |
| GET | `/api/defense-committees/me` | LECTURER | Lấy danh sách hội đồng mà giảng viên đang đăng nhập thuộc về |
| PATCH | `/api/defense-committees/{id}/close` | ADMIN | Đóng hội đồng |

---

## 11.1 Tạo hội đồng bảo vệ

- **`POST /api/defense-committees`** — ADMIN
- **Body (JSON):**
```json
{
  "registrationPeriodId": 1,
  "code": "HD01",
  "name": "Hội đồng bảo vệ số 1"
}
```
- **Giải thích field:**
  - `registrationPeriodId` (bắt buộc): ID kỳ đăng ký mà hội đồng thuộc về.
  - `code` (bắt buộc, tối đa 50 ký tự): mã hội đồng, **không được trùng**.
  - `name` (bắt buộc, tối đa 100 ký tự): tên hội đồng.
- **Lưu ý:**
  - Kỳ đăng ký phải tồn tại, nếu không → lỗi `REGISTRATION_PERIOD_NOT_FOUND`.
  - Mã hội đồng không được trùng, nếu trùng → lỗi `DEFENSE_COMMITTEE_CODE_EXISTS`.
  - Người tạo được xác định từ token đăng nhập; `createdBy` lưu email người tạo.
  - Hội đồng mới tạo có `status = ACTIVE` và chưa có thành viên.
- **Response (201):**
```json
{
  "timestamp": "2026-09-15T10:00:00",
  "status": 201,
  "message": "Tạo hội đồng thành công",
  "data": {
    "id": 1,
    "registrationPeriodId": 1,
    "code": "HD01",
    "name": "Hội đồng bảo vệ số 1",
    "status": "ACTIVE",
    "createdBy": "admin@hcmute.edu.vn",
    "committeeMembers": []
  }
}
```

---

## 11.2 Thêm thành viên vào hội đồng

- **`POST /api/defense-committees/{id}/members`** — ADMIN
- **Path param:** `id` — ID hội đồng.
- **Body (JSON):**
```json
{
  "lecturerId": 5,
  "committeeRole": "CHAIRMAN"
}
```
- **Giải thích field:**
  - `lecturerId` (bắt buộc): ID giảng viên được thêm vào hội đồng.
  - `committeeRole` (bắt buộc): vai trò trong hội đồng — `MEMBER`, `REVIEWER`, `CHAIRMAN`, `SECRETARY`.
- **Lưu ý:**
  - Hội đồng phải tồn tại, nếu không → lỗi `DEFENSE_COMMITTEE_NOT_FOUND`.
  - Hội đồng ở trạng thái `CLOSED` không thể thêm thành viên → lỗi `DEFENSE_COMMITTEE_CLOSED`.
  - Giảng viên phải tồn tại, nếu không → lỗi `LECTURER_NOT_FOUND`.
  - Một giảng viên không thể thuộc cùng một hội đồng hai lần → lỗi `COMMITTEE_MEMBER_ALREADY_EXISTS`.
  - `joinedAt` được gán tự động bằng thời điểm hiện tại.
- **Response (201):**
```json
{
  "timestamp": "2026-09-15T10:05:00",
  "status": 201,
  "message": "Thêm thành viên hội đồng thành công",
  "data": {
    "id": 1,
    "registrationPeriodId": 1,
    "code": "HD01",
    "name": "Hội đồng bảo vệ số 1",
    "status": "ACTIVE",
    "createdBy": "admin@hcmute.edu.vn",
    "committeeMembers": [
      {
        "id": 1,
        "lecturerId": 5,
        "lecturerCode": "GV001",
        "lecturerEmail": "gv001@hcmute.edu.vn",
        "committeeRole": "CHAIRMAN",
        "joinedAt": "2026-09-15T10:05:00"
      }
    ]
  }
}
```

---

## 11.3 Lấy danh sách tất cả hội đồng

- **`GET /api/defense-committees`** — ADMIN, LECTURER
- **Response (200):**
```json
{
  "timestamp": "2026-09-15T10:10:00",
  "status": 200,
  "message": "Lấy danh sách hội đồng thành công",
  "data": [
    {
      "id": 1,
      "registrationPeriodId": 1,
      "code": "HD01",
      "name": "Hội đồng bảo vệ số 1",
      "status": "ACTIVE",
      "createdBy": "admin@hcmute.edu.vn",
      "committeeMembers": []
    }
  ]
}
```

---

## 11.4 Lấy chi tiết hội đồng theo id

- **`GET /api/defense-committees/{id}`** — ADMIN, LECTURER
- **Path param:** `id` — ID hội đồng.
- **Lưu ý:** Nếu hội đồng không tồn tại → lỗi `DEFENSE_COMMITTEE_NOT_FOUND`.
- **Response (200):** Tương tự cấu trúc `DefenseCommitteeResponse` bên dưới.

---

## 11.5 Lấy danh sách hội đồng theo kỳ đăng ký

- **`GET /api/defense-committees/registration-period/{registrationPeriodId}`** — ADMIN, LECTURER
- **Path param:** `registrationPeriodId` — ID kỳ đăng ký.
- **Response (200):** Danh sách `DefenseCommitteeResponse` thuộc kỳ đăng ký đó.

---

## 11.6 Lấy danh sách hội đồng của giảng viên đang đăng nhập

- **`GET /api/defense-committees/me`** — LECTURER
- **Lưu ý:**
  - Giảng viên được xác định từ token đăng nhập (không cần truyền id).
  - Nếu giảng viên không tồn tại → lỗi `LECTURER_NOT_FOUND`.
  - Trả về danh sách **thành viên hội đồng** (`CommitteeMemberResponse`) mà giảng viên này thuộc về.
- **Response (200):**
```json
{
  "timestamp": "2026-09-15T10:15:00",
  "status": 200,
  "message": "Lấy thông tin hội đồng thành công",
  "data": [
    {
      "id": 1,
      "lecturerId": 5,
      "lecturerCode": "GV001",
      "lecturerEmail": "gv001@hcmute.edu.vn",
      "committeeRole": "CHAIRMAN",
      "joinedAt": "2026-09-15T10:05:00"
    }
  ]
}
```

---

## 11.7 Đóng hội đồng

- **`PATCH /api/defense-committees/{id}/close`** — ADMIN
- **Path param:** `id` — ID hội đồng.
- **Lưu ý:**
  - Hội đồng phải tồn tại, nếu không → lỗi `DEFENSE_COMMITTEE_NOT_FOUND`.
  - Sau khi đóng, `status` chuyển thành `CLOSED` và **không thể thêm thành viên** nữa.
- **Response (200):**
```json
{
  "timestamp": "2026-09-15T10:20:00",
  "status": 200,
  "message": "Đóng hội đồng thành công",
  "data": {
    "id": 1,
    "registrationPeriodId": 1,
    "code": "HD01",
    "name": "Hội đồng bảo vệ số 1",
    "status": "CLOSED",
    "createdBy": "admin@hcmute.edu.vn",
    "committeeMembers": []
  }
}
```
---

# X. 🏛️ Phân công hội đồng chấm đề tài — `/api/committee-topic-assignments`

> Gán một **đăng ký đề tài đã duyệt** cho một **hội đồng bảo vệ**, kèm lịch + phòng bảo vệ.
> Mỗi đăng ký đề tài chỉ được phân công cho **đúng một** hội đồng.
> Module **phân công hội đồng chấm đề tài** (committee topic assignment). Gán một đăng ký đề tài đã duyệt cho một hội đồng bảo vệ, kèm lịch + phòng. Mỗi đăng ký đề tài chỉ được phân công cho đúng một hội đồng.

| Method | Endpoint | Role | Mô tả | Trạng thái |
|--------|----------|------|-------|:----------:|
| POST | `/api/committee-topic-assignments` | ADMIN | Phân công đề tài cho hội đồng | ✅ |
| PUT | `/api/committee-topic-assignments/{id}/schedule` | ADMIN | Cập nhật lịch + phòng bảo vệ | ✅ |
| PATCH | `/api/committee-topic-assignments/{id}/complete` | ADMIN | Xác nhận đã bảo vệ xong | ❌ |
| PATCH | `/api/committee-topic-assignments/{id}/cancel` | ADMIN | Huỷ phân công | ❌ |
| GET | `/api/committee-topic-assignments` | ADMIN, LECTURER | Lấy danh sách tất cả phân công | ❌ |
| GET | `/api/committee-topic-assignments/{id}` | ADMIN, LECTURER | Lấy chi tiết phân công theo id | ❌ |
| GET | `/api/committee-topic-assignments/defense-committee/{defenseCommitteeId}` | ADMIN, LECTURER | Danh sách đề tài mà một hội đồng chấm | ❌ |
| GET | `/api/committee-topic-assignments/registration-period/{registrationPeriodId}` | ADMIN, LECTURER | Danh sách phân công theo kỳ đăng ký | ❌ |
| GET | `/api/committee-topic-assignments/me` | LECTURER | Lịch chấm của các hội đồng giảng viên tham gia | ❌ |
| GET | `/api/committee-topic-assignments/my-group` | STUDENT | Lịch bảo vệ của nhóm sinh viên | ❌ |


## X.1 Tạo phân công (gán đề tài cho hội đồng)
- **`POST /api/committee-topic-assignments`** — Role: `ADMIN`
- **Body (JSON):**
```json
{
  "defenseCommitteeId": 1,
  "topicRegistrationId": 1,
  "scheduledStart": "2026-12-20T08:00:00",
  "scheduledEnd": "2026-12-20T09:00:00",
  "room": "A5-101"
}
```
- **Giải thích field:**

| Field | Kiểu | Bắt buộc | Mô tả |
|-------|------|----------|-------|
| `defenseCommitteeId` | `Long` | ✅ | ID hội đồng bảo vệ |
| `topicRegistrationId` | `Long` | ✅ | ID đăng ký đề tài (phải ở trạng thái `APPROVED`) |
| `scheduledStart` | `LocalDateTime` | ❌ | Giờ bắt đầu bảo vệ. Bỏ trống nếu phân công trước, xếp lịch sau |
| `scheduledEnd` | `LocalDateTime` | ❌ | Giờ kết thúc bảo vệ |
| `room` | `String` | ❌ | Phòng bảo vệ (tối đa 100 ký tự) |

- **Điều kiện phân công hợp lệ:**
  - Hội đồng tồn tại, **chưa `CLOSED`**, và **đã có thành viên vai trò `CHAIRMAN`**.
  - Đăng ký đề tài tồn tại và đang `APPROVED`.
  - Hội đồng và đề tài **cùng một kỳ đăng ký**.
  - Đăng ký đề tài chưa từng được phân công.
  - Giảng viên hướng dẫn của đề tài **không** nằm trong hội đồng chấm.
  - Nếu có nhập lịch: `scheduledEnd` sau `scheduledStart`, và `scheduledStart` **không sớm hơn `defense_date` của kỳ**; phòng không trùng giờ với lịch khác.
- **Response (201):** `CommitteeTopicAssignmentResponse`

## X.2 Cập nhật lịch bảo vệ (giờ + phòng)
- **`PUT /api/committee-topic-assignments/{id}/schedule`** — Role: `ADMIN`
- **Body (JSON):** (cả 3 field **bắt buộc**)
```json
{
  "scheduledStart": "2026-12-20T08:00:00",
  "scheduledEnd": "2026-12-20T09:00:00",
  "room": "A5-101"
}
```
- **Điều kiện:** phân công phải đang ở trạng thái `ASSIGNED`; ràng buộc thời gian & phòng giống mục X.1.
- **Response (200):** `CommitteeTopicAssignmentResponse`

## X.3 Cấu trúc `CommitteeTopicAssignmentResponse`

| Field | Kiểu | Mô tả |
|-------|------|-------|
| `id` | `Long` | ID phân công |
| `defenseCommitteeId` | `Long` | ID hội đồng |
| `defenseCommitteeCode` | `String` | Mã hội đồng |
| `defenseCommitteeName` | `String` | Tên hội đồng |
| `registrationPeriodId` | `Long` | ID kỳ đăng ký |
| `topicRegistrationId` | `Long` | ID đăng ký đề tài |
| `topicId` | `Long` | ID đề tài |
| `topicTitle` | `String` | Tên đề tài |
| `studentGroupId` | `Long` | ID nhóm sinh viên |
| `studentGroupName` | `String` | Tên nhóm sinh viên |
| `scheduledStart` | `LocalDateTime` | Giờ bắt đầu bảo vệ (ISO-8601) |
| `scheduledEnd` | `LocalDateTime` | Giờ kết thúc bảo vệ (ISO-8601) |
| `room` | `String` | Phòng bảo vệ |
| `status` | `CommitteeTopicAssignmentStatus` | Trạng thái phân công |
| `assignedBy` | `String` | Email người phân công |
| `assignedAt` | `LocalDateTime` | Thời gian phân công (ISO-8601) |

## X.4 Enum `CommitteeTopicAssignmentStatus`

| Giá trị | Mô tả |
|---------|-------|
| `ASSIGNED` | Đã phân công, đang chờ bảo vệ |
| `COMPLETED` | Đã bảo vệ xong |
| `CANCELED` | Đã huỷ phân công |

## X.5 Các lỗi thường gặp (HTTP 409)

| Message | Nguyên nhân |
|---------|-------------|
| `Hội đồng không tồn tại` | `defenseCommitteeId` sai |
| `Hội đồng đã đóng` | Hội đồng đang `CLOSED` |
| `Hội đồng chưa có chủ tịch` | Hội đồng chưa có thành viên `CHAIRMAN` |
| `Đăng ký đề tài không tồn tại` | `topicRegistrationId` sai |
| `Đăng ký đề tài chưa được duyệt` | Đăng ký chưa `APPROVED` |
| `Nhóm này đã được phân công hội đồng` | Đăng ký đề tài đã được phân công trước đó |
| `Hội đồng và đề tài không cùng kỳ đăng ký` | Lệch `registration_period_id` |
| `Giảng viên hướng dẫn không được chấm đề tài mình hướng dẫn` | GVHD nằm trong hội đồng |
| `Thời gian bảo vệ không được trước ngày bảo vệ của kỳ đăng ký` | `scheduledStart` sớm hơn `defense_date`, hoặc `end` không sau `start` |
| `Phòng đã có lịch bảo vệ vào thời điểm này` | Trùng phòng + trùng giờ |
| `Trạng thái phân công không hợp lệ` | Cập nhật lịch khi phân công không còn `ASSIGNED` |



---

## 📄 Cấu trúc `DefenseCommitteeResponse`

| Field | Kiểu dữ liệu | Mô tả |
|-------|--------------|-------|
| `id` | `Long` | ID hội đồng |
| `registrationPeriodId` | `Long` | ID kỳ đăng ký mà hội đồng thuộc về |
| `code` | `String` | Mã hội đồng (duy nhất) |
| `name` | `String` | Tên hội đồng |
| `status` | `DefenseCommitteeStatus` | Trạng thái hội đồng |
| `createdBy` | `String` | Email người tạo hội đồng |
| `committeeMembers` | `List<CommitteeMemberResponse>` | Danh sách thành viên hội đồng |

### Cấu trúc `CommitteeMemberResponse`

| Field | Kiểu dữ liệu | Mô tả |
|-------|--------------|-------|
| `id` | `Long` | ID thành viên hội đồng |
| `lecturerId` | `Long` | ID giảng viên |
| `lecturerCode` | `String` | Mã giảng viên |
| `lecturerEmail` | `String` | Email giảng viên |
| `committeeRole` | `CommitteeRole` | Vai trò trong hội đồng |
| `joinedAt` | `LocalDateTime` | Thời gian tham gia hội đồng (ISO-8601) |

### Các giá trị enum

**`DefenseCommitteeStatus`** — trạng thái hội đồng:

| Giá trị | Mô tả |
|---------|-------|
| `ACTIVE` | Đang hoạt động |
| `INACTIVE` | Ngừng hoạt động |
| `CLOSED` | Đã đóng (không thể thêm thành viên) |

**`CommitteeRole`** — vai trò thành viên hội đồng:

| Giá trị | Mô tả |
|---------|-------|
| `MEMBER` | Thành viên |
| `REVIEWER` | Phản biện |
| `CHAIRMAN` | Chủ tịch hội đồng |
| `SECRETARY` | Thư ký |

---

# 🧭 Tóm tắt nhanh cho FE

| Chức năng | Endpoint | Role |
|-----------|----------|------|
| Đăng nhập | `POST /api/auth/login` | Public |
| Đăng ký SV | `POST /api/auth/register/student` | Public |
| Đăng ký GV | `POST /api/auth/register/lecturer` | Public |
| Đăng ký Admin | `POST /api/auth/register/admin` | Public |
| Refresh token | `POST /api/auth/refresh-token` | Public |
| CRUD Sinh viên | `/api/students` | ADMIN / LECTURER / STUDENT |
| CRUD Giảng viên | `/api/lecturers` | ADMIN / LECTURER |
| CRUD Khoa | `/api/departments` | ADMIN |
| CRUD Chuyên môn | `/api/expertise-tags` | ADMIN |
| CRUD Kỳ đăng ký | `/api/registration-periods` | ADMIN |
| CRUD Đề tài | `/api/topics` | ADMIN / LECTURER / STUDENT |
| Nhóm sinh viên | `/api/student-groups` | ADMIN / LECTURER / STUDENT |
| Đăng ký đề tài | `/api/topic-registrations` | ADMIN / LECTURER / STUDENT |
| Bài nộp | `/api/submissions` | ADMIN / LECTURER / STUDENT |
| Góp ý bài nộp | `/api/submissions/feedbacks` | LECTURER |
| Hội đồng bảo vệ | `/api/defense-committees` | ADMIN / LECTURER |
| Phân công hội đồng chấm | `/api/committee-topic-assignments` | ADMIN |

---

---

# 🏛️ Phân công hội đồng chấm đề tài — `/api/committee-topic-assignments`

> Gán một **đăng ký đề tài đã duyệt** cho một **hội đồng bảo vệ**, kèm lịch + phòng. Mỗi đăng ký đề tài chỉ được phân công cho **đúng một** hội đồng.

## Tạo phân công
- **`POST /api/committee-topic-assignments`** — Role: `ADMIN`
```json
{
  "defenseCommitteeId": 1,
  "topicRegistrationId": 1,
  "scheduledStart": "2026-12-20T08:00:00",
  "scheduledEnd": "2026-12-20T09:00:00",
  "room": "A5-101"
}
```
| Field | Bắt buộc | Mô tả |
|-------|:--------:|-------|
| `defenseCommitteeId` | ✅ | ID hội đồng (phải chưa `CLOSED`, đã có `CHAIRMAN`) |
| `topicRegistrationId` | ✅ | ID đăng ký đề tài (phải `APPROVED`, cùng kỳ với hội đồng) |
| `scheduledStart` / `scheduledEnd` | ❌ | Lịch bảo vệ (bỏ trống nếu xếp sau). `end` > `start`, `start` ≥ `defense_date` của kỳ |
| `room` | ❌ | Phòng (≤100 ký tự), không trùng giờ với lịch khác |

- **Response (201):** `CommitteeTopicAssignmentResponse`

## Cập nhật lịch bảo vệ
- **`PUT /api/committee-topic-assignments/{id}/schedule`** — Role: `ADMIN` (cả 3 field bắt buộc; phân công phải đang `ASSIGNED`)
```json
{ "scheduledStart": "2026-12-20T08:00:00", "scheduledEnd": "2026-12-20T09:00:00", "room": "A5-101" }
```

## `CommitteeTopicAssignmentResponse`
| Field | Mô tả |
|-------|-------|
| `id` | ID phân công |
| `defenseCommitteeId` / `defenseCommitteeCode` / `defenseCommitteeName` | Hội đồng |
| `registrationPeriodId` | Kỳ đăng ký |
| `topicRegistrationId` / `topicId` / `topicTitle` | Đề tài |
| `studentGroupId` / `studentGroupName` | Nhóm SV |
| `scheduledStart` / `scheduledEnd` / `room` | Lịch + phòng bảo vệ |
| `status` | `ASSIGNED` / `COMPLETED` / `CANCELED` |
| `assignedBy` / `assignedAt` | Người & thời điểm phân công |

## Lỗi thường gặp (409)
| Message | Nguyên nhân |
|---------|-------------|
| `Hội đồng chưa có chủ tịch` | Hội đồng chưa có thành viên `CHAIRMAN` |
| `Nhóm này đã được phân công hội đồng` | Đăng ký đề tài đã được phân công |
| `Đăng ký đề tài chưa được duyệt` | Đăng ký chưa `APPROVED` |
| `Hội đồng và đề tài không cùng kỳ đăng ký` | Lệch kỳ đăng ký |
| `Giảng viên hướng dẫn không được chấm đề tài mình hướng dẫn` | GVHD nằm trong hội đồng |
| `Thời gian bảo vệ không được trước ngày bảo vệ của kỳ đăng ký` | `scheduledStart` sớm hơn `defense_date` hoặc `end` ≤ `start` |
| `Phòng đã có lịch bảo vệ vào thời điểm này` | Trùng phòng + trùng giờ |

> 🚧 Các API `complete`, `cancel`, và các `GET` đã có ở tầng service, chưa expose controller.



# 🚀 Hướng dẫn nhanh cho FE khi gọi API

1. **Đăng nhập** → `POST /api/auth/login` → lưu `accessToken` + `refreshToken` (localStorage/sessionStorage).
2. **Gắn token** vào mọi request cần xác thực:
   ```
   Authorization: Bearer <accessToken>
   ```
3. **Khi access token hết hạn** (HTTP 401) → gọi `POST /api/auth/refresh-token` với `refreshToken` để lấy token mới.
4. **Lấy hồ sơ của tôi** → dùng `GET /api/students/me` (hoặc `/api/lecturers/me`) — không cần truyền id, server tự lấy từ token.
5. **Phân quyền hiển thị UI** dựa trên `roles` trả về từ `LoginResponse`.
6. **Đề tài (Topics)**:
   - **Giảng viên** tạo đề tài → `POST /api/topics`, xem đề tài của mình → `GET /api/topics/me`.
   - **Admin** duyệt/từ chối/công bố đề tài → `PATCH /api/topics/{id}/approve|reject|publish`.
   - **Sinh viên** xem đề tài đã công bố → `GET /api/topics/published`.
7. **Nhóm sinh viên (Student Groups)**:
   - **Sinh viên** tạo nhóm → `POST /api/student-groups` (người tạo tự động là `LEADER`).
   - **Admin/Lecturer** xem danh sách nhóm → `GET /api/student-groups`, xem theo kỳ đăng ký → `GET /api/student-groups/registration-period/{id}`.
   - **Admin** xóa nhóm → `DELETE /api/student-groups/{id}`.
8. **Đăng ký đề tài (Topic Registrations)**:
   - **Sinh viên** đăng ký đề tài cho nhóm → `POST /api/topic-registrations` (đề tài phải ở trạng thái `PUBLISHED`, nhóm chưa đăng ký đề tài nào trong kỳ).
   - **Sinh viên** hủy đăng ký → `PATCH /api/topic-registrations/{id}/cancel` (chỉ khi đang `PENDING`).
   - **Admin/Lecturer** duyệt/từ chối đăng ký → `PATCH /api/topic-registrations/{id}/approve|reject` (chỉ khi đang `PENDING`).
   - **Admin/Lecturer** xem danh sách đăng ký → `GET /api/topic-registrations`, theo nhóm → `GET /api/topic-registrations/student-group/{id}`, theo đề tài → `GET /api/topic-registrations/topic/{id}`.
9. **Bài nộp (Submissions)**:
   - **Sinh viên** tạo bài nộp → `POST /api/submissions` (đăng ký đề tài phải ở trạng thái `APPROVED`, mỗi đăng ký chỉ có 1 bài nộp).
   - **Admin/Lecturer** xem danh sách bài nộp → `GET /api/submissions`.
   - **Admin/Lecturer/Student** xem chi tiết bài nộp → `GET /api/submissions/{id}`, xem theo đăng ký đề tài → `GET /api/submissions/topic-registration/{topicRegistrationId}`.
   - **Giảng viên** góp ý cho phiên bản bài nộp → `POST /api/submissions/feedbacks`, xem góp ý của mình → `GET /api/submissions/feedbacks/me`.
   - **Admin/Lecturer/Student** xem góp ý theo phiên bản bài nộp → `GET /api/submissions/versions/{submissionVersionId}/feedbacks`.

   > 🆕 **Kỳ đăng ký**: `type` giờ là enum `SUBJECT` | `NCKH` | `TLCN` | `KLTN`. `reviewerScoreDeadline` chỉ gửi khi `TLCN`/`KLTN`, `defenseDate` chỉ gửi khi `KLTN` — gửi thừa cho loại khác sẽ bị từ chối.

> 🆕 **Đề tài**: thêm field tùy chọn `coSupervisorId` khi tạo (`POST /api/topics`) để có GVHD thứ 2 (phải khác người tạo). Đề tài có 1–2 GVHD.

> 🆕 **Bài nộp**: `POST /api/submissions` chỉ cho **nhóm trưởng** nộp; SV khác → 409 "Chỉ nhóm trưởng mới được nộp báo cáo".

> 🆕 **Hội đồng bảo vệ**: thêm thành viên bị chặn nếu đủ 5 GV hoặc đã có chủ tịch/thư ký. Đóng hội đồng yêu cầu 3–5 GV, đúng 1 chủ tịch + 1 thư ký.

