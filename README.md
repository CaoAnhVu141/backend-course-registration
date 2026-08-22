# Backend Course Registration

Backend cho hệ thống đăng ký môn học (Course Registration), xây dựng bằng **Spring Boot 4** và **Java 21**.

## 🚀 Công nghệ sử dụng

| Công nghệ | Phiên bản |
|-----------|-----------|
| Java | 21 |
| Spring Boot | 4.1.0 |
| Build tool | Gradle |
| Database | MySQL (đã cấu hình sẵn connector) |

## 📁 Cấu trúc dự án

Dự án áp dụng **kiến trúc module (Modular Monolith)** kết hợp **Clean Architecture**. Mỗi module nghiệp vụ được tách riêng, bên trong chia theo 4 tầng:

```
src/main/java/
├── com/example/backend_course_resgistration/
│   └── BackendCourseResgistrationApplication.java   # Class khởi động chính
└── modules/
    ├── demo/                                        # Module demo (trống)
    └── students/                                    # Module quản lý sinh viên
        ├── application/                             # Tầng ứng dụng (use cases, services)
        ├── domain/                                  # Tầng domain (entities, value objects)
        ├── infrastructure/                          # Tầng hạ tầng (repositories, DB)
        └── presentation/                            # Tầng trình bày (controllers, DTOs)
```

### Giải thích các tầng trong module

| Tầng | Vai trò |
|------|---------|
| `presentation` | Xử lý HTTP request/response, chứa các `@RestController`, DTO |
| `application` | Chứa use cases, business logic, orchestration |
| `domain` | Chứa entities, value objects, domain services (lõi nghiệp vụ) |
| `infrastructure` | Chứa repository implementation, kết nối database, external services |

## 🛠️ Cách chạy dự án

### Yêu cầu
- JDK 21
- Gradle (hoặc dùng `gradlew` wrapper có sẵn)

### Chạy ứng dụng

```bash
./gradlew bootRun
```

Ứng dụng khởi động tại: `http://localhost:8080`

### Build file JAR

```bash
./gradlew build
java -jar build/libs/backend_course_resgistration-0.0.1-SNAPSHOT.jar
```

## 🔗 API hiện có

| Method | Endpoint | Mô tả |
|--------|----------|-------|
| GET | `/students` | Test endpoint module students |

## 📝 Ghi chú

- Hiện tại dự án **chưa cấu hình database** (đã tạm gỡ Spring Data JPA). Khi cần dùng MySQL, thêm lại dependency `spring-boot-starter-data-jpa` và cấu hình trong `application.properties`.
- Các thư mục module mới nên đặt trong `src/main/java/modules/<tên_module>/` theo đúng 4 tầng trên.
