package ru.sberbank.bonus_points_system.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.sberbank.bonus_points_system.dao.BonusAccount;
import ru.sberbank.bonus_points_system.repository.BonusAccountRepository;

@RestController
@RequestMapping("/operator/bonus_accounts")
@RequiredArgsConstructor
@Slf4j
public class OperatorBonusController {

    private final BonusAccountRepository bonusAccountRepository;

    @GetMapping("/{id}")
    public BonusAccount getAccountById(@PathVariable Long id) {
        log.info("get {}", id);
        return null;
    }

    @GetMapping("/by-name")
    public BonusAccount getAccountByName(@RequestParam String userName) {
        log.info("get {}", userName);
        return bonusAccountRepository.findByUsername(userName);
    }

    @PatchMapping("/{id}")
    public void deductPoints(@PathVariable Long id,
                             @RequestParam Double change,
                             @RequestParam String description) {
        log.info("deduct points from Account {} - > {}, {}", id, change, description);
    }


}
