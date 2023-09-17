package ru.practicum.explorewithme.client;

import lombok.Data;

@Data
public class ViewsStats {
    private String app;
    private String uri;
    private Long hits;
}
