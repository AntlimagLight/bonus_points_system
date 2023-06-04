package ru.sberbank.bonus_points_system.service;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.sberbank.bonus_points_system.dao.Role;
import ru.sberbank.bonus_points_system.dao.User;
import ru.sberbank.bonus_points_system.repository.UserRepository;

import java.time.LocalDate;
import java.util.Optional;

import static ru.sberbank.bonus_points_system.config.WebSecurityConfig.PASSWORD_ENCODER;

@Service
@Slf4j
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
        if (!this.userRepository.existsByLogin("admin")) this.userRepository.save(new User(null, "Иван",
                "admin", PASSWORD_ENCODER.encode("admin56"), "ivan@gmail.com",
                LocalDate.now(), Role.USER, Role.OPERATOR, Role.ADMIN));
        if (!this.userRepository.existsByLogin("operator")) this.userRepository.save(new User(null, "Лидия",
                "operator", PASSWORD_ENCODER.encode("pass34"), "lida@gmail.com",
                LocalDate.now(), Role.USER, Role.OPERATOR));
        if (!this.userRepository.existsByLogin("user")) this.userRepository.save(new User(null, "Евгений",
                "user", PASSWORD_ENCODER.encode("pass12"), "evgeniy@gmail.com",
                LocalDate.now(), Role.USER));
    }

    public Optional<User> getByLogin(@NonNull String login) {
        return userRepository.findByLogin(login);
    }

}