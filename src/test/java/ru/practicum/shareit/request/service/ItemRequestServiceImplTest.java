package ru.practicum.shareit.request.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.exeption.NotFoundException;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestUserView;
import ru.practicum.shareit.request.dto.ItemRequestView;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ItemRequestServiceImplTest {
    @Mock
    ItemRequestRepository itemRequestRepository;
    @Mock
    UserRepository userRepository;

    @InjectMocks
    ItemRequestServiceImpl itemRequestService;

    @Test
    void create_whenInvoked_thenReturnItemRequestView() {
        final LocalDateTime dateTime = LocalDateTime.now();
        final User user = new User();
        final ItemRequestDto requestDto = new ItemRequestDto();
        final ItemRequest itemRequest = ItemRequestMapper.makeItemRequest(user, requestDto, dateTime);
        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(user));
        when(itemRequestRepository.save(Mockito.any(ItemRequest.class))).thenReturn(itemRequest);

        final ItemRequestView actual = itemRequestService.create(1L, requestDto);

        assertNotNull(actual);
        assertEquals(ItemRequestMapper.toItemRequestView(itemRequest), actual);

        verify(userRepository, times(1)).findById(Mockito.anyLong());
        verify(itemRequestRepository, times(1)).save(Mockito.any(ItemRequest.class));
    }

    @Test
    void create_whenInvalidUserId_thenNotFoundExceptionThrown() {
        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());

        final NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> itemRequestService.create(1L, new ItemRequestDto())
        );

        assertNotNull(exception);
        assertEquals("Пользователь c идентификатором 1 не найден", exception.getMessage());

        verify(userRepository, times(1)).findById(Mockito.anyLong());
        verify(itemRequestRepository, never()).save(Mockito.any(ItemRequest.class));
    }

    @Test
    void getItemRequestsByUserId_whenInvoked_thenReturnCollectionItemRequestView() {
        final User user = new User();
        user.setId(1L);
        final List<ItemRequest> itemRequests = List.of(new ItemRequest());
        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(user));
        when(itemRequestRepository.findItemRequestsByUserId(Mockito.anyLong())).thenReturn(itemRequests);

        final List<ItemRequestUserView> actual = itemRequestService.getItemRequestsByUserId(1L);

        assertNotNull(actual);
        assertEquals(ItemRequestMapper.toItemRequestUserView(itemRequests), actual);

        verify(userRepository, times(1)).findById(Mockito.anyLong());
        verify(itemRequestRepository, times(1)).findItemRequestsByUserId(Mockito.anyLong());
    }

    @Test
    void getItemRequestsByUserId_whenInvalidUserId_thenNotFountExceptionThrown() {
        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());

        final NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> itemRequestService.create(1L, new ItemRequestDto())
        );

        assertNotNull(exception);
        assertEquals("Пользователь c идентификатором 1 не найден", exception.getMessage());

        verify(userRepository, times(1)).findById(Mockito.anyLong());
        verify(itemRequestRepository, never()).findItemRequestsByUserId(Mockito.anyLong());
    }

    @Test
    void getItemRequestById_whenInvoked_thenReturnItemRequestView() {
        final ItemRequest itemRequest = new ItemRequest();
        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(new User()));
        when(itemRequestRepository.findItemRequestsById(Mockito.anyLong())).thenReturn(Optional.of(itemRequest));

        final ItemRequestUserView actual = itemRequestService.getItemRequestById(1L, 1L);

        assertNotNull(actual);
        assertEquals(ItemRequestMapper.toItemRequestUserView(itemRequest), actual);

        verify(userRepository, times(1)).findById(Mockito.anyLong());
        verify(itemRequestRepository, times(1)).findItemRequestsById(Mockito.anyLong());
    }

    @Test
    void getItemRequestById_whenInvalidUserId_thenNotFoundExceptionThrown() {
        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());

        final NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> itemRequestService.getItemRequestById(1L, 1L)
        );

        assertNotNull(exception);
        assertEquals("Пользователь c идентификатором 1 не найден", exception.getMessage());

        verify(userRepository, times(1)).findById(Mockito.anyLong());
        verify(itemRequestRepository, never()).findItemRequestsById(Mockito.anyLong());
    }

    @Test
    void getItemRequestById_whenInvalidItemRequestId_thenNotFoundExceptionThrown() {
        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(new User()));
        when(itemRequestRepository.findItemRequestsById(Mockito.anyLong())).thenReturn(Optional.empty());


        final NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> itemRequestService.getItemRequestById(1L, 1L)
        );

        assertNotNull(exception);
        assertEquals("Запрос c идентификатором 1 не найден", exception.getMessage());

        verify(userRepository, times(1)).findById(Mockito.anyLong());
        verify(itemRequestRepository, times(1)).findItemRequestsById(Mockito.anyLong());
    }

    @Test
    void getItemRequestsPageable_whenInvokedWithNotNullFromAndSize_thenReturnCollectionItemRequestUserView() {
        final User user = new User();
        user.setId(1L);
        final List<ItemRequest> itemRequests = List.of(new ItemRequest());
        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(user));
        when(itemRequestRepository.findItemRequestsExcludingOwnPageable(Mockito.anyLong(),
                Mockito.any(Pageable.class))).thenReturn(itemRequests);

        List<ItemRequestUserView> actual = itemRequestService.getItemRequestsPageable(1L, 0, 1);

        assertNotNull(actual);
        assertEquals(ItemRequestMapper.toItemRequestUserView(itemRequests), actual);

        verify(userRepository, times(1)).findById(Mockito.anyLong());
        verify(itemRequestRepository, times(1)).findItemRequestsExcludingOwnPageable(Mockito.anyLong(),
                Mockito.any(Pageable.class));
    }

    @Test
    void getItemRequestsPageable_whenInvokedWitNullFromOrSize_thenReturnEmptyList() {
        final User user = new User();
        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(user));

        List<ItemRequestUserView> actual = itemRequestService.getItemRequestsPageable(1L, null, null);

        assertNotNull(actual);
        assertEquals(0, actual.size());

        verify(userRepository, times(1)).findById(Mockito.anyLong());
        verify(itemRequestRepository, never()).findItemRequestsExcludingOwnPageable(Mockito.anyLong(),
                Mockito.any(Pageable.class));
    }

    @Test
    void getItemRequestsPageable_whenInvalidUserId_thenNotFoundExceptionThrown() {
        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());

        final NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> itemRequestService.getItemRequestsPageable(1L, 0, 1)
        );

        assertNotNull(exception);
        assertEquals("Пользователь c идентификатором 1 не найден", exception.getMessage());

        verify(userRepository, times(1)).findById(Mockito.anyLong());
        verify(itemRequestRepository, never()).findItemRequestsExcludingOwnPageable(Mockito.anyLong(),
                Mockito.any(Pageable.class));
    }

}