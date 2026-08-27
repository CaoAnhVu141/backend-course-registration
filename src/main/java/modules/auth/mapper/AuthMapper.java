package modules.auth.mapper;

import org.springframework.stereotype.Component;
import modules.auth.dto.LoginResponse;
import modules.auth.entity.User;
import modules.auth.entity.Role;
import java.util.stream.Collectors;

@Component
public class AuthMapper {

    public LoginResponse loginResponseMapper(User user, String accessToken, String refreshToken, long expiresIn) {
        return LoginResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .expiresIn(expiresIn)
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .roles(user
                        .getRoles()
                        .stream()
                        .map(Role::getName)
                        .collect(Collectors.toSet()))
                .build();
    }

}
