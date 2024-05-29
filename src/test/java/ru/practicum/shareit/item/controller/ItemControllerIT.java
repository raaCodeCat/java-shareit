package ru.practicum.shareit.item.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.exeption.NotFoundException;
import ru.practicum.shareit.item.dto.CommentRequest;
import ru.practicum.shareit.item.dto.CommentView;
import ru.practicum.shareit.item.dto.ItemCreateUpdateDto;
import ru.practicum.shareit.item.dto.ItemView;
import ru.practicum.shareit.item.service.ItemService;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ItemController.class)
class ItemControllerIT {
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ItemService itemService;

    private static final String USER = "X-Sharer-User-Id";

    @SneakyThrows
    @Test
    void getUserItems_whenValid_thenResponseStatusOkAndCollectionItemViewInBody() {
        Long userId = 1L;
        List<ItemView> items = List.of(new ItemView());
        when(itemService.getUserItems(userId)).thenReturn(items);

        String result = mockMvc.perform(get("/items").header(USER, userId))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertNotNull(result);
        assertEquals(objectMapper.writeValueAsString(items), result);

        verify(itemService, times(1)).getUserItems(userId);
    }

    @SneakyThrows
    @Test
    void getUserItems_whenUserIdIsNotValid_thenResponseStatusNotFound() {
        Long userId = 1L;
        when(itemService.getUserItems(userId)).thenThrow(new NotFoundException(""));

        mockMvc.perform(get("/items").header(USER, userId))
                .andExpect(status().isNotFound());

        verify(itemService, times(1)).getUserItems(userId);
    }

    @SneakyThrows
    @Test
    void getUserItems_whenXSharerUserIdHeaderIsMissing_thenResponseStatusInternalServerError() {
        mockMvc.perform(get("/items"))
                .andExpect(status().isInternalServerError());

        verify(itemService, never()).getUserItems(Mockito.anyLong());
    }

    @SneakyThrows
    @Test
    void getItemById_whenValid_thenResponseStatusOkAndItemViewInBody() {
        Long userId = 1L;
        Long itemId = 1L;
        ItemView item = new ItemView();
        when(itemService.getItemById(userId, itemId)).thenReturn(item);

        String result = mockMvc.perform(get("/items/{itemId}", itemId).header(USER, userId))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertNotNull(result);
        assertEquals(objectMapper.writeValueAsString(item), result);

        verify(itemService, times(1)).getItemById(userId, itemId);
    }

    @SneakyThrows
    @Test
    void getItemById_whenItemIdIsNotValid_thenResponseStatusNotFound() {
        Long userId = 1L;
        Long itemId = 1L;
        when(itemService.getItemById(userId, itemId)).thenThrow(new NotFoundException(""));

        mockMvc.perform(get("/items/{itemId}", itemId).header(USER, userId))
                .andExpect(status().isNotFound());

        verify(itemService, times(1)).getItemById(userId, itemId);
    }

    @SneakyThrows
    @Test
    void getItemById_whenXSharerUserIdHeaderIsMissing_thenResponseStatusInternalServerError() {
        Long itemId = 1L;
        mockMvc.perform(get("/items/{itemId}", itemId))
                .andExpect(status().isInternalServerError());

        verify(itemService, never()).getUserItems(Mockito.anyLong());
    }

    @SneakyThrows
    @Test
    void searchItem_whenValid_thenResponseStatusOkAndCollectionItemViewInBody() {
        Long userId = 1L;
        List<ItemView> items = List.of(new ItemView());
        String searchText = "searchText";
        when(itemService.searchItems(searchText)).thenReturn(items);

        mockMvc.perform(get("/items/search")
                .param("text", searchText).header(USER, userId))
                .andExpect(status().isOk());

        verify(itemService, times(1)).searchItems(searchText);
    }

    @SneakyThrows
    @Test
    void searchItem_whenTextParameterIsMissing_thenResponseStatusInternalServerError() {
        Long userId = 1L;

        mockMvc.perform(get("/items/search")
                .header(USER, userId))
                .andExpect(status().isInternalServerError());

        verify(itemService, never()).searchItems(Mockito.anyString());
    }

    @SneakyThrows
    @Test
    void searchItem_whenXSharerUserIdHeaderIsMissing_thenResponseStatusInternalServerError() {
        String searchText = "searchText";

        mockMvc.perform(get("/items/search")
                        .param("text", searchText))
                .andExpect(status().isInternalServerError());

        verify(itemService, never()).searchItems(Mockito.anyString());
    }

