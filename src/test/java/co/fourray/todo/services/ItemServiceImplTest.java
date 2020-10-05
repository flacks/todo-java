package co.fourray.todo.services;

import co.fourray.todo.models.Item;
import co.fourray.todo.repositories.ItemRepository;
import co.fourray.todo.utilities.MockObjects;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
public class ItemServiceImplTest {
    @InjectMocks
    private ItemServiceImpl itemService;

    @Mock
    private ItemRepository itemRepository;

    @Test
    public void testGetItems() {
        List<Item> items = new ArrayList<>();
        items.add(MockObjects.getItem());
        items.add(MockObjects.getItem());

        when (itemRepository.findAll()).thenReturn(items);

        assertEquals(2, itemService.getItemsDTO().size());
    }

    @Test
    public void testGetTripById() {
        Item item = MockObjects.getItem();

        when (itemRepository.findById(item.getId())).thenReturn(Optional.of(item));

        Optional<Item> itemOptional = itemService.getItemById(item.getId());
        if (itemOptional.isPresent()) assertEquals(item, itemOptional.get());
        else fail();
    }

    @Test
    public void testAddItem() {
        Item item = MockObjects.getItem();

        when (itemRepository.save(item)).thenReturn(item);

        Item itemAdded = itemService.addItem(item);
        assertEquals(item, itemAdded);
    }

    @Test
    public void testUpdateItem() {
        Item item = MockObjects.getItem();
        Item itemUpdate = MockObjects.getItem();
        itemUpdate.setId(item.getId());
        itemUpdate.setContent(item.getContent() + ", updated");
        System.out.println(item.toString());
        System.out.println(itemUpdate.toString());

        when (itemRepository.findById(item.getId())).thenReturn(Optional.of(item));
        when (itemRepository.save(itemUpdate)).thenReturn(itemUpdate);

        Optional<Item> itemOptional = itemService.getItemById(item.getId());
        if (itemOptional.isPresent()) {
            Item itemUpdated = itemService.updateItem(itemUpdate, itemOptional.get());
            System.out.println(itemUpdated.toString());
            System.out.println(itemUpdate.toString());
            assertEquals(itemUpdated, itemUpdate);
        } else fail();
    }

    @Test
    public void testDeleteItemById() {
        itemService.deleteItemById(1);

        verify (itemRepository, times(1)).deleteById(1);
    }
}
