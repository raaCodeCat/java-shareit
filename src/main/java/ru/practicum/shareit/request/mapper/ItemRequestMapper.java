package ru.practicum.shareit.request.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.item.dto.ItemResponseView;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestUserView;
import ru.practicum.shareit.request.dto.ItemRequestView;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ItemRequestMapper {
    public static ItemRequestView toItemRequestView(ItemRequest source) {
        return new ItemRequestView(source.getId(), source.getDescription(), source.getCreated());
    }

    public static ItemRequest makeItemRequest(User user, ItemRequestDto source, LocalDateTime dateTime) {
        return new ItemRequest(null, source.getDescription(), user, dateTime, new ArrayList<>());
    }

    public static ItemRequestUserView toItemRequestUserView(ItemRequest source) {
        List<Item> items = source.getItems();
        List<ItemResponseView> itemResponseViews = items.size() > 0 ?
                ItemMapper.toItemResponseView(items) : Collections.emptyList();

        return new ItemRequestUserView(source.getId(), source.getDescription(), source.getCreated(),
                itemResponseViews);
    }

    public static List<ItemRequestUserView> toItemRequestUserView(List<ItemRequest> source) {
        return source.stream()
                .map(ItemRequestMapper::toItemRequestUserView)
                .collect(Collectors.toList());
    }
}
