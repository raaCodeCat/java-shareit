package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import java.util.List;

/**
 * Сервис для работы с {@link User}.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserDao userDao;

    @Override
    public List<UserDto> get() {
        log.info("Запрошен список всех пользователей");

        return UserMapper.toUserDto(userDao.get());
    }

    @Override
    public UserDto getById(Long id) {
        log.info("Запрошен пользователь с id = {}", id);

        return UserMapper.toUserDto(userDao.getById(id));
    }

    @Override
    public UserDto create(UserDto userDto) {
        log.info("Попытка добавить пользователя {}", userDto);

        User user = UserMapper.fromUserDto(userDto);

        return UserMapper.toUserDto(userDao.create(user));
    }

    @Override
    public UserDto update(UserDto userDto) {
        log.info("Попытка обновить пользователя {}", userDto);

        User user = UserMapper.fromUserDto(userDto);

        checkExistsUserById(user.getId());

        return UserMapper.toUserDto(userDao.update(user));
    }

    @Override
    public void delete(Long id) {
        log.info("Удаление пользователя с id = {}", id);

        checkExistsUserById(id);
        userDao.delete(id);
    }

    private void checkExistsUserById(Long id) {
        userDao.getById(id);
    }
}
