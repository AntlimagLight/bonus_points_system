package ru.sberbank.bonus_points_system.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.sberbank.bonus_points_system.dao.BonusAccount;

import java.util.Optional;

public interface BonusAccountRepository extends JpaRepository<BonusAccount, Long> {

    Optional<BonusAccount> findByUsername(String username);
}
