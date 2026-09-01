package modules.auth.security;

import lombok.RequiredArgsConstructor;
import modules.auth.entity.Role;
import modules.auth.entity.User;
import modules.auth.repository.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import modules.auth.enums.EnumAuthError;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

        private final UserRepository userRepository;

        @Override
        public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

                String login = username.trim();
                User user = userRepository.findByUsernameOrEmail(login, login.toLowerCase())
                                .orElseThrow(() -> new UsernameNotFoundException(
                                                EnumAuthError.USER_NOT_FOUND.getMessage()));

                List<SimpleGrantedAuthority> authorities = user.getRoles().stream()
                                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getName()))
                                .collect(Collectors.toList());

                return new org.springframework.security.core.userdetails.User(
                                user.getUsername(),
                                user.getPassword(),
                                user.isStatus(),
                                true, true, true,
                                authorities);
        }
}