package ru.practicum.shareit.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import javax.validation.Valid;
import java.util.List;

/**
 * Контроллер для работы с {@link User}.
 */
@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;

    /**
     * Получение списка всех пользователей.
     */
    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers() {
        log.info("Получен запрос GET /users.");

        return ResponseEntity.ok(userService.get());
    }

    /**
     * Получение пользователя по идентификатору (id).
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
        log.info("Получен запрос GET /users/{}.", id);

        return ResponseEntity.ok(userService.getById(id));
    }

    /**
     * Создание пользователя.
     */
    @PostMapping
    public ResponseEntity<UserDto> addUser(@RequestBody @Valid UserDto userDto) {
        log.info("Получен запрос POST /users.");

        return ResponseEntity.ok(userService.create(userDto));
    }

    /**
     * Обновление пользователя.
     */
    @PatchMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable Long id, @RequestBody UserDto userDto) {
        log.info("Получен запрос PATCH /users/{}.", id);

        userDto.setId(id);

        return ResponseEntity.ok(userService.update(userDto));
    }

    /**
     * Удаление пользователя по идентификатору (id).
     */
    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        log.info("Получен запрос DELETE /users/{}.", id);

        userService.delete(id);
    }
}
