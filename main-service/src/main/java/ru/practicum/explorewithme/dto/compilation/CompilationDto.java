package ru.practicum.explorewithme.dto.compilation;

import lombok.Data;
import ru.practicum.explorewithme.dto.event.EventShortDto;

import java.util.List;

@Data
public class CompilationDto {
    private Long id;
    private List<EventShortDto> events;
    private Boolean pinned;
    private String title;
}