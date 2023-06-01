package ru.sberbank.bonus_points_system.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.sberbank.bonus_points_system.dao.BonusAccount;
import ru.sberbank.bonus_points_system.dto.BonusAccountDto;

@Mapper(componentModel = "spring", uses = BonusAccountMapper.class)
public interface BonusAccountMapper {

    BonusAccountDto toDto(BonusAccount bonusAccount);

    @Mapping(target = "operations", ignore = true)
    BonusAccount toDao(BonusAccountDto bonusAccountDto);

}
