package ru.sberbank.bonus_points_system.dao;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
@Table(name = "service_users", indexes = @Index(columnList = "login", name = "login_idx", unique = true))
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name")
    private String name;
    @Column(name = "login", nullable = false)
    private String login;
    @Column(name = "password", nullable = false)
    private String password;
    @Column(name = "email")
    private String email;
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"),
            indexes = @Index(columnList = "user_id, role", name = "users_roles_idx", unique = true))
    @Column(name = "role")
    @ElementCollection(fetch = FetchType.EAGER)
    private Set<Role> roles;
    @Column(name = "registered", nullable = false, updatable = false)
    @Temporal(TemporalType.DATE)
    private LocalDate registered;
    @Column(name = "enabled", nullable = false)
    private Boolean enabled;

    public User(Long id, String name, String login, String password, String email, LocalDate registered, Role... roles) {
        this(id, name, login, password, email, registered, Arrays.asList((roles)));
    }

    public User(Long id, String name, String login, String password, String email, LocalDate registered,
                Collection<Role> roles) {
        this.id = id;
        this.name = name;
        this.login = login;
        this.password = password;
        this.email = email;
        this.registered = registered;
        this.enabled = true;
        setRoles(new HashSet<>(roles));
    }

    public User(Long id, String name, String login, String password, String email, LocalDate registered) {
        this(id, name, login, password, email, registered, Collections.emptyList());
    }

}
