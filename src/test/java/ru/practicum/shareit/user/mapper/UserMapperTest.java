package ru.practicum.shareit.user.mapper;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserMapperTest {

    @Test
    void toUserDto() {
        User user = new User();
        UserDto userDto = new UserDto();

        UserDto actual = UserMapper.toUserDto(user);

        assertNotNull(actual);
        assertEquals(userDto, actual);
    }

    @Test
    void toUserDtoList() {
        List<User> user = List.of(new User());
        List<UserDto> userDto = List.of(new UserDto());

        List<UserDto> actual = UserMapper.toUserDto(user);

        assertNotNull(actual);
        assertEquals(1, actual.size());
        assertEquals(userDto, actual);
    }

    @Test
    void fromUserDto() {
        User user = new User();
        UserDto userDto = new UserDto();

        User actual = UserMapper.fromUserDto(userDto);

        assertNotNull(actual);
        assertEquals(user, actual);
    }
}