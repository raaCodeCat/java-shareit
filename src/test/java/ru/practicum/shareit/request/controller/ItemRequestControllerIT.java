package ru.practicum.shareit.request.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.exeption.NotFoundException;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestUserView;
import ru.practicum.shareit.request.dto.ItemRequestView;
import ru.practicum.shareit.request.service.ItemRequestService;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ItemRequestController.class)
class ItemRequestControllerIT {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ItemRequestService itemRequestService;

    private static final String USER = "X-Sharer-User-Id";

    @SneakyThrows
    @Test
    void addItemRequest_whenValid_thenResponseStatusOkAndItemRequestViewInBody() {
        Long userId = 1L;
        ItemRequestDto itemRequestDto = new ItemRequestDto();
        itemRequestDto.setDescription("Description");
        ItemRequestView itemRequestView = new ItemRequestView();
        when(itemRequestService.create(Mockito.anyLong(), Mockito.any(ItemRequestDto.class)))
                .thenReturn(itemRequestView);

        String result = mockMvc.perform(post("/requests").header(USER, userId)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(itemRequestDto)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertNotNull(result);
        assertEquals(objectMapper.writeValueAsString(itemRequestView), result);

        verify(itemRequestService, times(1)).create(Mockito.anyLong(), Mockito.any(ItemRequestDto.class));
    }

    @SneakyThrows
    @Test
    void addItemRequest_whenXSharerUserIdHeaderIsMissing_thenResponseStatusInternalServerError() {
        ItemRequestDto itemRequestDto = new ItemRequestDto();
        itemRequestDto.setDescription("Description");

        mockMvc.perform(post("/requests")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(itemRequestDto)))
                .andExpect(status().isInternalServerError());

        verify(itemRequestService, never()).create(Mockito.anyLong(), Mockito.any(ItemRequestDto.class));
    }

    @SneakyThrows
    @Test
    void addItemRequest_whenItemRequestDtoIsNotValid_thenResponseStatusBadRequest() {
        Long userId = 1L;
        ItemRequestDto itemRequestDto = new ItemRequestDto();
        itemRequestDto.setDescription("");

        mockMvc.perform(post("/requests").header(USER, userId)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(itemRequestDto)))
                .andExpect(status().isBadRequest());


        verify(itemRequestService, never()).create(Mockito.anyLong(), Mockito.any(ItemRequestDto.class));
    }

    @SneakyThrows
    @Test
    void getUserItemRequest() {
        List<ItemRequestUserView> itemRequests = List.of(new ItemRequestUserView());
        Long userId = 1L;
        when(itemRequestService.getItemRequestsByUserId(Mockito.anyLong())).thenReturn(itemRequests);

        String result = mockMvc.perform(get("/requests").header(USER, userId))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertNotNull(result);
        assertEquals(objectMapper.writeValueAsString(itemRequests), result);

        verify(itemRequestService, times(1)).getItemRequestsByUserId(Mockito.anyLong());
    }

    @SneakyThrows
    @Test
    void getItemRequestById() {
        ItemRequestUserView itemRequest = new ItemRequestUserView();
        Long userId = 1L;
        Long requestId = 10L;
        when(itemRequestService.getItemRequestById(userId, requestId)).thenReturn(itemRequest);

        String result = mockMvc.perform(get("/requests/{requestId}", requestId).header(USER, userId))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertNotNull(result);
        assertEquals(objectMapper.writeValueAsString(itemRequest), result);

        verify(itemRequestService, times(1)).getItemRequestById(userId, requestId);
    }

    @SneakyThrows
    @Test
    void getItemRequestById_whenRequestIdIsNotValid_thenResponseStatusNotFound() {
        Long userId = 1L;
        Long requestId = 10L;
        when(itemRequestService.getItemRequestById(userId, requestId)).thenThrow(new NotFoundException(""));

        mockMvc.perform(get("/requests/{requestId}", requestId).header(USER, userId))
                .andExpect(status().isNotFound());

        verify(itemRequestService, times(1)).getItemRequestById(userId, requestId);
    }

    @SneakyThrows
    @Test
    void getItemRequestsPageable() {
        List<ItemRequestUserView> itemRequests = List.of(new ItemRequestUserView());
        Long userId = 1L;
        Integer from = 0;
        Integer size = 1;
        when(itemRequestService.getItemRequestsPageable(userId, from, size)).thenReturn(itemRequests);

        String result = mockMvc.perform(get("/requests/all").header(USER, userId)
                        .param("from", from.toString())
                        .param("size", size.toString()))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertNotNull(result);
        assertEquals(objectMapper.writeValueAsString(itemRequests), result);

        verify(itemRequestService, times(1)).getItemRequestsPageable(userId, from, size);
    }
}