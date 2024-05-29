package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.item.dto.CommentRequest;
import ru.practicum.shareit.item.dto.CommentView;
import ru.practicum.shareit.item.dto.ItemView;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.item.dto.ItemCreateUpdateDto;
import ru.practicum.shareit.item.model.Item;
import javax.validation.Valid;
import java.util.List;

/**
 * Контроллер для работы с {@link Item}.
 */
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Slf4j
public class ItemController {
    private static final String SHARER = "X-Sharer-User-Id";

    private final ItemService itemService;

    /**
     * Получение списка вещей пользователя.
     */
    @GetMapping
    public ResponseEntity<List<ItemView>> getUserItems(@RequestHeader(SHARER) Long userId) {
        log.info("Получен запрос GET /items, от пользователя {}.", userId);

        return ResponseEntity.ok(itemService.getUserItems(userId));
    }

    /**
     * Получение вещи по идентификатору (itemId).
     */
    @GetMapping("/{itemId}")
    public ResponseEntity<ItemView> getItemById(@RequestHeader(SHARER) Long userId, @PathVariable Long itemId) {
        log.info("Получен запрос GET /items/{}, от пользователя {}.", itemId, userId);

        return ResponseEntity.ok(itemService.getItemById(userId, itemId));
    }

    /**
     * Получение списка вещей по ключевому слову (text).
     */
    @GetMapping("/search")
    public ResponseEntity<List<ItemView>> searchItem(@RequestHeader(SHARER) Long userId, @RequestParam String text) {
        log.info("Получен запрос GET /items/search, от пользователя {}.", userId);

        return ResponseEntity.ok(itemService.searchItems(text));
    }

    /**
     * Добавление вещи.
     */
    @PostMapping
    public ResponseEntity<ItemView> addItem(@RequestHeader(SHARER) Long userId,
                            @RequestBody @Valid ItemCreateUpdateDto itemCreateUpdateDto) {
        log.info("Получен запрос POST /items, от пользователя {}.", userId);

        return ResponseEntity.ok(itemService.create(userId, itemCreateUpdateDto));
    }

    /**
     * Добавление отзыва.
     */
    @PostMapping("/{itemId}/comment")
    public ResponseEntity<CommentView> addComment(@RequestHeader(SHARER) Long userId,
                                  @PathVariable Long itemId,
                                  @RequestBody @Valid CommentRequest commentRequest) {
        log.info("Получен запрос POST /items/{}/comment, от пользователя {}.", itemId, userId);

        return ResponseEntity.ok(itemService.createComment(userId, itemId, commentRequest));
    }

    /**
     * Обновление вещи.
     */
    @PatchMapping("/{itemId}")
    public ResponseEntity<ItemView> updateItem(@RequestHeader(SHARER) Long userId,
                              @PathVariable Long itemId,
                              @RequestBody ItemCreateUpdateDto itemCreateUpdateDto) {
        log.info("Получен запрос PATCH /items, от пользователя {}.", userId);

        return ResponseEntity.ok(itemService.update(userId, itemId, itemCreateUpdateDto));
    }
}
