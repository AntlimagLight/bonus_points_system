package ru.sberbank.bonus_points_system.mapper;

import lombok.val;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.sberbank.bonus_points_system.dao.BonusAccount;
import ru.sberbank.bonus_points_system.dto.BonusAccountDto;
import ru.sberbank.bonus_points_system.dto.UserDto;

@Mapper(componentModel = "spring", uses = BonusAccountMapper.class)
public interface BonusAccountMapper {

    default BonusAccountDto toDto(BonusAccount bonusAccount) {
        val userDto = UserDto.builder()
                .id(bonusAccount.getUser().getId())
                .login(bonusAccount.getUser().getLogin())
                .name(bonusAccount.getUser().getName())
                .email(bonusAccount.getUser().getEmail())
                .registered(bonusAccount.getUser().getRegistered())
                .roles(bonusAccount.getUser().getRoles())
                .enabled(bonusAccount.getUser().getEnabled())
                .build();
        return new BonusAccountDto(bonusAccount.getId(), bonusAccount.getBonus(), bonusAccount.getVersion(),
                bonusAccount.getLastUpdate(), userDto);
    }

    @Mapping(target = "operations", ignore = true)
    BonusAccount toDao(BonusAccountDto bonusAccountDto);

}
