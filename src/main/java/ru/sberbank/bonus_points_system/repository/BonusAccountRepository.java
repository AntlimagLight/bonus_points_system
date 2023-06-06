package ru.sberbank.bonus_points_system.repository;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import ru.sberbank.bonus_points_system.dao.BonusAccount;
import ru.sberbank.bonus_points_system.dao.User;

import java.util.Optional;

public interface BonusAccountRepository extends JpaRepository<BonusAccount, Long> {

    @Lock(LockModeType.OPTIMISTIC)
    Optional<BonusAccount> findByUser(User user);

    BonusAccount getReferenceByUser(User user);
}
