package ru.practicum.explorewithme.dto.event;

import lombok.Data;
import ru.practicum.explorewithme.model.Category;
import ru.practicum.explorewithme.model.User;

@Data
public class EventShortDto {
    private Long id;
    private String annotation;
    private Integer confirmedRequests;
    private Category category;
    private String eventDate;
    private User initiator;
    private Boolean paid;
    private String title;
    private Long views;
}
