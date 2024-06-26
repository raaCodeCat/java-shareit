package ru.practicum.shareit.item.dto;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class ItemViewJsonTest {
    private static final Long ID = 10L;
    private static final String NAME = "name";
    private static final String DESCRIPTION = "description";
    private static final Boolean AVAILABLE = true;
    private static final Long REQUEST_ID = 1L;

    @Autowired
    private JacksonTester<ItemView> json;

    @SneakyThrows
    @Test
    void testSerialize() {
        ItemView view = new ItemView(ID, NAME, DESCRIPTION, AVAILABLE, null, null, List.of(), REQUEST_ID);

        JsonContent<ItemView> actual = json.write(view);

        assertThat(actual).hasJsonPath("$.id");
        assertThat(actual).hasJsonPath("$.name");
        assertThat(actual).hasJsonPath("$.description");
        assertThat(actual).hasJsonPath("$.available");
        assertThat(actual).hasJsonPath("$.lastBooking");
        assertThat(actual).hasJsonPath("$.nextBooking");
        assertThat(actual).hasJsonPath("$.comments");
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
        ItemView actual = json.parseObject("{" +
                "\"id\":10," +
                "\"name\":\"name\"," +
                "\"description\":\"description\"," +
                "\"available\":true," +
                "\"lastBooking\":null," +
                "\"nextBooking\":null," +
                "\"comments\":[]," +
                "\"requestId\":1" +
                "}");

        assertThat(actual).isNotNull();
        assertThat(actual.getId()).isEqualTo(ID);
        assertThat(actual.getName()).isEqualTo(NAME);
        assertThat(actual.getDescription()).isEqualTo(DESCRIPTION);
        assertThat(actual.getAvailable()).isEqualTo(AVAILABLE);
        assertThat(actual.getLastBooking()).isNull();
        assertThat(actual.getNextBooking()).isNull();
        assertThat(actual.getComments()).isEqualTo(List.of());
        assertThat(actual.getRequestId()).isEqualTo(REQUEST_ID);
    }
}