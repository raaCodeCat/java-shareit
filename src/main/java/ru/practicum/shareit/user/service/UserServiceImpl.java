package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exeption.ConflictException;
import ru.practicum.shareit.exeption.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import java.util.List;

/**
 * Сервис для работы с {@link User}.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public List<UserDto> get() {
        log.info("Запрошен список всех пользователей");

        return UserMapper.toUserDto(userRepository.findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public UserDto getById(Long id) {
        log.info("Запрошен пользователь с id = {}", id);

        return UserMapper.toUserDto(getUserById(id));
    }

    @Override
    @Transactional
    public UserDto create(UserDto userDto) {
        log.info("Попытка добавить пользователя {}", userDto);

        User user = UserMapper.fromUserDto(userDto);

        try {
            return UserMapper.toUserDto(userRepository.save(user));
        } catch (DataIntegrityViolationException e) {
            throw new ConflictException(e.getMostSpecificCause().getMessage());
        }
    }

    @Override
    @Transactional
    public UserDto update(UserDto userDto) {
        log.info("Попытка обновить пользователя {}", userDto);

        Long id = userDto.getId();
        String name = userDto.getName();
        String email = userDto.getEmail();

        User userForUpdate = getUserById(id);

        if (name != null) {
            userForUpdate.setName(name);
        }

        if (email != null) {
            userForUpdate.setEmail(email);
        }

        try {
            return UserMapper.toUserDto(userRepository.save(userForUpdate));
        } catch (DataIntegrityViolationException e) {
            throw new ConflictException(e.getMostSpecificCause().getMessage());
        }
    }

    @Override
    @Transactional
    public void delete(Long id) {
        log.info("Удаление пользователя с id = {}", id);

        User user = getUserById(id);
        userRepository.delete(user);
    }

    private User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() ->
                new NotFoundException(String.format("Пользователь с идентификатором %d не найден", id)));
    }
}
