package ru.sberbank.bonus_points_system.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.sberbank.bonus_points_system.repository.BonusAccountRepository;

@RestController
@RequestMapping("/operator/bonus_accounts")
@RequiredArgsConstructor
@Slf4j
public class UserBonusController {

    private final BonusAccountRepository bonusAccountRepository;

//    @GetMapping("/{id}")
//    public BonusAccount getAccount(@AuthenticationPrincipal AuthUser authUser) {
//        log.info("get {}", id);
//        return null;
//    }

//    @PatchMapping ("/{id}")
//    public void deductPoints(@AuthenticationPrincipal AuthUser authUser,
//                                 @RequestParam Double change,
//                                 @RequestParam String description ) {
//        log.info("deduct points from Account {} - > {}, {}", id, change, description);
//    }


}
