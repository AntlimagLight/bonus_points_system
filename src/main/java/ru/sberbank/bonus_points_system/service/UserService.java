package ru.sberbank.bonus_points_system.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.sberbank.bonus_points_system.dao.Role;
import ru.sberbank.bonus_points_system.dao.User;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final List<User> users;

    public UserService() {
        this.users = List.of(
                new User("anton", "1234", Role.USER),
                new User("viktor", "12345", Role.USER, Role.OPERATOR),
                new User("ivan", "123456", Role.USER, Role.OPERATOR, Role.ADMIN)
        );
    }

    public Optional<User> getByLogin(@NonNull String login) {
        return users.stream()
                .filter(user -> login.equals(user.getLogin()))
                .findFirst();
    }

}