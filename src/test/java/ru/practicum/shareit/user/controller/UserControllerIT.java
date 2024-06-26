package ru.practicum.shareit.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.exeption.ConflictException;
import ru.practicum.shareit.exeption.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class UserControllerIT {
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;


    @SneakyThrows
    @Test
    void getAllUsers_whenValid_thenResponseStatusOkAndCollectionUserDtoInBody() {
        List<UserDto> users = List.of(new UserDto());
        when(userService.get()).thenReturn(users);

        String result = mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertNotNull(result);
        assertEquals(objectMapper.writeValueAsString(users), result);

        verify(userService, times(1)).get();
    }

    @SneakyThrows
    @Test
    void getUserById_whenValid_thenResponseStatusOkAndUserDtoInBody() {
        Long userId = 1L;
        UserDto user = new UserDto();
        when(userService.getById(userId)).thenReturn(user);

        String result = mockMvc.perform(get("/users/{id}", userId))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertNotNull(result);
        assertEquals(objectMapper.writeValueAsString(user), result);

        verify(userService, times(1)).getById(userId);
    }

    @SneakyThrows
    @Test
    void getUserById_whenUserIdNotValid_thenResponseStatusNotFound() {
        Long userId = 1L;
        when(userService.getById(userId)).thenThrow(new NotFoundException(""));

        mockMvc.perform(get("/users/{id}", userId))
                .andExpect(status().isNotFound());

        verify(userService, times(1)).getById(userId);
    }

    @SneakyThrows
    @Test
    void addUser_whenUserDtoIsValid_thenResponseStatusOkAndReturnUserDto() {
        UserDto userToCreate = new UserDto();
        userToCreate.setName("name");
        userToCreate.setEmail("email@server.com");
        when(userService.create(userToCreate)).thenReturn(userToCreate);

        String result = mockMvc.perform(post("/users").contentType("application/json")
                .content(objectMapper.writeValueAsString(userToCreate)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(userToCreate), result);

        verify(userService, times(1)).create(userToCreate);
    }

    @SneakyThrows
    @Test
    void addUser_whenEmailAlreadyExists_thenResponseStatusConflict() {
        UserDto userToCreate = new UserDto();
        userToCreate.setName("name");
        userToCreate.setEmail("email@server.com");
        when(userService.create(userToCreate)).thenThrow(new ConflictException(""));

        mockMvc.perform(post("/users").contentType("application/json")
                        .content(objectMapper.writeValueAsString(userToCreate)))
                .andExpect(status().isConflict());

        verify(userService, times(1)).create(userToCreate);
    }

    @SneakyThrows
    @Test
    void addUser_whenUserDtoIsNotValid_thenResponseStatusBadRequest() {
        UserDto userToCreate = new UserDto();
        userToCreate.setName(null);
        userToCreate.setEmail(null);

        mockMvc.perform(post("/users").contentType("application/json")
                        .content(objectMapper.writeValueAsString(userToCreate)))
                .andExpect(status().isBadRequest());

        verify(userService, never()).create(userToCreate);
    }

    @SneakyThrows
    @Test
    void updateUser_whenInvoked_thenResponseStatusOkAndReturnUserDto() {
        Long userId = 1L;
        UserDto userToUpdate = new UserDto();
        userToUpdate.setId(userId);
        when(userService.update(userToUpdate)).thenReturn(userToUpdate);

        String result = mockMvc.perform(patch("/users/{id}", userId)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(userToUpdate)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(userToUpdate), result);

        verify(userService, times(1)).update(Mockito.any(UserDto.class));
    }

    @SneakyThrows
    @Test
    void updateUser_whenUserIdNotValid_thenResponseStatusNotFound() {
        Long userId = 1L;
        UserDto userToUpdate = new UserDto();
        userToUpdate.setId(userId);
        when(userService.update(userToUpdate)).thenThrow(new NotFoundException(""));

        mockMvc.perform(patch("/users/{id}", userId)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(userToUpdate)))
                .andExpect(status().isNotFound());

        verify(userService, times(1)).update(Mockito.any(UserDto.class));
    }

    @SneakyThrows
    @Test
    void updateUser_whenEmailAlreadyExists_thenResponseStatusConflict() {
        Long userId = 1L;
        UserDto userToUpdate = new UserDto();
        userToUpdate.setId(userId);
        when(userService.update(userToUpdate)).thenThrow(new ConflictException(""));

        mockMvc.perform(patch("/users/{id}", userId)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(userToUpdate)))
                .andExpect(status().isConflict());

        verify(userService, times(1)).update(Mockito.any(UserDto.class));
    }

    @SneakyThrows
    @Test
    void deleteUser_whenValid_thenResponseStatusOk() {
        Long userId = 1L;
        mockMvc.perform(delete("/users/{id}", userId))
                .andExpect(status().isOk());

        verify(userService, times(1)).delete(userId);
    }
}