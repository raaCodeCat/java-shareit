package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import java.util.List;

/**
 * Сервис для работы с {@link User}.
 */
public interface UserService {
    List<UserDto> get();

    UserDto getById(Long id);

    UserDto create(UserDto userDto);

    UserDto update(UserDto userDto);

    void delete(Long id);
}
