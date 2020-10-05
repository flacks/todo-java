package co.fourray.todo.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Component
@Entity
@NoArgsConstructor
@Data
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String content;

    private LocalDateTime timeCreated;

    public Item(ItemDTO itemDTO) {
        this.id = itemDTO.getId();
        this.content = itemDTO.getContent();
        this.timeCreated = itemDTO.getTimeCreated();
    }
}
