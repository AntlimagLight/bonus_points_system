package ru.sberbank.bonus_points_system.service;

import jakarta.annotation.PostConstruct;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.sberbank.bonus_points_system.security.JwtAuthentication;
import ru.sberbank.bonus_points_system.security.JwtProvider;
import ru.sberbank.bonus_points_system.security.dto.JwtRequest;
import ru.sberbank.bonus_points_system.security.dto.JwtResponse;

import java.util.HashMap;
import java.util.Map;

import static ru.sberbank.bonus_points_system.config.WebSecurityConfig.PASSWORD_ENCODER;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserService userService;
    private final Map<String, String> refreshStorage = new HashMap<>(); //Подключить Redis вместо мапы по возможности
    private final JwtProvider jwtProvider;

    @PostConstruct
    public void linkUserService() {
        userService.registerAuthService(this);
    }

    public JwtResponse login(@NonNull JwtRequest authRequest) {
        val user = userService.getEntityByLogin(authRequest.getLogin());
        if (!user.getEnabled()) {
            throw new AccessDeniedException("access blocked");
        }
        if (PASSWORD_ENCODER.matches(authRequest.getPassword(), user.getPassword())) {
            val accessToken = jwtProvider.generateAccessToken(user);
            val refreshToken = jwtProvider.generateRefreshToken(user);
            refreshStorage.put(user.getLogin(), refreshToken);
            return new JwtResponse(accessToken, refreshToken);
        } else {
            throw new AccessDeniedException("incorrect password");
        }
    }

    public JwtResponse getAccessToken(@NonNull String refreshToken) {
        if (jwtProvider.validateRefreshToken(refreshToken)) {
            val claims = jwtProvider.getRefreshClaims(refreshToken);
            val login = claims.getSubject();
            val saveRefreshToken = refreshStorage.get(login);
            if (saveRefreshToken != null && saveRefreshToken.equals(refreshToken)) {
                val user = userService.getEntityByLogin(login);
                val accessToken = jwtProvider.generateAccessToken(user);
                return new JwtResponse(accessToken, null);
            }
        }
        return new JwtResponse(null, null);
    }

    public JwtResponse refresh(@NonNull String refreshToken) {
        if (jwtProvider.validateRefreshToken(refreshToken)) {
            val claims = jwtProvider.getRefreshClaims(refreshToken);
            val login = claims.getSubject();
            val saveRefreshToken = refreshStorage.get(login);
            if (saveRefreshToken != null && saveRefreshToken.equals(refreshToken)) {
                val user = userService.getEntityByLogin(login);
                val accessToken = jwtProvider.generateAccessToken(user);
                val newRefreshToken = jwtProvider.generateRefreshToken(user);
                refreshStorage.put(user.getLogin(), newRefreshToken);
                return new JwtResponse(accessToken, newRefreshToken);
            }
        }
        throw new AccessDeniedException("Invalid JWT token");
    }

    public void BlockAccess(String login) {
        refreshStorage.remove(login);
    }

    public JwtAuthentication getAuthInfo() {
        return (JwtAuthentication) SecurityContextHolder.getContext().getAuthentication();
    }

}