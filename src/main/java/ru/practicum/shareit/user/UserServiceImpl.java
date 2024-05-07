package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exeption.NotFoundException;
import ru.practicum.shareit.user.model.User;
import java.util.List;
import java.util.Optional;

/**
 * Сервис для работы с {@link User}.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserDao userDao;

    @Override
    public List<User> get() {
        log.info("Запрошен список всех пользователей");

        return userDao.get();
    }

    @Override
    public User getById(Long id) {
        log.info("Запрошен пользователь с id = {}", id);

        Optional<User> userOptional = userDao.getById(id);
        if (userOptional.isEmpty()) {
            throw new NotFoundException(String.format("Пользователь с id = %d не найден", id));
        }

        return userOptional.get();
    }

    @Override
    public User create(User user) {
        log.info("Попытка добавить пользователя {}", user);

        return userDao.create(user);
    }

    @Override
    public User update(User user) {
        log.info("Попытка обновить пользователя {}", user);

        checkExistsUserById(user.getId());

        return userDao.update(user);
    }

    @Override
    public void delete(Long id) {
        log.info("Удаление пользователя с id = {}", id);

        checkExistsUserById(id);
        userDao.delete(id);
    }

    private void checkExistsUserById(Long id) {
        Optional<User> userOptional = userDao.getById(id);

        if (userOptional.isEmpty()) {
            throw new NotFoundException("Пользователь с id = " + id + " не найден");
        }
    }
}
