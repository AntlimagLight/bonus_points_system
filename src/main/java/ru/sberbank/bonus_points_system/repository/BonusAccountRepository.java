package ru.sberbank.bonus_points_system.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.sberbank.bonus_points_system.dao.BonusAccount;

public interface BonusAccountRepository extends JpaRepository<BonusAccount, Long> {

    BonusAccount findByUsername(String username);
}
