package ru.practicum.shareit.user.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserDtoTest {
    private static final Long ID = 1L;
    private static final String NAME = "name";
    private static final String EMAIL = "email@email.email";

    @Test
    void stateIsCorrect() {
        UserDto userDto = new UserDto(ID, NAME, EMAIL);

        assertEquals(ID, userDto.getId());
        assertEquals(NAME, userDto.getName());
        assertEquals(EMAIL, userDto.getEmail());
    }

    @Test
    void equalsTwoObjectsWithSameValues() {
        UserDto userDto = new UserDto(ID, NAME, EMAIL);

        assertEquals(userDto, new UserDto(ID, NAME, EMAIL));
    }

    @Test
    void notEqualsTwoObjectsWithDifferentId() {
        UserDto userDto = new UserDto(ID, NAME, EMAIL);

        assertNotEquals(userDto, new UserDto(2L, NAME, EMAIL));
    }

    @Test
    void notEqualsTwoObjectsWithDifferentName() {
        UserDto userDto = new UserDto(ID, "AnotherName", EMAIL);

        assertNotEquals(userDto, new UserDto(ID, NAME, EMAIL));
    }

    @Test
    void notEqualsTwoObjectsWithDifferentEmail() {
        UserDto userDto = new UserDto(ID, NAME, "AnotherEmail");

        assertNotEquals(userDto, new UserDto(ID, NAME, EMAIL));
    }

    @Test
    void setId() {
        UserDto userDto = new UserDto();
        userDto.setId(ID);

        assertEquals(ID, userDto.getId());
    }

    @Test
    void setName() {
        UserDto userDto = new UserDto();
        userDto.setName(NAME);

        assertEquals(NAME, userDto.getName());
    }

    @Test
    void setEmail() {
        UserDto userDto = new UserDto();
        userDto.setEmail(EMAIL);

        assertEquals(EMAIL, userDto.getEmail());
    }

    @Test
    void testToString() {
        UserDto userDto = new UserDto(ID, NAME, EMAIL);

        assertEquals("UserDto(id=1, name=name, email=email@email.email)",
                userDto.toString());
    }
}