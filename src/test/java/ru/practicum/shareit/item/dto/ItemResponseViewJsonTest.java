package ru.practicum.shareit.item.dto;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class ItemResponseViewJsonTest {
    private static final Long ID = 10L;
    private static final String NAME = "name";
    private static final String DESCRIPTION = "description";
    private static final Boolean AVAILABLE = true;
    private static final Long REQUEST_ID = 1L;

    @Autowired
    private JacksonTester<ItemResponseView> json;

    @SneakyThrows
    @Test
    void testSerialize() {
        ItemResponseView response = new ItemResponseView(ID, NAME, DESCRIPTION, AVAILABLE, REQUEST_ID);

        JsonContent<ItemResponseView> actual = json.write(response);

        assertThat(actual).hasJsonPath("$.id");
        assertThat(actual).hasJsonPath("$.name");
        assertThat(actual).hasJsonPath("$.description");
        assertThat(actual).hasJsonPath("$.available");
        assertThat(actual).hasJsonPath("$.requestId");
        assertThat(actual).extractingJsonPathNumberValue("$.id").isEqualTo(10);
        assertThat(actual).extractingJsonPathStringValue("$.name").isEqualTo(NAME);
        assertThat(actual).extractingJsonPathStringValue("$.description").isEqualTo(DESCRIPTION);
        assertThat(actual).extractingJsonPathBooleanValue("$.available").isEqualTo(AVAILABLE);
        assertThat(actual).extractingJsonPathNumberValue("$.requestId").isEqualTo(1);
    }

    @SneakyThrows
    @Test
    void testDeserialize() {
        ItemResponseView actual = json.parseObject("{" +
                "\"id\":10," +
                "\"name\":\"name\"," +
                "\"description\":\"description\"," +
                "\"available\":true," +
                "\"requestId\":1" +
                "}");

        assertThat(actual).isNotNull();
        assertThat(actual.getId()).isEqualTo(ID);
        assertThat(actual.getName()).isEqualTo(NAME);
        assertThat(actual.getDescription()).isEqualTo(DESCRIPTION);
        assertThat(actual.getAvailable()).isEqualTo(AVAILABLE);
        assertThat(actual.getRequestId()).isEqualTo(REQUEST_ID);
    }

}