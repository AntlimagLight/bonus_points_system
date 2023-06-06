package ru.sberbank.bonus_points_system.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.sberbank.bonus_points_system.dto.UserDto;
import ru.sberbank.bonus_points_system.security.JwtAuthentication;
import ru.sberbank.bonus_points_system.security.dto.JwtRequest;
import ru.sberbank.bonus_points_system.security.dto.JwtResponse;
import ru.sberbank.bonus_points_system.security.dto.RefreshJwtRequest;
import ru.sberbank.bonus_points_system.service.AuthService;
import ru.sberbank.bonus_points_system.service.UserService;

import java.net.URI;

import static ru.sberbank.bonus_points_system.util.UserUtils.STARTING_ROLES;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthService authService;
    private final UserService userService;

    @Operation(
            summary = "Register new User",
            description = "Register new user, roles set to User"
    )
    @PostMapping("/register")
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserDto userDto) {
        log.info("create {}", userDto.getLogin());
        userDto.setRoles(STARTING_ROLES);
        val created = userService.create(userDto);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/admin/users/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @Operation(
            summary = "Login",
            description = "Enter your login and password to get a new pair of tokens: " +
                    "access token (valid for 10 minutes) and refresh token (valid for 30 days)."
    )
    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody JwtRequest authRequest) {
        val token = authService.login(authRequest);
        return ResponseEntity.ok(token);
    }

    @Operation(
            summary = "Get new access token",
            description = "Enter your valid refresh token to get a new access token (valid for 10 minutes)."
    )
    @PostMapping("/token")
    public ResponseEntity<JwtResponse> getNewAccessToken(@RequestBody RefreshJwtRequest request) {
        val token = authService.getAccessToken(request.getRefreshToken());
        return ResponseEntity.ok(token);
    }

    @Operation(
            summary = "Get new access + refresh tokens",
            description = "Enter your valid refresh token to get a new pair of tokens: " +
                    "access token (valid for 10 minutes) and refresh token (valid for 30 days)."
    )
    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasAuthority('USER')")
    @PostMapping("/refresh")
    public ResponseEntity<JwtResponse> getNewRefreshToken(@RequestBody RefreshJwtRequest request) {
        val token = authService.refresh(request.getRefreshToken());
        return ResponseEntity.ok(token);
    }

    @Operation(
            summary = "Get auth info",
            description = "The user receives information about his auth session"
    )
    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasAuthority('USER')")
    @GetMapping("/info")
    public JwtAuthentication authInfo() {
        val authInfo = authService.getAuthInfo();
        log.info("get auth Information {}", authInfo.getLogin());
        return authInfo;
    }

}
