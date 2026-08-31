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

> ⚠️ **Ngoại lệ:** `AuthController` và `DepartmentController` trả **trực tiếp** object (không bọc `ApiResponse`). Các module còn lại (students, lecturers, expertise-tags, registration-periods, topics) đều bọc `ApiResponse`.

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

---

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
