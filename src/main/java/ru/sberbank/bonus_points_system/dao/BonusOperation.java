package ru.sberbank.bonus_points_system.dao;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
@Table(name = "operations", indexes = {
        @Index(name = "moment_idx", columnList = "operation_time")
})
public class BonusOperation {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bonus_accounts", nullable = false)
    @ToString.Exclude
    @JsonIgnore
    private BonusAccount account;

    @Column(name = "operation_time", nullable = false, updatable = false)
    private LocalDateTime dateTime;

    @Column(name = "change", nullable = false, updatable = false)
    private BigDecimal change;

}
