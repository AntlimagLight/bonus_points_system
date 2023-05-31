package ru.sberbank.bonus_points_system.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.sberbank.bonus_points_system.dao.BonusOperation;

public interface OperationRepository extends JpaRepository<BonusOperation, Long> {
}
