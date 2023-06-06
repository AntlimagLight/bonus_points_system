package ru.sberbank.bonus_points_system.dao;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
@AllArgsConstructor
@Builder
@Table(name = "operations", indexes = {
        @Index(name = "moment_idx", columnList = "operation_time")
})
public class BonusOperation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bonus_accounts", nullable = false)
    @ToString.Exclude
    private BonusAccount account;

    @Column(name = "description", updatable = false)
    private String description;

    @Column(name = "operation_time", nullable = false, updatable = false)
    private LocalDateTime dateTime;

    @Column(name = "change", nullable = false, updatable = false)
    private BigDecimal change;

    @Column(name = "external_id", nullable = false, updatable = false)
    private String externalID;


}
