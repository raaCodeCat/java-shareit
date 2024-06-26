package ru.practicum.shareit.item.dto;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class CommentRequestJsonTest {
    private static final String TEXT = "text";

    @Autowired
    private JacksonTester<CommentRequest> json;

    @SneakyThrows
    @Test
    void testSerialize() {
        CommentRequest commentRequest = new CommentRequest(TEXT);

        JsonContent<CommentRequest> actual = json.write(commentRequest);

        assertThat(actual).hasJsonPath("$.text");
        assertThat(actual).extractingJsonPathStringValue("$.text").isEqualTo(TEXT);
    }

    @SneakyThrows
    @Test
    void testDeserialize() {
        CommentRequest actual = json.parseObject("{" +
                "\"text\":\"text\"" +
                "}");

        assertThat(actual).isNotNull();
        assertThat(actual.getText()).isEqualTo(TEXT);
    }
}