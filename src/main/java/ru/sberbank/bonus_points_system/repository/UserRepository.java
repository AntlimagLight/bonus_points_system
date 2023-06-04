package ru.sberbank.bonus_points_system.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.sberbank.bonus_points_system.dao.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByLogin(String login);

    Boolean existsByLogin(String login);
}
