package ru.practicum.shareit.item.mapper;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.dto.ItemCreateUpdateDto;
import ru.practicum.shareit.item.dto.ItemResponseView;
import ru.practicum.shareit.item.dto.ItemView;
import ru.practicum.shareit.item.model.Item;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


class ItemMapperTest {

    @Test
    void toItemView() {
        Item item = new Item();
        ItemView itemView = new ItemView();

        ItemView actual = ItemMapper.toItemView(item);

        assertNotNull(actual);
        assertEquals(itemView, actual);
    }

    @Test
    void toCollectionOfItemView() {
        List<Item> items = List.of(new Item());
        List<ItemView> itemsView = List.of(new ItemView());

        List<ItemView> actual = ItemMapper.toItemView(items);

        assertNotNull(actual);
        assertEquals(1, actual.size());
        assertEquals(itemsView, actual);
    }

    @Test
    void fromItemCreateUpdateDto() {
        ItemCreateUpdateDto itemCreateUpdateDto = new ItemCreateUpdateDto();
        Item item = new Item();

        Item actual = ItemMapper.fromItemCreateUpdateDto(itemCreateUpdateDto);

        assertNotNull(actual);
        assertEquals(item, actual);
    }

    @Test
    void toItemResponseView() {
        Item item = new Item();
        ItemResponseView itemResponseView = new ItemResponseView();

        ItemResponseView actual = ItemMapper.toItemResponseView(item);

        assertNotNull(actual);
        assertEquals(itemResponseView, actual);
    }

    @Test
    void toCollectionOfItemResponseView() {
        List<Item> items = List.of(new Item());
        List<ItemResponseView> itemResponsesView = List.of(new ItemResponseView());

        List<ItemResponseView> actual = ItemMapper.toItemResponseView(items);

        assertNotNull(actual);
        assertEquals(1, actual.size());
        assertEquals(itemResponsesView, actual);
    }
}