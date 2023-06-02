package ru.sberbank.bonus_points_system.dao;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.validator.constraints.Range;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
@Table(name = "bonus_accounts", indexes = @Index(columnList = "username", name = "username_idx", unique = true))
public class BonusAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "bonus", nullable = false)
    @Range(min = 0)
    private BigDecimal bonus;

    @Column(name = "version", nullable = false)
    private Integer version;

    @Column(name = "last_update", nullable = false)
    private LocalDateTime lastUpdate;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "account")
    @OrderBy("dateTime DESC")
    @ToString.Exclude
    @JsonIgnore
    private List<BonusOperation> operations;
}
