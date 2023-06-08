package ru.sberbank.bonus_points_system.service;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.sberbank.bonus_points_system.dao.User;
import ru.sberbank.bonus_points_system.dto.UserDto;
import ru.sberbank.bonus_points_system.mapper.UserMapper;
import ru.sberbank.bonus_points_system.repository.UserRepository;
import ru.sberbank.bonus_points_system.util.UserUtils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import static ru.sberbank.bonus_points_system.util.ValidationUtils.assertExistence;
import static ru.sberbank.bonus_points_system.util.ValidationUtils.assertNotExistence;

@Service
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private AuthService authService;

    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public void registerAuthService(AuthService authService) {
        this.authService = authService;
    }

    @Transactional
    public UserDto create(UserDto userDto) {
        assertNotExistence(userRepository.findByLogin(userDto.getLogin()), "User with this name already exist");
        userDto.setId(null);
        userDto.setRegistered(ZonedDateTime.of(LocalDateTime.now(), ZoneId.of("Europe/Moscow"))
                .toLocalDateTime().toLocalDate());
        userDto.setEnabled(true);
        return userMapper.toDto(userRepository.save(UserUtils.prepareToSave(userMapper.toDao(userDto))));
    }

    @Transactional
    public void update(Long id, UserDto userDto) {
        val user = assertExistence(userRepository.findById(id), "User with this ID not found");
        userDto.setId(user.getId());
        userDto.setLogin(user.getLogin());
        userDto.setEnabled(user.getEnabled());
        userRepository.save(UserUtils.prepareToSave(userMapper.toDao(userDto)));
    }

    @Transactional(readOnly = true)
    public User getEntityByLogin(String login) {
        return assertExistence(userRepository.findByLogin(login), "User with this Login not found");
    }

    @Transactional(readOnly = true)
    public UserDto getById(Long id) {
        return userMapper.toDto(assertExistence(userRepository.findById(id), "User with this ID not found"));
    }

    @Transactional(readOnly = true)
    public UserDto getByLogin(String login) {
        return userMapper.toDto(assertExistence(userRepository.findByLogin(login), "User with this Login not found"));
    }

    @Transactional
    public void delete(Long id) {
        assertExistence(userRepository.findById(id), "User with this ID not found");
        userRepository.deleteById(id);

    }

    @Transactional
    public void setEnabled(Long id, Boolean enabled) {
        val user = assertExistence(userRepository.findById(id), "User with this ID not found");
        user.setEnabled(enabled);
        if (!enabled) {
            authService.BlockAccess(user.getLogin());
        }
    }


}