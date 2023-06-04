package ru.sberbank.bonus_points_system.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

@RequiredArgsConstructor
public enum Role implements GrantedAuthority {

    ADMIN("ADMIN"),
    OPERATOR("OPERATOR"),
    USER("USER");


    private final String value;

    @Override
    public String getAuthority() {
        return value;
    }

}
