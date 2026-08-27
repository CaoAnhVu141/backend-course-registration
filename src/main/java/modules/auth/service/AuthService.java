package modules.auth.service;

import lombok.RequiredArgsConstructor;
import modules.auth.dto.*;
import modules.auth.entity.RefreshToken;
import modules.auth.entity.Role;
import modules.auth.entity.User;
import modules.auth.repository.RefreshTokenRepository;
import modules.auth.repository.RoleRepository;
import modules.auth.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.stream.Collectors;
import global.security.JwtService;
import modules.auth.security.CustomUserDetailsService;
import modules.auth.mapper.AuthMapper;
import java.time.Duration;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService userDetailsService;
    private final AuthMapper authMapper;

    @Transactional
    public LoginResponse loginService(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());
        User user = userRepository.findByUsername(request.getUsername()).orElseThrow();

        String accessToken = jwtService.generateAccessToken(userDetails);
        String refreshToken = createRefreshToken(user);

        return authMapper.loginResponseMapper(user, accessToken, refreshToken,
                jwtService.getAccessTokenExpiration());
    }

    @Transactional
    public LoginResponse refreshToken(RefreshTokenRequest request) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(request.getRefreshToken())
                .orElseThrow(() -> new RuntimeException("Refresh token không hợp lệ"));

        if (refreshToken.isRevoked() || refreshToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Refresh token đã hết hạn hoặc bị thu hồi");
        }

        User user = refreshToken.getUser();
        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());

        String newAccessToken = jwtService.generateAccessToken(userDetails);
        String newRefreshToken = createRefreshToken(user);

        refreshToken.setRevoked(true);
        refreshTokenRepository.save(refreshToken);

        return authMapper.loginResponseMapper(user, newAccessToken, newRefreshToken,
                jwtService.getAccessTokenExpiration());
    }

    private String createRefreshToken(User user) {
        String token = UUID.randomUUID().toString();
        RefreshToken refreshToken = RefreshToken.builder()
                .token(token)
                .user(user)
                .expiryDate(LocalDateTime.now().plus(Duration.ofMillis(jwtService.getRefreshTokenExpiration())))
                .revoked(false)
                .build();
        refreshTokenRepository.save(refreshToken);
        return token;
    }
}
