package ru.practicum.shareit.item.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ItemRepositoryIT {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemRepository itemRepository;

    private User user1;
    private User user2;
    private Item item1;
    private Item item2;

    @BeforeEach
    void setUp() {
        user1 = new User(null, "user", "user@email.com");
        userRepository.save(user1);
        user2 = new User(null, "user_2", "user_2@email.com");
        userRepository.save(user2);
        item1 = new Item(null, "name_item_1", "description_1", true, user1, null);
        itemRepository.save(item1);
        item2 = new Item(null, "name_item_2", "description_2", true, user1, null);
        itemRepository.save(item2);
    }

    @Test
    void findItemsByOwnerIdOrderById_whenUserHasItems_thenReturnNotEmptyCollection() {
        List<Item> items = itemRepository.findItemsByOwnerIdOrderById(user1.getId());

        assertNotNull(items);
        assertEquals(2, items.size());
        assertEquals(item1, items.get(0));
        assertEquals(item2, items.get(1));
    }

    @Test
    void findItemsByOwnerIdOrderById_whenUserDoesNotHasItems_thenReturnEmptyCollection() {
        List<Item> items = itemRepository.findItemsByOwnerIdOrderById(user2.getId());

        assertNotNull(items);
        assertEquals(0, items.size());
    }

    @Test
    void findItemBySearchText_whenItemsFoundByText_thenReturnNotEmptyCollection() {
        List<Item> items = itemRepository.findItemBySearchText("item_1");

        assertNotNull(items);
        assertEquals(1, items.size());
        assertEquals(item1, items.get(0));
    }

    @Test
    void findItemBySearchText_whenItemsNotFoundByText_thenReturnEmptyCollection() {
        List<Item> items = itemRepository.findItemBySearchText("foo");

        assertNotNull(items);
        assertEquals(0, items.size());
    }
}