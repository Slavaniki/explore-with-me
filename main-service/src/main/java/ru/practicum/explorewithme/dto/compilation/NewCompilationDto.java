package ru.practicum.explorewithme.dto.compilation;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class NewCompilationDto implements Serializable {
    private List<Long> events;
    private Boolean pinned;
    private String title;
}
