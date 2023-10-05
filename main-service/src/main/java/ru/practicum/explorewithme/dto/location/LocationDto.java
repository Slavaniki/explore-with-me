package ru.practicum.explorewithme.dto.location;

import lombok.Data;
import ru.practicum.explorewithme.model.Event;

import java.util.List;

@Data
public class LocationDto {
    private Long id;
    private String name;
    private Double longitude;
    private Double latitude;
    private Integer radius;
    private List<Event> events;
}