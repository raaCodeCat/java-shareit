package ru.practicum.shareit.request.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestUserView;
import ru.practicum.shareit.request.dto.ItemRequestView;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.service.ItemRequestService;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.List;

/**
 * Контроллер для работы с {@link ItemRequest}.
 */
@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Validated
public class ItemRequestController {
    private static final String USER = "X-Sharer-User-Id";

    private final ItemRequestService itemRequestService;

    /**
     * Создание запроса вещи.
     */
    @PostMapping
    public ResponseEntity<ItemRequestView> addItemRequest(@RequestHeader(USER) Long userId,
                                          @RequestBody @Valid ItemRequestDto requestDto) {
        return ResponseEntity.ok(itemRequestService.create(userId, requestDto));
    }

    /**
     * Получение списка запросов пользователя вместе с данными об ответах на них.
     */
    @GetMapping
    public ResponseEntity<List<ItemRequestUserView>> getUserItemRequest(@RequestHeader(USER) Long userId) {
        return ResponseEntity.ok(itemRequestService.getItemRequestsByUserId(userId));
    }

    /**
     * Получить данные по запросу по идентификатору (id).
     */
    @GetMapping("/{requestId}")
    public ResponseEntity<ItemRequestUserView> getItemRequestById(@RequestHeader(USER) Long userId,
                                                  @PathVariable Long requestId) {
        return ResponseEntity.ok(itemRequestService.getItemRequestById(userId, requestId));
    }

    @GetMapping("/all")
    public ResponseEntity<List<ItemRequestUserView>> getItemRequestsPageable(
            @RequestHeader(USER) Long userId,
            @RequestParam(required = false) @Min(0) Integer from,
            @RequestParam(required = false) @Min(1) @Max(20) Integer size
    ) {
        return ResponseEntity.ok(itemRequestService.getItemRequestsPageable(userId, from, size));
    }
}
