package ru.sberbank.bonus_points_system.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import ru.sberbank.bonus_points_system.dao.BonusAccount;
import ru.sberbank.bonus_points_system.dto.BonusAccountDto;
import ru.sberbank.bonus_points_system.dto.BonusOperationDto;
import ru.sberbank.bonus_points_system.dto.PageOfBonusOperation;
import ru.sberbank.bonus_points_system.exception.DoubleOperationException;
import ru.sberbank.bonus_points_system.exception.InsufficientBonusException;
import ru.sberbank.bonus_points_system.exception.NotExistException;
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
        val account = assertExistence(bonusAccountRepository.findById(id), "Account with this ID");
        account.setUsername(bonusAccountDto.getUsername());
        account.setVersion(account.getVersion() + 1);
        account.setLastUpdate(ZonedDateTime.of(LocalDateTime.now(), ZoneId.of("Europe/Moscow")).toLocalDateTime());
    }

    @Transactional
    public void deleteBonusAccount(Long id) {
        assertExistence(bonusAccountRepository.findById(id), "Account with this ID not found");
        bonusAccountRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public PageOfBonusOperation getOperationHistory(String username, LocalDateTime startTime, LocalDateTime endTime,
                                                    Pageable pageable) {
        val account = bonusAccountRepository.getReferenceByUsername(username);
        val page = bonusOperationRepository
                .findAllByAccountAndDateTimeAfterAndDateTimeBefore(account, startTime, endTime, pageable)
                .map(bonusOperationMapper::toDto);
        if (page.getContent().isEmpty()) {
            throw new NotExistException("Blank page requested");
        }
        return new PageOfBonusOperation(page.getTotalPages(), page.getTotalElements(), page.getSize(),
                page.getNumber(), page.getContent());
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void startOperation(Long id, BonusOperationDto operationDto) {
        val account = assertExistence(bonusAccountRepository.findById(id),
                "Account with this ID not found");
        processOperation(account, operationDto);
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void startOperation(String username, BonusOperationDto operationDto) {
        val account = assertExistence(bonusAccountRepository.findByUsername(username),
                "Account with this username not found");
        processOperation(account, operationDto);
    }

    private void processOperation(BonusAccount account, BonusOperationDto operationDto) {
        val time = ZonedDateTime.of(LocalDateTime.now(), ZoneId.of("Europe/Moscow")).toLocalDateTime();
        val operation = bonusOperationMapper.toDao(operationDto);
        operation.setAccount(account);
        operation.setDateTime(time);
        val newBonus = account.getBonus().add(operationDto.getChange());
        if (newBonus.compareTo(BigDecimal.ZERO) < 0) {
            throw new InsufficientBonusException("Not enough bonuses for this operation");
        }
        if (bonusOperationRepository.existsByExternalIDAndAccountAndDateTimeAfter(operationDto.getExternalID(),
                account, time.minusHours(1))) {
            throw new DoubleOperationException("Duplicate operation detected");
        }
        bonusOperationRepository.save(operation);
        account.setBonus(newBonus);
        account.setVersion(account.getVersion() + 1);
        account.setLastUpdate(time);
    }


}