    @SneakyThrows
    @Test
    void addItem_whenValid_thenResponseStatusOkAndItemViewInBody() {
        Long userId = 1L;
        ItemCreateUpdateDto itemToCreate = new ItemCreateUpdateDto();
        itemToCreate.setName("name");
        itemToCreate.setDescription("description");
        itemToCreate.setAvailable(true);
        ItemView itemView = new ItemView();
        when(itemService.create(Mockito.anyLong(), Mockito.any(ItemCreateUpdateDto.class))).thenReturn(itemView);

        String result = mockMvc.perform(post("/items").header(USER, userId).contentType("application/json")
                        .content(objectMapper.writeValueAsString(itemToCreate)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(itemView), result);

        verify(itemService, times(1)).create(Mockito.anyLong(), Mockito.any(ItemCreateUpdateDto.class));
    }

    @SneakyThrows
    @Test
    void addItem_whenItemDtoRequestIsNotValid_thenResponseStatusBadRequest() {
        Long userId = 1L;
        ItemCreateUpdateDto itemToCreate = new ItemCreateUpdateDto();
        itemToCreate.setName(null);
        itemToCreate.setDescription(null);
        itemToCreate.setAvailable(null);

        mockMvc.perform(post("/items").header(USER, userId).contentType("application/json")
                        .content(objectMapper.writeValueAsString(itemToCreate)))
                .andExpect(status().isBadRequest());

        verify(itemService, never()).create(Mockito.anyLong(), Mockito.any(ItemCreateUpdateDto.class));
    }

    @SneakyThrows
    @Test
    void addItem_whenXSharerUserIdHeaderIsMissing_thenResponseStatusInternalServerError() {
        ItemCreateUpdateDto itemToCreate = new ItemCreateUpdateDto();
        itemToCreate.setName("name");
        itemToCreate.setDescription("description");
        itemToCreate.setAvailable(true);

        mockMvc.perform(post("/items").contentType("application/json")
                        .content(objectMapper.writeValueAsString(itemToCreate)))
                .andExpect(status().isInternalServerError());

        verify(itemService, never()).create(Mockito.anyLong(), Mockito.any(ItemCreateUpdateDto.class));
    }

    @SneakyThrows
    @Test
    void addItem_whenUserIdIsNotValid_thenResponseStatusNotFound() {
        Long userId = 1L;
        ItemCreateUpdateDto itemToCreate = new ItemCreateUpdateDto();
        itemToCreate.setName("name");
        itemToCreate.setDescription("description");
        itemToCreate.setAvailable(true);
        when(itemService.create(userId, itemToCreate)).thenThrow(new NotFoundException(""));

        mockMvc.perform(post("/items").header(USER, userId).contentType("application/json")
                        .content(objectMapper.writeValueAsString(itemToCreate)))
                .andExpect(status().isNotFound());

        verify(itemService, times(1)).create(userId, itemToCreate);
    }

    @SneakyThrows
    @Test
    void addItem_whenRequestIdIsNotValid_thenResponseStatusNotFound() {
        Long userId = 1L;
        ItemCreateUpdateDto itemToCreate = new ItemCreateUpdateDto();
        itemToCreate.setName("name");
        itemToCreate.setDescription("description");
        itemToCreate.setAvailable(true);
        itemToCreate.setRequestId(100L);
        when(itemService.create(userId, itemToCreate)).thenThrow(new NotFoundException(""));

        mockMvc.perform(post("/items").header(USER, userId).contentType("application/json")
                        .content(objectMapper.writeValueAsString(itemToCreate)))
                .andExpect(status().isNotFound());

        verify(itemService, times(1)).create(userId, itemToCreate);
    }

    @SneakyThrows
    @Test
    void addComment_whenValid_thenResponseStatusOkAndCommentViewInBody() {
        Long userId = 1L;
        Long itemId = 1L;
        CommentRequest commentToCreate = new CommentRequest();
        commentToCreate.setText("comment");
        CommentView commentView = new CommentView();
        when(itemService.createComment(userId, itemId, commentToCreate)).thenReturn(commentView);

        String result = mockMvc.perform(post("/items/{itemId}/comment", itemId).header(USER, userId).contentType("application/json")
                        .content(objectMapper.writeValueAsString(commentToCreate)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(commentView), result);

        verify(itemService, times(1)).createComment(userId, itemId, commentToCreate);
    }

    @SneakyThrows
    @Test
    void addComment_whenCommentRequestIsNotValid_thenResponseStatusBadRequest() {
        Long userId = 1L;
        Long itemId = 1L;
        CommentRequest commentToCreate = new CommentRequest();
        commentToCreate.setText("");
        CommentView commentView = new CommentView();
        when(itemService.createComment(userId, itemId, commentToCreate)).thenReturn(commentView);

        mockMvc.perform(post("/items/{itemId}/comment", itemId).header(USER, userId).contentType("application/json")
                        .content(objectMapper.writeValueAsString(commentToCreate)))
                .andExpect(status().isBadRequest());

        verify(itemService, times(0)).createComment(userId, itemId, commentToCreate);
    }

    @SneakyThrows
    @Test
    void addComment_whenItemIdIsNotValid_thenResponseStatusNotFound() {
        Long userId = 1L;
        Long itemId = 1L;
        CommentRequest commentToCreate = new CommentRequest();
        commentToCreate.setText("comment");
        when(itemService.createComment(userId, itemId, commentToCreate)).thenThrow(new NotFoundException(""));

        mockMvc.perform(post("/items/{itemId}/comment", itemId).header(USER, userId).contentType("application/json")
                        .content(objectMapper.writeValueAsString(commentToCreate)))
                .andExpect(status().isNotFound());

        verify(itemService, times(1)).createComment(userId, itemId, commentToCreate);
    }

    @SneakyThrows
    @Test
    void addComment_whenUserIdIsNotValid_thenResponseStatusNotFound() {
        Long userId = 1L;
        Long itemId = 1L;
        CommentRequest commentToCreate = new CommentRequest();
        commentToCreate.setText("comment");
        when(itemService.createComment(userId, itemId, commentToCreate)).thenThrow(new NotFoundException(""));

        mockMvc.perform(post("/items/{itemId}/comment", itemId).header(USER, userId).contentType("application/json")
                        .content(objectMapper.writeValueAsString(commentToCreate)))
                .andExpect(status().isNotFound());

        verify(itemService, times(1)).createComment(userId, itemId, commentToCreate);
    }

    @SneakyThrows
    @Test
    void addComment_whenXSharerUserIdHeaderIsMissing_thenResponseStatusInternalServerError() {
        Long userId = 1L;
        Long itemId = 1L;
        CommentRequest commentToCreate = new CommentRequest();
        commentToCreate.setText("comment");
        when(itemService.createComment(userId, itemId, commentToCreate)).thenThrow(new NotFoundException(""));

        mockMvc.perform(post("/items/{itemId}/comment", itemId).contentType("application/json")
                        .content(objectMapper.writeValueAsString(commentToCreate)))
                .andExpect(status().isInternalServerError());

        verify(itemService, times(0)).createComment(userId, itemId, commentToCreate);
    }

    @SneakyThrows
    @Test
    void updateItem_whenValid_thenResponseStatusOkAndItemViewInBody() {
        Long userId = 1L;
        Long itemId = 1L;
        ItemCreateUpdateDto itemToUpdate = new ItemCreateUpdateDto();
        itemToUpdate.setName("name");
        itemToUpdate.setDescription("description");
        itemToUpdate.setAvailable(true);
        ItemView itemView = new ItemView();
        when(itemService.update(userId, itemId, itemToUpdate)).thenReturn(itemView);

        String result = mockMvc.perform(patch("/items/{itemId}", itemId).header(USER, userId)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(itemToUpdate)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(itemView), result);

        verify(itemService, times(1)).update(userId, itemId, itemToUpdate);
    }

    @SneakyThrows
    @Test
    void updateItem_whenItemIdIsNotValid_thenResponseStatusNotFound() {
        Long userId = 1L;
        Long itemId = 1L;
        ItemCreateUpdateDto itemToUpdate = new ItemCreateUpdateDto();
        itemToUpdate.setName("name");
        itemToUpdate.setDescription("description");
        itemToUpdate.setAvailable(true);
        when(itemService.update(userId, itemId, itemToUpdate)).thenThrow(new NotFoundException(""));

        mockMvc.perform(patch("/items/{itemId}", itemId).header(USER, userId)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(itemToUpdate)))
                .andExpect(status().isNotFound());

        verify(itemService, times(1)).update(userId, itemId, itemToUpdate);
    }

    @SneakyThrows
    @Test
    void updateItem_whenUserIdIsNotValid_thenResponseStatusNotFound() {
        Long userId = 1L;
        Long itemId = 1L;
        ItemCreateUpdateDto itemToUpdate = new ItemCreateUpdateDto();
        itemToUpdate.setName("name");
        itemToUpdate.setDescription("description");
        itemToUpdate.setAvailable(true);
        when(itemService.update(userId, itemId, itemToUpdate)).thenThrow(new NotFoundException(""));

        mockMvc.perform(patch("/items/{itemId}", itemId).header(USER, userId)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(itemToUpdate)))
                .andExpect(status().isNotFound());

        verify(itemService, times(1)).update(userId, itemId, itemToUpdate);
    }

    @SneakyThrows
    @Test
    void updateItem_whenXSharerUserIdHeaderIsMissing_thenResponseStatusInternalServerError() {
        Long userId = 1L;
        Long itemId = 1L;
        ItemCreateUpdateDto itemToUpdate = new ItemCreateUpdateDto();
        itemToUpdate.setName("name");
        itemToUpdate.setDescription("description");
        itemToUpdate.setAvailable(true);
        when(itemService.update(userId, itemId, itemToUpdate)).thenThrow(new NotFoundException(""));

        mockMvc.perform(patch("/items/{itemId}", itemId).contentType("application/json")
                        .content(objectMapper.writeValueAsString(itemToUpdate)))
                .andExpect(status().isInternalServerError());

        verify(itemService, never()).update(userId, itemId, itemToUpdate);
    }
}