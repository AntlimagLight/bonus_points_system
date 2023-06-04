package ru.sberbank.bonus_points_system.util;

import io.jsonwebtoken.Claims;
import lombok.experimental.UtilityClass;
import lombok.val;
import ru.sberbank.bonus_points_system.dao.Role;
import ru.sberbank.bonus_points_system.security.JwtAuthentication;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@UtilityClass
public final class JwtUtils {

    public static JwtAuthentication generate(Claims claims) {
        val jwtInfoToken = new JwtAuthentication();
        jwtInfoToken.setRoles(getRoles(claims));
        jwtInfoToken.setUserId(claims.get("id", Long.class));
        jwtInfoToken.setName(claims.get("name", String.class));
        jwtInfoToken.setLogin(claims.getSubject());
        return jwtInfoToken;
    }

    @SuppressWarnings("unchecked")
    private static Set<Role> getRoles(Claims claims) {
        final List<String> roles = claims.get("roles", List.class);
        return roles.stream()
                .map(Role::valueOf)
                .collect(Collectors.toSet());
    }

}
