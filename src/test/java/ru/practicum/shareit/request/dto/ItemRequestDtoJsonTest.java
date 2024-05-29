package ru.practicum.shareit.request.dto;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class ItemRequestDtoJsonTest {
    private static final String DESCRIPTION = "This is description";

    @Autowired
    private JacksonTester<ItemRequestDto> json;

    @SneakyThrows
    @Test
    void testSerialize() {
        ItemRequestDto itemRequestDto = new ItemRequestDto(DESCRIPTION);

        JsonContent<ItemRequestDto> actual = json.write(itemRequestDto);

        assertThat(actual).hasJsonPath("$.description");
        assertThat(actual).extractingJsonPathStringValue("$.description").isEqualTo(DESCRIPTION);
    }

    @SneakyThrows
    @Test
    void testDeserialize() {
        ItemRequestDto actual = json.parseObject("{" +
                "\"description\":\"This is description\"" +
                "}");

        assertThat(actual).isNotNull();
        assertThat(actual.getDescription()).isEqualTo(DESCRIPTION);
    }
}