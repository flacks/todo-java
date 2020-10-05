package co.fourray.todo.controllers;

import co.fourray.todo.models.Item;
import co.fourray.todo.models.ItemDTO;
import co.fourray.todo.services.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/items")
@CrossOrigin
@Validated
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ItemController {
    private final ItemService itemService;

    /**
     * HTTP GET method (/items)
     *
     * @return a list of all Items along with a 200 code.
     */
    @GetMapping
    public ResponseEntity<List<ItemDTO>> getItems() {
        List<ItemDTO> items = itemService.getItemsDTO();
        if (!items.isEmpty()) return ResponseEntity.ok(items);
        else return ResponseEntity.noContent().build();
    }

    /**
     * HTTP GET method (/items/{id})
     *
     * @param id is an Item's ID.
     * @return an Item that matches by ID along with a 200 code.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ItemDTO> getItemById(@Positive @PathVariable("id") int id) {
        Optional<Item> item = itemService.getItemById(id);
        return item.map(value -> ResponseEntity.ok(new ItemDTO(value)))
                .orElseGet(() -> ResponseEntity.noContent().build());
    }

    /**
     * HTTP POST method (/items)
     *
     * @param itemDTO is the new Item.
     * @return the new Item object along with a 201 code.
     */
    @PostMapping
    public ResponseEntity<ItemDTO> addItem(@Valid @RequestBody ItemDTO itemDTO) {
        Optional<Item> existingItem = itemService.getItemById(itemDTO.getId());
        if (existingItem.isPresent()) return ResponseEntity.badRequest().build();
        else return ResponseEntity.status(201).body(new ItemDTO(itemService.addItem(new Item(itemDTO))));
    }

    /**
     * HTTP PUT method (/items)
     *
     * @param itemDTO is the updated Item being sent.
     * @return the updated Item object along with a 201 code.
     */
    @PutMapping
    public ResponseEntity<ItemDTO> updateItem(@Valid @RequestBody ItemDTO itemDTO) {
        Optional<Item> existingItem = itemService.getItemById(itemDTO.getId());
        if (existingItem.isEmpty()) return ResponseEntity.badRequest().build();
        else return ResponseEntity.status(201).body(
                new ItemDTO(itemService.updateItem(new Item(itemDTO), existingItem.get()))
        );
    }

    /**
     * HTTP DELETE method (/items/{id})
     *
     * @param id is the Item's ID.
     * @return a 404 or 202 code.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteItemById(@Positive @PathVariable("id") int id) {
        Optional<Item> item = itemService.getItemById(id);
        if (item.isEmpty()) return ResponseEntity.notFound().build();
        else {
            itemService.deleteItemById(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
    }
}
