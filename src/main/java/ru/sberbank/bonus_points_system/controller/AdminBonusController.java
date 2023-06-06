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
import ru.sberbank.bonus_points_system.dto.BonusAccountDto;
import ru.sberbank.bonus_points_system.dto.BonusOperationDto;
import ru.sberbank.bonus_points_system.service.BonusService;

import java.net.URI;

@RestController
@RequestMapping("/admin/bonus_accounts")
@RequiredArgsConstructor
@Slf4j
@PreAuthorize("hasAuthority('ADMIN')")
@SecurityRequirement(name = "Bearer Authentication")
public class AdminBonusController {

    private final BonusService bonusService;

    @Operation(
            summary = "Create Account",
            description = "The administrator registers a new bonus account for the user with the specified ID"
    )
    @PostMapping
    public ResponseEntity<BonusAccountDto> createAccount(@Valid @RequestParam Long userId) {
        log.info("create acc for user {}", userId);
        val created = bonusService.createBonusAccount(userId);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/admin/bonus_accounts/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @Operation(
            summary = "Perform Operation",
            description = "Administrator performs the operation of accruing or debiting bonuses " +
                    "for with the specified User ID"
    )
    @PatchMapping("/{userId}")
    public void performOperation(@PathVariable @Parameter(example = "1") Long userId,
                                 @Valid @RequestBody BonusOperationDto bonusOperationDto) {
        log.info("operation with Account {} - > {}", userId, bonusOperationDto.toString());
        bonusService.processOperation(userId, bonusOperationDto);
    }

    @Operation(
            summary = "Delete Account",
            description = "Administrator deletes account with the specified ID of account"
    )
    @DeleteMapping("/{id}")
    public void deleteAccount(@PathVariable @Parameter(example = "1") Long id) {
        log.info("deleted {}", id);
        bonusService.deleteBonusAccount(id);
    }


}
