package ru.sberbank.bonus_points_system.dao;

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
@AllArgsConstructor
@ToString
@Table(name = "bonus_accounts")
public class BonusAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @Column(name = "bonus", nullable = false)
    @Range(min = 0)
    private BigDecimal bonus;

    @Version
    @Column(name = "version", nullable = false)
    private Integer version;

    @Column(name = "last_update", nullable = false)
    private LocalDateTime lastUpdate;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "account")
    @OrderBy("dateTime DESC")
    @ToString.Exclude
    private List<BonusOperation> operations;
}
