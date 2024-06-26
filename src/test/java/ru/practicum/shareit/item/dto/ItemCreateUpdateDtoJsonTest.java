package ru.practicum.shareit.item.dto;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class ItemCreateUpdateDtoJsonTest {
    private static final String NAME = "name";
    private static final String DESCRIPTION = "description";
    private static final Boolean AVAILABLE = true;
    private static final Long REQUEST_ID = 1L;

    @Autowired
    private JacksonTester<ItemCreateUpdateDto> json;

    @SneakyThrows
    @Test
    void testSerialize() {
        ItemCreateUpdateDto request = new ItemCreateUpdateDto(NAME, DESCRIPTION, AVAILABLE, REQUEST_ID);

        JsonContent<ItemCreateUpdateDto> actual = json.write(request);

        assertThat(actual).hasJsonPath("$.name");
        assertThat(actual).hasJsonPath("$.description");
        assertThat(actual).hasJsonPath("$.available");
        assertThat(actual).hasJsonPath("$.requestId");
        assertThat(actual).extractingJsonPathStringValue("$.name").isEqualTo(NAME);
        assertThat(actual).extractingJsonPathStringValue("$.description").isEqualTo(DESCRIPTION);
        assertThat(actual).extractingJsonPathBooleanValue("$.available").isEqualTo(AVAILABLE);
        assertThat(actual).extractingJsonPathNumberValue("$.requestId").isEqualTo(1);
    }

    @SneakyThrows
    @Test
    void testDeserialize() {
        ItemCreateUpdateDto actual = json.parseObject("{" +
                "\"name\":\"name\"," +
                "\"description\":\"description\"," +
                "\"available\":true," +
                "\"requestId\":1" +
                "}");

        assertThat(actual).isNotNull();
        assertThat(actual.getName()).isEqualTo(NAME);
        assertThat(actual.getDescription()).isEqualTo(DESCRIPTION);
        assertThat(actual.getAvailable()).isEqualTo(AVAILABLE);
        assertThat(actual.getRequestId()).isEqualTo(REQUEST_ID);
    }
}