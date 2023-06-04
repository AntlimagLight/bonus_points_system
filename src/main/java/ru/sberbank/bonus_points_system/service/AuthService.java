package ru.sberbank.bonus_points_system.service;

import jakarta.security.auth.message.AuthException;
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

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserService userService;
    private final Map<String, String> refreshStorage = new HashMap<>(); //Подключить Redis вместо мапы по возможности
    private final JwtProvider jwtProvider;

    public JwtResponse login(@NonNull JwtRequest authRequest) {
        try {
            val user = userService.getByLogin(authRequest.getLogin())
                    .orElseThrow(() -> new AuthException("User not found"));
            if (user.getPassword().equals(authRequest.getPassword())) {
                val accessToken = jwtProvider.generateAccessToken(user);
                val refreshToken = jwtProvider.generateRefreshToken(user);
                refreshStorage.put(user.getLogin(), refreshToken);
                return new JwtResponse(accessToken, refreshToken);
            } else {
                throw new AuthException("incorrect password");
            }
        } catch (AuthException e) {
            throw new AccessDeniedException(e.getMessage());
        }
    }

    public JwtResponse getAccessToken(@NonNull String refreshToken) {
        try {
            if (jwtProvider.validateRefreshToken(refreshToken)) {
                val claims = jwtProvider.getRefreshClaims(refreshToken);
                val login = claims.getSubject();
                val saveRefreshToken = refreshStorage.get(login);
                if (saveRefreshToken != null && saveRefreshToken.equals(refreshToken)) {
                    val user = userService.getByLogin(login)
                            .orElseThrow(() -> new AuthException("User not found"));
                    val accessToken = jwtProvider.generateAccessToken(user);
                    return new JwtResponse(accessToken, null);
                }
            }
            return new JwtResponse(null, null);
        } catch (AuthException e) {
            throw new AccessDeniedException(e.getMessage());
        }
    }

    public JwtResponse refresh(@NonNull String refreshToken) {
        try {
            if (jwtProvider.validateRefreshToken(refreshToken)) {
                val claims = jwtProvider.getRefreshClaims(refreshToken);
                val login = claims.getSubject();
                val saveRefreshToken = refreshStorage.get(login);
                if (saveRefreshToken != null && saveRefreshToken.equals(refreshToken)) {
                    val user = userService.getByLogin(login)
                            .orElseThrow(() -> new AuthException("User not found"));
                    val accessToken = jwtProvider.generateAccessToken(user);
                    val newRefreshToken = jwtProvider.generateRefreshToken(user);
                    refreshStorage.put(user.getLogin(), newRefreshToken);
                    return new JwtResponse(accessToken, newRefreshToken);
                }
            }
            throw new AuthException("Invalid JWT token");
        } catch (AuthException e) {
            throw new AccessDeniedException(e.getMessage());
        }
    }

    public JwtAuthentication getAuthInfo() {
        return (JwtAuthentication) SecurityContextHolder.getContext().getAuthentication();
    }

}