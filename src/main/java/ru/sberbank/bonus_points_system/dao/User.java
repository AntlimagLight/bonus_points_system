package ru.sberbank.bonus_points_system.dao;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {

    private String login;
    private String password;
    private Set<Role> roles;

    public User(String login, String password, Role... roles) {
        this(login, password, Arrays.asList((roles)));
    }

    public User(String login, String password, Collection<Role> roles) {
        this.login = login;
        this.password = password;
        setRoles(new HashSet<>(roles));
    }

    public User(String login, String password) {
        this(login, password, Collections.emptyList());
    }


}
