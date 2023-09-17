package ru.practicum.explorewithme.dto.categories;

import lombok.Data;

import java.io.Serializable;

@Data
public class NewCategoryDto implements Serializable {
    private String name;
}
