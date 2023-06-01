package ru.sberbank.bonus_points_system.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.sberbank.bonus_points_system.dto.BonusAccountDto;
import ru.sberbank.bonus_points_system.exception.IllegalAccrualOperation;
import ru.sberbank.bonus_points_system.service.BonusService;

@RestController
@RequestMapping("/operator/bonus_accounts")
@RequiredArgsConstructor
@Slf4j
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
            summary = "Get account by Name",
            description = "Operator receives data about the account of the user with the specified username"
    )
    @GetMapping("/by-name")
    public BonusAccountDto getAccountByName(@RequestParam @Parameter(example = "User4") String userName) {
        log.info("get {}", userName);
        return bonusService.getBonusAccountByName(userName);
    }

    @Operation(
            summary = "Spend Points",
            description = "Operator writes off a certain amount of points from the user's account with the specified ID." +
                    " The score cannot be less than 0."
    )
    @PatchMapping("/{id}")
    public void spendPoints(@PathVariable @Parameter(example = "1") Long id,
                            @RequestParam @Parameter(example = "-10") Double change,
                            @RequestParam @Parameter(example = "Test debit") String description) {
        log.info("deduct points from Account {} - > {}, {}", id, change, description);
        if (change > 0) throw new IllegalAccrualOperation("This operation must be a debit operation. " +
                "The transferred value of points must not be greater than 0.");
        bonusService.processOperation(id, change, description);

    }


}
