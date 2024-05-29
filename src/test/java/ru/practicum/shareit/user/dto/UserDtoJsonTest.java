package ru.practicum.shareit.user.dto;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class UserDtoJsonTest {
    private static final Long ID = 1L;
    private static final String NAME = "name";
    private static final String EMAIL = "email@email.email";

    @Autowired
    private JacksonTester<UserDto> json;

    @SneakyThrows
    @Test
    void testSerialize() {
        UserDto userDto = new UserDto(ID, NAME, EMAIL);

        JsonContent<UserDto> actual = json.write(userDto);

        assertThat(actual).hasJsonPath("$.id");
        assertThat(actual).hasJsonPath("$.name");
        assertThat(actual).hasJsonPath("$.email");
        assertThat(actual).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(actual).extractingJsonPathStringValue("$.name").isEqualTo(NAME);
        assertThat(actual).extractingJsonPathStringValue("$.email").isEqualTo(EMAIL);
    }

    @SneakyThrows
    @Test
    void testDeserialize() {
        UserDto actual = json.parseObject("{" +
                "\"id\":1," +
                "\"name\":\"name\"," +
                "\"email\":\"email@email.email\"" +
                "}");

        assertThat(actual).isNotNull();
        assertThat(actual.getId()).isEqualTo(ID);
        assertThat(actual.getName()).isEqualTo(NAME);
        assertThat(actual.getEmail()).isEqualTo(EMAIL);
    }
}