package global.storage;

import java.io.IOException;
import java.nio.file.*;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileStorageService {

    private final Path root;

    public FileStorageService(@Value("${file.upload-dir}") String uploadDir) {
        this.root = Paths.get(uploadDir).toAbsolutePath().normalize();
        try {
            Files.createDirectories(root);
        } catch (IOException e) {
            throw new IllegalStateException("Không tạo được thư mục lưu file", e);
        }
    }

    // lưu file -> trả về storageKey (tên duy nhất trên đĩa)
    public String store(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File không được để trống");
        }
        String original = StringUtilsSafe(file.getOriginalFilename());
        String ext = "";
        int dot = original.lastIndexOf('.');
        if (dot >= 0) {
            ext = original.substring(dot);
        }
        String storageKey = UUID.randomUUID() + ext;
        try {
            Path target = root.resolve(storageKey).normalize();
            Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);
            return storageKey;
        } catch (IOException e) {
            throw new IllegalStateException("Lưu file thất bại", e);
        }
    }

    // đọc file để tải về
    public Resource loadAsResource(String storageKey) {
        try {
            Path file = root.resolve(storageKey).normalize();
            Resource resource = new UrlResource(file.toUri());
            if (!resource.exists() || !resource.isReadable()) {
                throw new IllegalStateException("File không tồn tại: " + storageKey);
            }
            return resource;
        } catch (IllegalStateException e) {
            throw e;
        } catch (Exception e) {
            throw new IllegalStateException("Không đọc được file: " + storageKey, e);
        }
    }

    private String StringUtilsSafe(String name) {
        if (name == null || name.isBlank()) {
            return "file";
        }
        return Paths.get(name).getFileName().toString();
    }
}
