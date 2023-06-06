package ru.sberbank.bonus_points_system.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.sberbank.bonus_points_system.dto.BonusAccountDto;
import ru.sberbank.bonus_points_system.dto.BonusOperationDto;
import ru.sberbank.bonus_points_system.exception.IllegalAccrualOperation;
import ru.sberbank.bonus_points_system.service.BonusService;

import java.math.BigDecimal;

@RestController
@RequestMapping("/operator/bonus_accounts")
@RequiredArgsConstructor
@Slf4j
@PreAuthorize("hasAuthority('OPERATOR')")
@SecurityRequirement(name = "Bearer Authentication")
public class OperatorBonusController {

    private final BonusService bonusService;

    @Operation(
            summary = "Get account by ID",
            description = "Operator receives data about the account of the user with the specified ID"
    )
    @GetMapping("/{id}")
    public BonusAccountDto getAccountById(@PathVariable @Parameter(example = "1") Long id) {
        log.info("get {}", id);
        return bonusService.getBonusAccountById(id);
    }

    @Operation(
            summary = "Get account by Login",
            description = "Operator receives data about the account of the user with the specified user login"
    )
    @GetMapping("/by-name")
    public BonusAccountDto getAccountByLogin(@RequestParam @Parameter(example = "user") String userName) {
        log.info("get {}", userName);
        return bonusService.getBonusAccountByLogin(userName);
    }

    @Operation(
            summary = "Spend Points",
            description = "Operator debit points from the user's account with the specified user ID." +
                    " The score cannot be less than 0."
    )
    @PatchMapping("/{userId}")
    public void spendPoints(@PathVariable @Parameter(example = "1") Long userId,
                            @Valid @RequestBody BonusOperationDto bonusOperationDto) {
        log.info("deduct points from Account {} - > {}", userId, bonusOperationDto.toString());
        if (bonusOperationDto.getChange().compareTo(BigDecimal.ZERO) > 0) {
            throw new IllegalAccrualOperation("This operation must be a debit operation. " +
                    "The transferred value of points must not be greater than 0.");
        }
        bonusService.processOperation(userId, bonusOperationDto);
    }

}

