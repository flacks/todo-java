package co.fourray.todo.utilities;

import co.fourray.todo.models.Item;

import java.time.LocalDateTime;

public class MockObjects {
    public static int itemId = 0;

    public static Item getItem() {
        Item item = new Item();
        item.setId(++itemId);
        item.setContent("Example content");
        item.setTimeCreated(LocalDateTime.now());
        return item;
    }
}
