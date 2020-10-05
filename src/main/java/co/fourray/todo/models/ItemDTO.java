package co.fourray.todo.models;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@NoArgsConstructor
@Data
public class ItemDTO {
    private int id;

    @Valid
    @NotNull
    private String content;

    private LocalDateTime timeCreated;

    public ItemDTO(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("ItemDTO must not be null");
        }
        this.id = item.getId();
        this.content = item.getContent();
        this.timeCreated = item.getTimeCreated();
    }
}
