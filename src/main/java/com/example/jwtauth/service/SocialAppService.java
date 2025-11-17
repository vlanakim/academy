package com.example.jwtauth.service;

import com.example.jwtauth.model.Role;
import com.example.jwtauth.model.User;
import com.example.jwtauth.repo.UserRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class SocialAppService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UserRepository userRepository;
    private final String adminEmail;

    public SocialAppService(
            UserRepository userRepository,
            @Value("${app.oauth2.admin-email:}") String adminEmail
    ) {
        this.userRepository = userRepository;
        this.adminEmail = adminEmail;
    }

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        Map<String, Object> attributes = oAuth2User.getAttributes();
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        String email = (String) attributes.get("email");
        String name = (String) attributes.getOrDefault("name", email);

        log.info("OAuth2 login via {}: email={}, name={}", registrationId, email, name);

        User user = userRepository.findByUsername(email).orElseGet(() -> {
            User newUser = User.builder()
                    .username(email)
                    .password("N/A")
                    .role(Role.USER)
                    .accountNonLocked(true)
                    .failedAttempts(0)
                    .build();
            log.info("Creating new local user for email={}", email);
            return userRepository.save(newUser);
        });

        if (adminEmail != null && !adminEmail.isBlank() && adminEmail.equalsIgnoreCase(email)) {
            if (user.getRole() != Role.SUPER_ADMIN) {
                user.setRole(Role.SUPER_ADMIN);
                userRepository.save(user);
                log.info("User {} promoted to SUPER_ADMIN based on admin-email config", email);
            }
        }

        Collection<? extends GrantedAuthority> authorities =
                List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()));

        String nameAttributeKey = "sub";

        return new DefaultOAuth2User(
                authorities,
                attributes,
                nameAttributeKey
        );
    }
}