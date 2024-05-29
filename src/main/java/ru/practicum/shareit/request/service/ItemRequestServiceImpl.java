package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
import java.util.Collections;
import java.util.List;

/**
 * Сервис для работы с {@link ItemRequest}.
 */
@Service
@RequiredArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {
    private final ItemRequestRepository itemRequestRepository;

    private final UserRepository userRepository;

    @Override
    @Transactional
    public ItemRequestView create(Long userId, ItemRequestDto requestDto) {
        User user = findUserById(userId);
        ItemRequest itemRequest = ItemRequestMapper.makeItemRequest(user, requestDto, LocalDateTime.now());

        return ItemRequestMapper.toItemRequestView(itemRequestRepository.save(itemRequest));
    }

    @Override
    public List<ItemRequestUserView> getItemRequestsByUserId(Long userId) {
        User user = findUserById(userId);
        List<ItemRequest> itemRequests = itemRequestRepository.findItemRequestsByUserId(user.getId());

        return ItemRequestMapper.toItemRequestUserView(itemRequests);
    }

    @Override
    public ItemRequestUserView getItemRequestById(Long userId, Long id) {
        findUserById(userId);
        ItemRequest itemRequest = findItemRequestById(id);

        return ItemRequestMapper.toItemRequestUserView(itemRequest);
    }

    @Override
    public List<ItemRequestUserView> getItemRequestsPageable(Long userId, Integer from, Integer size) {
        User user = findUserById(userId);

        if (from == null || size == null) {
            return Collections.emptyList();
        }

        int page = from / size;
        Pageable pageable = PageRequest.of(page, size);
        List<ItemRequest> itemRequests = itemRequestRepository.findItemRequestsExcludingOwnPageable(user.getId(),
                pageable);

        return ItemRequestMapper.toItemRequestUserView(itemRequests);
    }

    private User findUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException(String.format("Пользователь c идентификатором %d не найден", userId)));
    }

    private ItemRequest findItemRequestById(Long itemRequestId) {
        return itemRequestRepository.findItemRequestsById(itemRequestId).orElseThrow(() ->
                new NotFoundException(String.format("Запрос c идентификатором %d не найден", itemRequestId)));
    }
}
