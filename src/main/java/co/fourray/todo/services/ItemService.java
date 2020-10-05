package co.fourray.todo.services;

import co.fourray.todo.models.Item;
import co.fourray.todo.models.ItemDTO;

import java.util.List;
import java.util.Optional;

public interface ItemService {
    List<Item> getItems();
    List<ItemDTO> getItemsDTO();
    Optional<Item> getItemById(int id);
    Item addItem(Item item);
    Item updateItem(Item item, Item existingItem);
    void deleteItemById(int id);
}
