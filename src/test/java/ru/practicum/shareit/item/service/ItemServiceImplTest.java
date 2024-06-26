package ru.practicum.shareit.item.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exeption.BadRequestException;
import ru.practicum.shareit.exeption.NotFoundException;
import ru.practicum.shareit.item.dto.CommentRequest;
import ru.practicum.shareit.item.dto.CommentView;
import ru.practicum.shareit.item.dto.ItemCreateUpdateDto;
import ru.practicum.shareit.item.dto.ItemView;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
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
class ItemServiceImplTest {
    @Mock
    private ItemRepository itemRepository;
    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private CommentRepository commentRepository;
    @Mock
    private ItemRequestRepository itemRequestRepository;

    @InjectMocks
    private ItemServiceImpl itemService;

    @Test
    void getUserItems_whenInvalidUserId_thenNotFoundExceptionThrown() {
        when(userRepository.findById(1L))
                .thenReturn(Optional.empty());

        final NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> itemService.getUserItems(1L)
        );

        assertNotNull(exception);
        assertEquals("Пользователь с идентификатором 1 не найден",
                exception.getMessage());

        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void getUserItems_whenUserItemListEmpty_thenReturnEmptyList() {
        when(userRepository.findById((1L)))
                .thenReturn(Optional.of(new User()));

        final List<ItemView> actual = itemService.getUserItems(1L);

        assertNotNull(actual);
        assertEquals(0, actual.size());

        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void getUserItems_whenInvoked_thenReturnItemList() {
        final User user = new User();
        user.setId(1L);
        final Item item = new Item();
        item.setId(11L);

        when(userRepository.findById((1L)))
                .thenReturn(Optional.of(user));

        when(itemRepository.findItemsByOwnerIdOrderById(1L))
                .thenReturn(List.of(item));

        final List<ItemView> actual = itemService.getUserItems(1L);

        assertNotNull(actual);
        assertEquals(1, actual.size());

        verify(userRepository, times(1)).findById(1L);
        verify(itemRepository, times(1)).findItemsByOwnerIdOrderById(1L);
    }

    @Test
    void getItemById_whenInvalidItemId_thenNotFoundExceptionThrown() {
        when(itemRepository.findById(1L)).thenReturn(Optional.empty());

        final NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> itemService.getItemById(1L, 1L)
        );

        assertNotNull(exception);
        assertEquals("Вещь с идентификатором 1 не найдена",
                exception.getMessage());

        verify(itemRepository, times(1)).findById(1L);
        verify(bookingRepository, never()).findLastBookingByItemIds(Mockito.anyList(),
                Mockito.any(LocalDateTime.class));
        verify(bookingRepository, never()).findNextBookingByItemIds(Mockito.anyList(),
                Mockito.any(LocalDateTime.class));
        verify(commentRepository, never()).findCommentsByItemIds(Mockito.anyList());
    }

