package ru.practicum.shareit.user.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserMapper {
    public static UserDto toUserDto(User source) {
        return new UserDto(source.getId(), source.getName(), source.getEmail());
    }

    public static List<UserDto> toUserDto(List<User> source) {
        return source.stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }

    public static User fromUserDto(UserDto source) {
        return new User(null, source.getName(), source.getEmail());
    }
}
