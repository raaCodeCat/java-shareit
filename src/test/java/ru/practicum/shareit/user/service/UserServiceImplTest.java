package ru.practicum.shareit.user.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exeption.ConflictException;
import ru.practicum.shareit.exeption.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void getUser_whenEmptyUserTable_thenReturnEmptyList() {
        when(userRepository.findAll())
                .thenReturn(Collections.emptyList());

        final List<UserDto> users = userService.get();

        assertNotNull(users);
        assertEquals(Collections.emptyList(), users);

        verify(userRepository, times(1)).findAll();
    }

    @Test
    void getUser_whenInvoked_thenReturnUserDtoList() {
        final User user1 = new User(1L, "user1", "user1@mail.com");
        final User user2 = new User(2L, "user2", "user2@mail.com");

        when(userRepository.findAll())
                .thenReturn(List.of(user1, user2));

        List<UserDto> actual = userService.get();

        assertNotNull(actual);
        assertEquals(2, actual.size());

        assertEquals(1L, actual.get(0).getId());
        assertEquals("user1", actual.get(0).getName());
        assertEquals("user1@mail.com", actual.get(0).getEmail());

        assertEquals(2L, actual.get(1).getId());
        assertEquals("user2", actual.get(1).getName());
        assertEquals("user2@mail.com", actual.get(1).getEmail());

        verify(userRepository, times(1)).findAll();
    }

    @Test
    void getById_whenInvalidUserId_thenNotFoundExceptionThrown() {
        when(userRepository.findById(1L))
                .thenThrow(new NotFoundException("Пользователь с идентификатором 1 не найден"));

        final NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> userService.getById(1L)
        );

        assertNotNull(exception);
        assertEquals("Пользователь с идентификатором 1 не найден",
                exception.getMessage());

        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void getById_whenInvoked_thenReturnUserDto() {
        final User user1 = new User(1L, "user1", "user1@mail.com");

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user1));

        final UserDto actual = userService.getById(1L);

        assertNotNull(actual);
        assertEquals(1L, actual.getId());
        assertEquals("user1", actual.getName());
        assertEquals("user1@mail.com", actual.getEmail());

        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void create_whenInvoked_thenCreateUser() {
        when(userRepository.save(Mockito.any(User.class)))
                .thenReturn(new User(1L, "user1", "user1@mail.com"));

        final UserDto actual = userService.create(new UserDto(null, "user1", "user1@mail.com"));

        assertNotNull(actual);
        assertEquals(1L, actual.getId());
        assertEquals("user1", actual.getName());
        assertEquals("user1@mail.com", actual.getEmail());

        verify(userRepository, times(1)).save(Mockito.any(User.class));
    }

    @Test
    void create_whenEmailAlreadyExists_thenConflictExceptionThrown() {
        when(userRepository.save(Mockito.any(User.class)))
                .thenThrow(new ConflictException("Конфликт"));

        final ConflictException exception = assertThrows(
                ConflictException.class,
                () -> userService.create(new UserDto(null, "user1", "user1@mail.com"))
        );

        assertNotNull(exception);
        assertEquals("Конфликт", exception.getMessage());

        verify(userRepository, times(1)).save(Mockito.any(User.class));
    }

    @Test
    void update_whenInvoked_thenUpdateUser() {
        when(userRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(new User(1L, "user", "user@mail.com")));

        when(userRepository.save(Mockito.any(User.class)))
                .thenReturn(new User(1L, "userUpd", "userUpd@mail.com"));

        final UserDto actual = userService.update(new UserDto(1L, "userUpd", "userUpd@mail.com"));

        assertNotNull(actual);
        assertEquals(1L, actual.getId());
        assertEquals("userUpd", actual.getName());
        assertEquals("userUpd@mail.com", actual.getEmail());

        verify(userRepository, times(1)).findById(Mockito.anyLong());
        verify(userRepository, times(1)).save(Mockito.any(User.class));
    }

    @Test
    void update_whenUserNotExists_thenNotFoundExceptionThrown() {
        when(userRepository.findById(1L))
                .thenReturn(Optional.empty());

        final NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> userService.update(new UserDto(1L, "userUpd", "userUpd@mail.com"))
        );

        assertNotNull(exception);
        assertEquals("Пользователь с идентификатором 1 не найден", exception.getMessage());

        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, never()).save(Mockito.any(User.class));
    }

    @Test
    void update_whenEmailAlreadyExists_thenConflictExceptionThrown() {
        when(userRepository.findById(1L))
                .thenReturn(Optional.of(new User(1L, "user", "user@mail.com")));

        when(userRepository.save(Mockito.any(User.class)))
                .thenThrow(new ConflictException("Конфликт"));

        final ConflictException exception = assertThrows(
                ConflictException.class,
                () -> userService.update(new UserDto(1L, "userUpd", "userUpd@mail.com"))
        );

        assertNotNull(exception);
        assertEquals("Конфликт", exception.getMessage());

        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).save(Mockito.any(User.class));
    }

    @Test
    void delete_whenInvoked_thenDeleteUserById() {
        when(userRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(new User(1L, "user", "user@mail.com")));

        userService.delete(1L);

        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).delete(Mockito.any(User.class));
    }
}