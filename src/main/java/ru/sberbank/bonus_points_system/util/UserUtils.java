package ru.sberbank.bonus_points_system.util;

import lombok.experimental.UtilityClass;
import ru.sberbank.bonus_points_system.dao.Role;
import ru.sberbank.bonus_points_system.dao.User;

import java.util.HashSet;
import java.util.Set;

import static ru.sberbank.bonus_points_system.config.WebSecurityConfig.PASSWORD_ENCODER;

@UtilityClass
public class UserUtils {

    public final static Set<Role> STARTING_ROLES = new HashSet<>();

    static {
        STARTING_ROLES.add(Role.USER);
    }

    public static User prepareToSave(User user) {
        user.setPassword(PASSWORD_ENCODER.encode(user.getPassword()));
        user.setEmail(user.getEmail().toLowerCase());
        Set<Role> roles = user.getRoles();
        if (roles == null || roles.isEmpty()) {
            user.setRoles(STARTING_ROLES);
        }
        return user;
    }


}
