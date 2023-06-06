package ru.sberbank.bonus_points_system.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
import ru.sberbank.bonus_points_system.service.UserService;

import java.net.URI;

@RestController
@RequestMapping("/admin/users")
@RequiredArgsConstructor
@Slf4j
@PreAuthorize("hasAuthority('ADMIN')")
@SecurityRequirement(name = "Bearer Authentication")
public class AdminUserController {

    private final UserService userService;

    @Operation(
            summary = "Create User",
            description = "Administrator creates a new user and can set any roles for him"
    )
    @PostMapping
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserDto userDto) {
        log.info("create {}", userDto.getLogin());
        val created = userService.create(userDto);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/admin/users/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @Operation(
            summary = "Update User",
            description = "Administrator updates the user data. Login is not updatable field"
    )
    @PutMapping("/{id}")
    public void updateUser(@PathVariable @Parameter(example = "3") Long id, @Valid @RequestBody UserDto userDto) {
        log.info("update {}", id);
        userService.update(id, userDto);
    }

    @Operation(
            summary = "Get user by ID",
            description = "Administrator gets user with the specified ID"
    )
    @GetMapping("/{id}")
    public UserDto getUserById(@PathVariable @Parameter(example = "3") Long id) {
        log.info("get {}", id);
        return userService.getById(id);
    }

    @Operation(
            summary = "Get account by Login",
            description = "Administrator gets user with the specified login"
    )
    @GetMapping("/by-login")
    public UserDto getUserByName(@RequestParam @Parameter(example = "user") String login) {
        log.info("get {}", login);
        return userService.getByLogin(login);
    }

    @Operation(
            summary = "Delete User",
            description = "Administrator deletes user with the specified ID"
    )
    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable @Parameter(example = "3") Long id) {
        log.info("deleted {}", id);
        userService.delete(id);
    }

    @Operation(
            summary = "Set Enabled",
            description = "Uses for block or unblock user with the specified ID"
    )
    @PatchMapping("/{id}")
    public void setEnabled(@PathVariable @Parameter(example = "3") Long id,
                           @RequestParam @Parameter(example = "false") Boolean enabled) {
        log.info("set {} enabled to user {}", enabled, id);
        userService.setEnabled(id, enabled);
    }
}
