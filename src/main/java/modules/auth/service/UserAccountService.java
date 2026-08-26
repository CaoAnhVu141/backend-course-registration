package modules.auth.service;

import lombok.RequiredArgsConstructor;
import modules.auth.entity.Role;
import modules.auth.entity.User;
import modules.auth.repository.RoleRepository;
import modules.auth.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserAccountService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public User create(String username, String email, String rawPassword, String roleName) {
        String normalizedUsername = username.trim();
        String normalizedEmail = email.trim().toLowerCase();
        if (userRepository.existsByUsername(normalizedUsername))
            throw new IllegalArgumentException("Username đã tồn tại");
        if (userRepository.existsByEmail(normalizedEmail))
            throw new IllegalArgumentException("Email đã tồn tại");

        Role role = roleRepository.findByName(roleName)
                .filter(Role::isStatus)
                .orElseThrow(() -> new IllegalStateException("Role " + roleName + " chưa được tạo hoặc đã bị khóa"));

        return userRepository.save(User.builder()
                .username(normalizedUsername)
                .email(normalizedEmail)
                .password(passwordEncoder.encode(rawPassword))
                .status(true)
                .roles(Set.of(role))
                .build());
    }
}
