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
@RequestMapping("/admin")
@RequiredArgsConstructor
@Slf4j
public class AdminBonusController {

    private final BonusAccountRepository bonusAccountRepository;

    @PostMapping
    public ResponseEntity<BonusAccount> createRestaurant(@Valid @RequestBody BonusAccount account) {
        log.info("create {}", account.getUsername());
        account.setId(null);
        account.setLastUpdate(LocalDateTime.now());
        val created = bonusAccountRepository.save(account);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/admin/bonus_accounts/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(account);
    }

    @GetMapping("/by-name")
    public BonusAccount getUserByEmail(@RequestParam String name) {
        log.info("get {}", name);
        return bonusAccountRepository.findByUsername(name);
    }
}
