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
import ru.sberbank.bonus_points_system.exception.IllegalAccrualOperation;
import ru.sberbank.bonus_points_system.service.BonusService;

import java.net.URI;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Slf4j
public class UserBonusController {

    private final BonusService bonusService;

    @Operation(
            summary = "Register",
            description = "User registers a new account"
    )
    @PostMapping
    public ResponseEntity<BonusAccountDto> register(@Valid @RequestBody BonusAccountDto account) {
        log.info("register {}", account.getUsername());
        val created = bonusService.createBonusAccount(account);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/admin/bonus_accounts/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

//    @Operation(
//            summary = "Get Account",
//            description = "The user receives information about the state of his account."
//    )
//    @GetMapping("/{id}")
//    public BonusAccountDto getAccount(@AuthenticationPrincipal AuthUser authUser) {
//        log.info("get {}", id);
//        return null;
//    }
//
//    @Operation(
//            summary = "Spend Points",
//            description = "User spends a certain amount of points from his account. " +
//                    "The score cannot be less than 0."
//    )
//    @PatchMapping ("/{id}")
//    public void spendPoints(@AuthenticationPrincipal AuthUser authUser,
//                                 @RequestParam @Parameter(example = "-10") Double change,
//                                 @RequestParam @Parameter(example = "Test debit") String description ) {
//        log.info("deduct points from Account {} - > {}, {}", authUser.getId(), change, description);
//        if (change > 0) throw new IllegalAccrualOperation("This operation must be a debit operation. " +
//                "The transferred value of points must not be greater than 0.");
//        bonusService.processOperation(authUser.getId(), change, description);
//    }


}