    @Test
    void getItemById_whenInvokedFromOwner_thenReturnItemViewWithBookingInfo() {
        final User owner = new User();
        final Item item = new Item();
        owner.setId(1L);
        item.setId(1L);
        item.setOwner(owner);
        final ItemView itemView = ItemMapper.toItemView(item);
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));

        final ItemView actual = itemService.getItemById(1L, 1L);

        assertNotNull(actual);
        assertEquals(itemView, actual);

        verify(itemRepository, times(1)).findById(1L);
        verify(bookingRepository, times(1)).findLastBookingByItemIds(Mockito.anyList(),
                Mockito.any(LocalDateTime.class));
        verify(bookingRepository, times(1)).findNextBookingByItemIds(Mockito.anyList(),
                Mockito.any(LocalDateTime.class));
        verify(commentRepository, times(1)).findCommentsByItemIds(Mockito.anyList());
    }

    @Test
    void getItemById_whenInvokedFromNotOwner_thenReturnItemViewWithoutBookingInfo() {
        final User owner = new User();
        final Item item = new Item();
        owner.setId(1L);
        item.setId(1L);
        item.setOwner(owner);
        final ItemView itemView = ItemMapper.toItemView(item);
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));

        final ItemView actual = itemService.getItemById(2L, 1L);

        assertNotNull(actual);
        assertEquals(itemView, actual);

        verify(itemRepository, times(1)).findById(1L);
        verify(bookingRepository, never()).findLastBookingByItemIds(Mockito.anyList(),
                Mockito.any(LocalDateTime.class));
        verify(bookingRepository, never()).findNextBookingByItemIds(Mockito.anyList(),
                Mockito.any(LocalDateTime.class));
        verify(commentRepository, times(1)).findCommentsByItemIds(Mockito.anyList());
    }

    @Test
    void searchItems_whenInvoked_thenReturnItemViewList() {
        final List<Item> items = List.of(new Item());
        final List<ItemView> itemViews = ItemMapper.toItemView(items);
        when(itemRepository.findItemBySearchText(Mockito.anyString()))
                .thenReturn(items);

        final List<ItemView> actual = itemService.searchItems("text");

        assertNotNull(actual);
        assertEquals(1, actual.size());
        assertEquals(itemViews, actual);
    }

    @Test
    void searchItems_whenSearchTextIsBlank_thenReturnEmptyList() {
        final List<ItemView> actual = itemService.searchItems("");

        assertNotNull(actual);
        assertEquals(0, actual.size());
    }

    @Test
    void create_whenInvokedWithInvalidUserId_thenNotFoundExceptionThrownAndNotCreatItem() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        final NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> itemService.getUserItems(1L)
        );

        assertNotNull(exception);
        assertEquals("Пользователь с идентификатором 1 не найден",
                exception.getMessage());

        verify(userRepository, Mockito.times(1)).findById(1L);
        verify(itemRepository, never()).save(Mockito.any(Item.class));
    }

    @Test
    void create_whenInvokedWithoutRequestId_thenCreatItemAndReturnItemView() {
        final User user = new User();
        final ItemCreateUpdateDto itemCreateUpdateDto = new ItemCreateUpdateDto();
        final Item item = ItemMapper.fromItemCreateUpdateDto(itemCreateUpdateDto);
        item.setOwner(user);

        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(user));
        when(itemRepository.save(item)).thenReturn(item);

        ItemView actual = itemService.create(1L, itemCreateUpdateDto);

        assertEquals(ItemMapper.toItemView(item), actual);

        verify(userRepository, times(1)).findById(Mockito.anyLong());
        verify(itemRequestRepository, never()).findById(Mockito.anyLong());
        verify(itemRepository, times(1)).save(item);
    }

    @Test
    void create_whenInvokedWithRequestId_thenCreatItemAndReturnItemView() {
        final User user = new User();
        ItemCreateUpdateDto itemCreateUpdateDto = new ItemCreateUpdateDto();
        itemCreateUpdateDto.setRequestId(1L);
        final ItemRequest itemRequest = new ItemRequest();
        itemRequest.setId(1L);
        final Item item = ItemMapper.fromItemCreateUpdateDto(itemCreateUpdateDto);
        item.setRequest(itemRequest);
        item.setOwner(user);

        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(user));
        when(itemRequestRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(itemRequest));
        when(itemRepository.save(item)).thenReturn(item);

        final ItemView actual = itemService.create(1L, itemCreateUpdateDto);

        assertEquals(ItemMapper.toItemView(item), actual);

        verify(userRepository, times(1)).findById(Mockito.anyLong());
        verify(itemRequestRepository, times(1)).findById(Mockito.anyLong());
        verify(itemRepository, times(1)).save(item);
    }

    @Test
    void create_whenInvokedWithInvalidRequestId_thenNotFoundExceptionThrown() {
        final User user = new User();
        final ItemCreateUpdateDto itemCreateUpdateDto = new ItemCreateUpdateDto();
        itemCreateUpdateDto.setRequestId(1L);

        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(user));
        when(itemRequestRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());

        final NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> itemService.create(1L, itemCreateUpdateDto)
        );

        assertNotNull(exception);
        assertEquals("Запрос вещи с идентификатором 1 не найден", exception.getMessage());

        verify(userRepository, times(1)).findById(Mockito.anyLong());
        verify(itemRequestRepository, times(1)).findById(Mockito.anyLong());
        verify(itemRepository, never()).save(Mockito.any(Item.class));
    }

    @Test
    void update_whenInvalidItemId_thenNotFoundExceptionThrown() {
        when(itemRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());

        final NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> itemService.update(1L, 1L, new ItemCreateUpdateDto())
        );

        assertNotNull(exception);
        assertEquals("Вещь с идентификатором 1 не найдена", exception.getMessage());

        verify(itemRepository, times(1)).findById(1L);
        verify(itemRepository, never()).save(Mockito.any(Item.class));
    }

    @Test
    void update_whenInvalidUserId_thenNotFoundExceptionThrown() {
        when(itemRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(new Item()));
        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());

        final NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> itemService.update(1L, 1L, new ItemCreateUpdateDto())
        );

        assertNotNull(exception);
        assertEquals("Пользователь с идентификатором 1 не найден", exception.getMessage());

        verify(itemRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).findById(1L);
        verify(itemRepository, never()).save(Mockito.any(Item.class));
    }

    @Test
    void update_whenUserNotOwnerOfItem_thenNotFoundExceptionThrown() {
        final User user = new User();
        user.setId(1L);
        final User owner = new User();
        owner.setId(100L);
        final Item item = new Item();
        item.setId(1L);
        item.setOwner(owner);
        when(itemRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(item));
        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(user));

        final NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> itemService.update(1L, 1L, new ItemCreateUpdateDto())
        );

        assertNotNull(exception);
        assertEquals("Пользователь не владеет вещью с идентификатором 1", exception.getMessage());

        verify(itemRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).findById(1L);
        verify(itemRepository, never()).save(Mockito.any(Item.class));
    }

    @Test
    void update_whenInvoked_thenUpdateItemAndReturnItemView() {
        final User user = new User();
        user.setId(1L);
        final ItemCreateUpdateDto itemCreateUpdateDto = new ItemCreateUpdateDto();
        itemCreateUpdateDto.setName("name");
        itemCreateUpdateDto.setDescription("description");
        itemCreateUpdateDto.setAvailable(true);
        final Item item = new Item();
        item.setId(1L);
        item.setOwner(user);
        when(itemRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(item));
        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(user));
        when(itemRepository.save(item)).thenReturn(item);

        ItemView actual = itemService.update(1L, 1L, itemCreateUpdateDto);

        assertNotNull(actual);
        assertEquals(ItemMapper.toItemView(item), actual);

        verify(itemRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).findById(1L);
        verify(itemRepository, times(1)).save(Mockito.any(Item.class));
    }

    @Test
    void createComment_whenInvalidUserId_thenNotFoundExceptionThrown() {
        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());

        final NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> itemService.createComment(1L, 1L, new CommentRequest())
        );

        assertNotNull(exception);
        assertEquals("Пользователь с идентификатором 1 не найден", exception.getMessage());

        verify(userRepository, times(1)).findById(1L);
        verify(commentRepository, never()).save(Mockito.any(Comment.class));
    }

    @Test
    void createComment_whenInvalidItemId_thenNotFoundExceptionThrown() {
        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(new User()));
        when(itemRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());

        final NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> itemService.createComment(1L, 1L, new CommentRequest())
        );

        assertNotNull(exception);
        assertEquals("Вещь с идентификатором 1 не найдена", exception.getMessage());

        verify(userRepository, times(1)).findById(1L);
        verify(itemRepository, times(1)).findById(1L);
        verify(commentRepository, never()).save(Mockito.any(Comment.class));
    }

    @Test
    void createComment_whenUserNotHaveCompletedBooking_thenBadRequestExceptionThrown() {
        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(new User()));
        when(itemRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(new Item()));
        when(bookingRepository.findBookingForAllowCommentPageable(Mockito.anyLong(), Mockito.anyLong(),
                Mockito.any(LocalDateTime.class), Mockito.any(PageRequest.class))).thenReturn(List.of());

        final BadRequestException exception = assertThrows(
                BadRequestException.class,
                () -> itemService.createComment(1L, 1L, new CommentRequest())
        );

        assertNotNull(exception);
        assertEquals("У пользователя нет завершенного бронирования вещи с идентификатором 1",
                exception.getMessage());

        verify(userRepository, times(1)).findById(1L);
        verify(itemRepository, times(1)).findById(1L);
        verify(bookingRepository, times(1)).findBookingForAllowCommentPageable(Mockito.anyLong(),
                Mockito.anyLong(), Mockito.any(LocalDateTime.class), Mockito.any(PageRequest.class));
        verify(commentRepository, never()).save(Mockito.any(Comment.class));
    }

    @Test
    void createComment_whenInvoked_thenSaveCommentAndReturnCommentView() {
        CommentRequest commentRequest = new CommentRequest();
        Comment comment = new Comment();
        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(new User()));
        when(itemRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(new Item()));
        when(bookingRepository.findBookingForAllowCommentPageable(Mockito.anyLong(), Mockito.anyLong(),
                Mockito.any(LocalDateTime.class), Mockito.any(PageRequest.class))).thenReturn(List.of(new Booking()));
        when(commentRepository.save(Mockito.any(Comment.class))).thenReturn(comment);

        CommentView actual = itemService.createComment(1L, 1L, commentRequest);

        assertNotNull(actual);
        assertEquals(CommentMapper.toCommentView(comment), actual);

        verify(userRepository, times(1)).findById(1L);
        verify(itemRepository, times(1)).findById(1L);
        verify(bookingRepository, times(1)).findBookingForAllowCommentPageable(Mockito.anyLong(), Mockito.anyLong(),
                Mockito.any(LocalDateTime.class), Mockito.any(PageRequest.class));
        verify(commentRepository, times(1)).save(Mockito.any(Comment.class));
    }
}