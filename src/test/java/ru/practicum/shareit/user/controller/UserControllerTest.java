package ru.practicum.shareit.user.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {
    @Mock
    UserService userService;

    @InjectMocks
    UserController userController;

    @Test
    void getAllUsers_whenInvoked_thenResponseStatusOkWithCollectionUserDtoInBody() {
        List<UserDto> userDtoList = List.of(new UserDto());
        when(userService.get()).thenReturn(userDtoList);

        ResponseEntity<List<UserDto>> response = userController.getAllUsers();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(userDtoList, response.getBody());
    }

    @Test
    void getUserById_whenInvoked_thenResponseStatusOkWithUserDtoInBody() {
        UserDto userDto = new UserDto();
        when(userService.getById(Mockito.anyLong())).thenReturn(userDto);

        ResponseEntity<UserDto> response = userController.getUserById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(userDto, response.getBody());
    }

    @Test
    void addUser_whenInvoked_thenResponseStatusOkWithUserDtoInBody() {
        UserDto userDto = new UserDto();
        when(userService.create(userDto)).thenReturn(userDto);

        ResponseEntity<UserDto> response = userController.addUser(userDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(userDto, response.getBody());
    }

    @Test
    void updateUser_whenInvoked_thenResponseStatusOkWithUserDtoInBody() {
        UserDto userDto = new UserDto();
        when(userService.update(userDto)).thenReturn(userDto);

        ResponseEntity<UserDto> response = userController.updateUser(1L, userDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(userDto, response.getBody());
    }
}