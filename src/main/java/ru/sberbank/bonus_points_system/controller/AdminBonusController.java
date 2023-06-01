package ru.sberbank.bonus_points_system.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.sberbank.bonus_points_system.dao.BonusAccount;
import ru.sberbank.bonus_points_system.repository.BonusAccountRepository;

import java.net.URI;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/admin/bonus_accounts")
@RequiredArgsConstructor
@Slf4j
public class AdminBonusController {

    private final BonusAccountRepository bonusAccountRepository;

    @PostMapping
    public ResponseEntity<BonusAccount> createAccount(@Valid @RequestBody BonusAccount account) {
        log.info("create {}", account.getUsername());
        account.setId(null);
        account.setLastUpdate(LocalDateTime.now());
        val created = bonusAccountRepository.save(account);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/admin/bonus_accounts/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(account);
    }

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

    @PutMapping("/{id}")
    public void updateAccount(@PathVariable Long id, @Valid @RequestBody BonusAccount account) {
        log.info("update {}", id);
    }

    @PatchMapping("/{id}")
    public void performOperation(@PathVariable Long id,
                                 @RequestParam Double change,
                                 @RequestParam String description) {
        log.info("operation with Account {} - > {}, {}", id, change, description);
    }

    @DeleteMapping("/{id}")
    public void deleteAccount(@PathVariable Long id) {
        log.info("deleted {}", id);
    }


}
