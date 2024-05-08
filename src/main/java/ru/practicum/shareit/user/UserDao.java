package ru.practicum.shareit.user;

import ru.practicum.shareit.user.model.User;
import java.util.List;

/**
 * DAO для работы с {@link User}.
 */
public interface UserDao {
    List<User> get();

    User getById(Long id);

    User create(User user);

    User update(User user);

    void delete(Long id);
}
