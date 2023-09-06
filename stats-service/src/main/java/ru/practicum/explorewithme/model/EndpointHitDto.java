package ru.practicum.explorewithme.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EndpointHitDto {
    private Integer id;
    private String app;
    private String uri;
    private String ip;
    private String timestamp;
}
