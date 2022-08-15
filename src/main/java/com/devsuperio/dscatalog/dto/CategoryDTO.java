package com.devsuperio.dscatalog.dto;

import com.devsuperio.dscatalog.entities.Category;
import lombok.*;

import java.io.Serializable;
import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class CategoryDTO implements Serializable {
    private Long id;
    private String name;
    private Instant createdAt;
    private Instant updatedAt;

    public CategoryDTO(Category category) {
        this.id = category.getId();
        this.name = category.getName();
        this.createdAt = category.getCreatedAt();
        this.updatedAt = category.getUpdatedAt();
    }
}
