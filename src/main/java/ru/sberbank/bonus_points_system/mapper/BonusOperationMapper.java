package ru.sberbank.bonus_points_system.mapper;

import org.mapstruct.Mapper;
import ru.sberbank.bonus_points_system.dao.BonusOperation;
import ru.sberbank.bonus_points_system.dto.BonusOperationDto;

@Mapper(componentModel = "spring", uses = BonusOperationMapper.class)
public interface BonusOperationMapper {

    BonusOperation toDto(BonusOperationDto bonusOperationDto);

    BonusOperationDto toDao(BonusOperation bonusOperation);

}
