package ru.practicum.shareit.request.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;


@DataJpaTest
class ItemRequestRepositoryIT {
    @Autowired
    private ItemRequestRepository itemRequestRepository;

    @Autowired
    private UserRepository userRepository;

    private User user2;
    private User user3;
    private ItemRequest itemRequest1;
    private ItemRequest itemRequest2;
    private LocalDateTime created;

    @BeforeEach
    void setUp() {
        created = LocalDateTime.of(2024,5,20,11,20);
        User user1 = new User(null, "user_1", "user_1@email.com");
        user2 = new User(null, "user_2", "user_2@email.com");
        user3 = new User(null, "user_3", "user_3@email.com");
        userRepository.save(user1);
        userRepository.save(user2);
        userRepository.save(user3);
        itemRequest1 = new ItemRequest(null, "request_description_1", user2, created, null);
        itemRequest2 = new ItemRequest(null, "request_description_2", user2, created.plusDays(1), null);
        ItemRequest itemRequest3 = new ItemRequest(null, "request_description_3", user3, created, null);
        itemRequestRepository.save(itemRequest1);
        itemRequestRepository.save(itemRequest2);
        itemRequestRepository.save(itemRequest3);
    }

    @Test
    void findItemRequestsByUserId_whenUserHasRequests_thenReturnNotEmptyCollection() {
        List<ItemRequest> itemRequests = itemRequestRepository.findItemRequestsByUserId(
                user2.getId());

        assertNotNull(itemRequests);
        assertEquals(2, itemRequests.size());
        assertEquals(itemRequest2.getId(), itemRequests.get(0).getId());
        assertEquals("request_description_2", itemRequests.get(0).getDescription());
        assertEquals(user2.getId(), itemRequests.get(0).getUser().getId());
        assertEquals(created.plusDays(1), itemRequests.get(0).getCreated());
        assertEquals(itemRequest1.getId(), itemRequests.get(1).getId());
        assertEquals("request_description_1", itemRequests.get(1).getDescription());
        assertEquals(user2.getId(), itemRequests.get(1).getUser().getId());
        assertEquals(created, itemRequests.get(1).getCreated());
    }

    @Test
    void findItemRequestsByUserId_whenUserDoesNotHasRequests_thenReturnEmptyCollection() {
        List<ItemRequest> itemRequests = itemRequestRepository.findItemRequestsByUserId(1L);

        assertNotNull(itemRequests);
        assertEquals(0, itemRequests.size());
    }

    @Test
    void findItemRequestsById_whenItemRequestIdIsValid_returnNotEmptyOptional() {
        Optional<ItemRequest> itemRequest = itemRequestRepository.findItemRequestsById(
                itemRequest2.getId());

        assertNotNull(itemRequest);
        assertTrue(itemRequest.isPresent());
        assertEquals(itemRequest2.getId(), itemRequest.get().getId());
        assertEquals("request_description_2", itemRequest.get().getDescription());
        assertEquals(user2.getId(), itemRequest.get().getUser().getId());
        assertEquals(created.plusDays(1), itemRequest.get().getCreated());
    }

    @Test
    void findItemRequestsById_whenItemRequestIdIsNotValid_returnEmptyOptional() {
        Optional<ItemRequest> itemRequest = itemRequestRepository.findItemRequestsById(100L);

        assertNotNull(itemRequest);
        assertTrue(itemRequest.isEmpty());
    }

    @Test
    void findItemRequestsExcludingOwnPageable() {
        Pageable pageable = PageRequest.of(0, 1);

        List<ItemRequest> itemRequests = itemRequestRepository
                .findItemRequestsExcludingOwnPageable(user3.getId(), pageable);

        assertNotNull(itemRequests);
        assertEquals(1, itemRequests.size());
        assertEquals(itemRequest2.getId(), itemRequests.get(0).getId());
        assertEquals("request_description_2", itemRequests.get(0).getDescription());
        assertEquals(user2.getId(), itemRequests.get(0).getUser().getId());
        assertEquals(created.plusDays(1), itemRequests.get(0).getCreated());
    }
}