package co.fourray.todo.controllers;

import co.fourray.todo.models.Item;
import co.fourray.todo.models.ItemDTO;
import co.fourray.todo.services.ItemService;
import co.fourray.todo.utilities.MockObjects;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(ItemController.class)
public class ItemControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ItemService itemService;

    @Test
    public void testGetItems() throws Exception {
        List<ItemDTO> items = new ArrayList<>();
        items.add(new ItemDTO(MockObjects.getItem()));
        items.add(new ItemDTO(MockObjects.getItem()));

        when (itemService.getItemsDTO()).thenReturn(items);

        mockMvc.perform(get("/items"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    public void testGetItemsFail() throws Exception {
        List<ItemDTO> items = new ArrayList<>();

        when (itemService.getItemsDTO()).thenReturn(items);

        mockMvc.perform(get("/items"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testGetItemById() throws Exception {
        Item item = MockObjects.getItem();

        when (itemService.getItemById(item.getId())).thenReturn(Optional.of(item));

        mockMvc.perform(get("/items/{id}", item.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(item.getId()));
    }

    @Test
    public void testGetItemByIdFail() throws Exception {
        Item item = MockObjects.getItem();

        when (itemService.getItemById(2)).thenReturn(Optional.of(item));

        mockMvc.perform(get("/items/{id}", item.getId()))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testAddItem() throws Exception {
        Item item = MockObjects.getItem();
        ItemDTO itemDTO = new ItemDTO(item);

        when (itemService.addItem(new Item(itemDTO))).thenReturn(item);

        mockMvc.perform(post("/items")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(item)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.content").value(MockObjects.getItem().getContent()));
    }

    @Test
    public void testAddItemFail() throws Exception {
        mockMvc.perform(post("/items")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString("Bad item")))
                .andExpect(status().isBadRequest());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullItemDTOFail() {
        new ItemDTO(null);
    }

    @Test
    public void testUpdateItem() throws Exception {
        Item item = MockObjects.getItem();
        Item itemUpdated = MockObjects.getItem();
        itemUpdated.setId(item.getId());
        String updatedContent = itemUpdated.getContent() + ", updated";
        itemUpdated.setContent(updatedContent);
        ItemDTO itemUpdatedDTO = new ItemDTO(itemUpdated);

        when (itemService.getItemById(item.getId())).thenReturn(Optional.of(item));
        when (itemService.updateItem(new Item(itemUpdatedDTO), item)).thenReturn(itemUpdated);

        mockMvc.perform(put("/items")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(itemUpdated)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.content").value(updatedContent))
                .andReturn();
    }

    @Test
    public void testUpdateItemFail() throws Exception {
        Item item = MockObjects.getItem();

        mockMvc.perform(put("/items")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(item)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testDeleteItem() throws Exception {
        Item item = MockObjects.getItem();

        when (itemService.getItemById(item.getId())).thenReturn(Optional.of(item));

        mockMvc.perform(delete("/items/{id}", item.getId()))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testDeleteItemFail() throws Exception {
        Item item = MockObjects.getItem();

        mockMvc.perform(delete("/items/{id}", item.getId()))
                .andExpect(status().isNotFound());
    }
}
