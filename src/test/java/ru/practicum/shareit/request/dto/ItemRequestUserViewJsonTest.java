package ru.practicum.shareit.request.dto;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class ItemRequestUserViewJsonTest {
    private static final Long ID = 1L;
    private static final String DESCRIPTION = "This is description";
    private static final LocalDateTime CREATED = LocalDateTime.of(2024,5, 30, 10, 30, 30);

    @Autowired
    private JacksonTester<ItemRequestUserView> json;

    @SneakyThrows
    @Test
    void testSerialize() {
        ItemRequestUserView itemRequestUserView = new ItemRequestUserView(ID, DESCRIPTION, CREATED, List.of());

        JsonContent<ItemRequestUserView> actual = json.write(itemRequestUserView);

        assertThat(actual).hasJsonPath("$.id");
        assertThat(actual).hasJsonPath("$.description");
        assertThat(actual).hasJsonPath("$.created");
        assertThat(actual).hasJsonPath("$.items");
        assertThat(actual).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(actual).extractingJsonPathStringValue("$.description").isEqualTo(DESCRIPTION);
        assertThat(actual).extractingJsonPathValue("$.created").isEqualTo(CREATED.toString());
        assertThat(actual).extractingJsonPathArrayValue("$.items").isEqualTo(List.of());
    }

    @SneakyThrows
    @Test
    void testDeserialize() {
        ItemRequestUserView actual = json.parseObject("{" +
                "\"id\":1," +
                "\"description\":\"This is description\"," +
                "\"created\":\"2024-05-30T10:30:30\"," +
                "\"items\":[]" +
                "}");

        assertThat(actual).isNotNull();
        assertThat(actual.getId()).isEqualTo(ID);
        assertThat(actual.getDescription()).isEqualTo(DESCRIPTION);
        assertThat(actual.getCreated()).isEqualTo(CREATED);
        assertThat(actual.getItems()).isEqualTo(List.of());
    }
}