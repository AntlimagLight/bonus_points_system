package ru.sberbank.bonus_points_system.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.sberbank.bonus_points_system.dto.BonusAccountDto;
import ru.sberbank.bonus_points_system.dto.BonusOperationDto;
import ru.sberbank.bonus_points_system.dto.PageOfBonusOperation;
import ru.sberbank.bonus_points_system.exception.IllegalAccrualOperation;
import ru.sberbank.bonus_points_system.service.AuthService;
import ru.sberbank.bonus_points_system.service.BonusService;

import java.math.BigDecimal;
import java.net.URI;
import java.time.LocalDateTime;

@RestController
@RequestMapping
@RequiredArgsConstructor
@Slf4j
@PreAuthorize("hasAuthority('USER')")
@SecurityRequirement(name = "Bearer Authentication")
public class UserBonusController {

    private final BonusService bonusService;
    private final AuthService authService;

    @Operation(
            summary = "Register Bonus Acc",
            description = "User registers a new account"
    )
    @PostMapping("/register_ba")
    public ResponseEntity<BonusAccountDto> registerBonusAcc() {
        val account = BonusAccountDto.builder()
                .username(authService.getAuthInfo().getLogin())
                .build();
        log.info("register {}", account.getUsername());
        val created = bonusService.createBonusAccount(account);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/admin/bonus_accounts/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @Operation(
            summary = "Get bonus account",
            description = "The user receives information about the state of his account."
    )
    @GetMapping("/bonus")
    public BonusAccountDto getAccount() {
        val authInfo = authService.getAuthInfo();
        log.info("get bonus account by user {}", authInfo.getLogin());
        return bonusService.getBonusAccountByName(authInfo.getLogin());
    }

    @Operation(
            summary = "Spend Points",
            description = "User spends a certain amount of points from his account. " +
                    "The score cannot be less than 0."
    )
    @PatchMapping("/bonus")
    public void spendPoints(@Valid @RequestBody BonusOperationDto bonusOperationDto) {
        val authInfo = authService.getAuthInfo();
        log.info("deduct points from Account {} - > {}", authInfo.getLogin(), bonusOperationDto.toString());
        if (bonusOperationDto.getChange().compareTo(BigDecimal.ZERO) > 0) {
            throw new IllegalAccrualOperation("This operation must be a debit operation. " +
                    "The transferred value of points must not be greater than 0.");
        }
        bonusService.startOperation(authInfo.getLogin(), bonusOperationDto);
    }

    @Operation(
            summary = "Get Operation History",
            description = "The response is a list of all transactions for a given period of time"
    )
    @GetMapping("/history")
    public PageOfBonusOperation getOperationHistory(@RequestParam @Parameter(example = "2023-05-31T22:00") LocalDateTime startTime,
                                                    @RequestParam @Parameter(example = "2023-06-10T22:00") LocalDateTime endTime,
                                                    @RequestParam @Parameter(example = "0") Integer pageNumber,
                                                    @RequestParam @Parameter(example = "10") Integer pageSize) {
        val authInfo = authService.getAuthInfo();
        log.info("get operations by user {}, from {} to {}, page {} / {}", authInfo.getLogin(),
                startTime, endTime, pageNumber, pageSize);
        val pageRequest = PageRequest.of(pageNumber, pageSize);
        return bonusService.getOperationHistory(authInfo.getLogin(), LocalDateTime.now().minusMonths(1),
                LocalDateTime.now(), pageRequest);
    }

}
