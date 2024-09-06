package net.duchung.quora.data.document;

import jakarta.persistence.Id;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BaseDocument {

    @Id
    private Long id;


    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
