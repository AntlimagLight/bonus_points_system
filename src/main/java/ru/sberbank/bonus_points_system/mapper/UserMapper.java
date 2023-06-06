package ru.sberbank.bonus_points_system.mapper;

import org.mapstruct.Mapper;
import ru.sberbank.bonus_points_system.dao.User;
import ru.sberbank.bonus_points_system.dto.UserDto;

@Mapper(componentModel = "spring", uses = UserMapper.class)
public interface UserMapper {

    UserDto toDto(User user);

    User toDao(UserDto userDto);

}
