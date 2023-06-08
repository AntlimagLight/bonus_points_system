package ru.sberbank.bonus_points_system.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.sberbank.bonus_points_system.dao.BonusAccount;
import ru.sberbank.bonus_points_system.dao.BonusOperation;

import java.time.LocalDateTime;

public interface BonusOperationRepository extends JpaRepository<BonusOperation, Long> {

    Boolean existsByExternalIDAndAccountAndDateTimeAfter(String externalID, BonusAccount account,
                                                         LocalDateTime localDateTime);

    Page<BonusOperation> findAllByAccountAndDateTimeAfterAndDateTimeBefore(BonusAccount account, LocalDateTime startTime,
                                                                           LocalDateTime endTime, Pageable pageable);

    BonusOperation findByExternalID(String externalID);

}
