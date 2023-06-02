package ru.sberbank.bonus_points_system.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.sberbank.bonus_points_system.dto.BonusAccountDto;
import ru.sberbank.bonus_points_system.dto.BonusOperationDto;
import ru.sberbank.bonus_points_system.service.BonusService;

import java.net.URI;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/admin/bonus_accounts")
@RequiredArgsConstructor
@Slf4j
public class AdminBonusController {

    private final BonusService bonusService;

    @Operation(
            summary = "Create Account",
            description = "Administrator registers a new bonus account"
    )
    @PostMapping
    public ResponseEntity<BonusAccountDto> createAccount(@Valid @RequestBody BonusAccountDto account) {
        log.info("create {}", account.getUsername());
        val created = bonusService.createBonusAccount(account);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/admin/bonus_accounts/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @Operation(
            summary = "Update Account",
            description = "Administrator updates the bonus account data without affecting the number of bonuses"
    )
    @PutMapping("/{id}")
    public void updateAccount(@PathVariable @Parameter(example = "1") Long id, @Valid @RequestBody BonusAccountDto account) {
        log.info("update {}", id);
        bonusService.updateBonusAccount(id, account);
    }

    @Operation(
            summary = "Perform Operation",
            description = "Administrator performs the operation of accruing or debiting bonuses " +
                    "for an account with the specified ID"
    )
    @PatchMapping("/{id}")
    public void performOperation(@PathVariable @Parameter(example = "1") Long id,
                                 @Valid @RequestBody BonusOperationDto bonusOperationDto) {
        log.info("operation with Account {} - > {}", id,bonusOperationDto.toString());
        bonusService.processOperation(id, bonusOperationDto);
    }

    @Operation(
            summary = "Delete Account",
            description = "Administrator deletes account with the specified ID"
    )
    @DeleteMapping("/{id}")
    public void deleteAccount(@PathVariable @Parameter(example = "1") Long id) {
        log.info("deleted {}", id);
        bonusService.deleteBonusAccount(id);
    }


}
