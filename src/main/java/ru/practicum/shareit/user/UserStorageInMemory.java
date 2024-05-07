package ru.practicum.shareit.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exeption.ConflictException;
import ru.practicum.shareit.user.model.User;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * Хранилище в памяти для работы с {@link User}.
 */
@Component
@Slf4j
public class UserStorageInMemory implements UserDao {
    private Long currentId = 0L;
    private final Set<String> emails = new HashSet<>();
    private final Map<Long, User> users = new HashMap<>();

    @Override
    public List<User> get() {
        return new ArrayList<>(users.values());
    }

    @Override
    public Optional<User> getById(Long id) {
        if (!users.containsKey(id)) {
            return Optional.empty();
        }

        return Optional.of(users.get(id));
    }

    @Override
    public User create(User user) {
        String email = user.getEmail();

        checkEmailNotExists(email);

        emails.add(email);
        currentId++;
        user.setId(currentId);
        users.put(currentId, user);
        log.info("Пользователь добавлен");

        return user;
    }

    @Override
    public User update(User user) {
        Long id = user.getId();
        String newName = user.getName();
        String newEmail = user.getEmail();

        User userInMemory = users.get(id);
        String userEmailInMemory = userInMemory.getEmail();

        if (newEmail != null && !newEmail.equals(userInMemory.getEmail())) {
            checkEmailNotExists(newEmail);

            emails.remove(userEmailInMemory);
            emails.add(newEmail);
            userInMemory.setEmail(newEmail);
        }

        if (newName != null && !newName.equals(userInMemory.getName())) {
            userInMemory.setName(newName);
        }

        log.info("Пользователь обновлён");

        return userInMemory;
    }

    @Override
    public void delete(Long id) {
        emails.remove(users.get(id).getEmail());
        users.remove(id);

        log.info("Пользователь удален");
    }

    private void checkEmailNotExists(String email) {
        if (emails.contains(email)) {
            throw new ConflictException(
                    String.format("Пользователь с email %s уже существует", email)
            );
        }
    }
}
