package ru.sberbank.bonus_points_system.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import ru.sberbank.bonus_points_system.dto.BonusAccountDto;
import ru.sberbank.bonus_points_system.dto.BonusOperationDto;
import ru.sberbank.bonus_points_system.exception.DoubleOperationException;
import ru.sberbank.bonus_points_system.exception.InsufficientBonusException;
import ru.sberbank.bonus_points_system.mapper.BonusAccountMapper;
import ru.sberbank.bonus_points_system.mapper.BonusOperationMapper;
import ru.sberbank.bonus_points_system.repository.BonusAccountRepository;
import ru.sberbank.bonus_points_system.repository.BonusOperationRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import static ru.sberbank.bonus_points_system.util.ValidationUtils.assertExistence;
import static ru.sberbank.bonus_points_system.util.ValidationUtils.assertNotExistence;

@Service
@Slf4j
@RequiredArgsConstructor
public class BonusService {

    private final BonusAccountRepository bonusAccountRepository;
    private final BonusOperationRepository bonusOperationRepository;
    private final BonusAccountMapper bonusAccountMapper;
    private final BonusOperationMapper bonusOperationMapper;

    @Transactional
    public BonusAccountDto createBonusAccount(BonusAccountDto bonusAccountDto) {
        assertNotExistence(bonusAccountRepository.findByUsername(bonusAccountDto.getUsername()),
                "Account with this name already exist");
        bonusAccountDto.setId(null);
        bonusAccountDto.setLastUpdate(ZonedDateTime.of(LocalDateTime.now(),
                ZoneId.of("Europe/Moscow")).toLocalDateTime());
        bonusAccountDto.setBonus(new BigDecimal(0));
        bonusAccountDto.setVersion(0);
        return bonusAccountMapper.toDto(bonusAccountRepository.save(bonusAccountMapper.toDao(bonusAccountDto)));
    }

    @Transactional(readOnly = true)
    public BonusAccountDto getBonusAccountByName(String userName) {
        return bonusAccountMapper.toDto(assertExistence(bonusAccountRepository.findByUsername(userName),
                "Account with this name not found"));
    }

    @Transactional(readOnly = true)
    public BonusAccountDto getBonusAccountById(Long id) {
        return bonusAccountMapper.toDto(assertExistence(bonusAccountRepository.findById(id),
                "Account with this ID not found"));
    }

    @Transactional
    public void updateBonusAccount(Long id, BonusAccountDto bonusAccountDto) {
        var account = assertExistence(bonusAccountRepository.findById(id), "Account with this ID");
        account.setUsername(bonusAccountDto.getUsername());
        account.setVersion(account.getVersion() + 1);
        account.setLastUpdate(ZonedDateTime.of(LocalDateTime.now(), ZoneId.of("Europe/Moscow")).toLocalDateTime());
    }

    @Transactional
    public void deleteBonusAccount(Long id) {
        assertExistence(bonusAccountRepository.findById(id), "Account with this ID not found");
        bonusAccountRepository.deleteById(id);
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void processOperation(Long id, BonusOperationDto operationDto) {
        var account = assertExistence(bonusAccountRepository.findById(id),
                "Account with this ID not found");
        var time = ZonedDateTime.of(LocalDateTime.now(), ZoneId.of("Europe/Moscow")).toLocalDateTime();
        var operation = bonusOperationMapper.toDao(operationDto);
        operation.setAccount(account);
        operation.setDateTime(time);
        bonusOperationRepository.save(operation);
        var newBonus = account.getBonus().add(operationDto.getChange());
        if (newBonus.compareTo(BigDecimal.ZERO) < 0) {
            throw new InsufficientBonusException("Not enough bonuses for this operation");
        }
        if (bonusOperationRepository.existsByExternalIDAndAccountAndDateTimeAfter(operationDto.getExternalID(),
                account, time.minusHours(1))) {
            throw new DoubleOperationException("Duplicate operation detected");
        }
        account.setBonus(newBonus);
        account.setVersion(account.getVersion() + 1);
        account.setLastUpdate(time);
    }

//    public void test(String externalID, Long id) {
//
//
//    }

    // TODO Get Audit


}
