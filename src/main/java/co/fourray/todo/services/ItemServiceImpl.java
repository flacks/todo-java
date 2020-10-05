package co.fourray.todo.services;

import co.fourray.todo.models.Item;
import co.fourray.todo.models.ItemDTO;
import co.fourray.todo.repositories.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;

    @Override
    public List<Item> getItems() {
        return itemRepository.findAll();
    }

    @Override
    public List<ItemDTO> getItemsDTO() {
        List<Item> items = this.getItems();
        List<ItemDTO> itemsDTO = new ArrayList<>();
        for (Item item : items) {
            itemsDTO.add(new ItemDTO(item));
        }
        return itemsDTO;
    }

    @Override
    public Optional<Item> getItemById(int id) {
        return itemRepository.findById(id);
    }

    @Override
    public Item addItem(Item item) {
        item.setTimeCreated(LocalDateTime.now());
        return itemRepository.save(item);
    }

    @Override
    public Item updateItem(Item item, Item existingItem) {
        item.setTimeCreated(existingItem.getTimeCreated());
        return itemRepository.save(item);
    }

    @Override
    public void deleteItemById(int id) {
        itemRepository.deleteById(id);
    }
}
