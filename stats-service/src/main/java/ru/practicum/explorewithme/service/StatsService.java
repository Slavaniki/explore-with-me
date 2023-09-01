package ru.practicum.explorewithme.service;

import ru.practicum.explorewithme.model.EndpointHitDto;
import ru.practicum.explorewithme.model.ViewStatsDto;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsService {
    void createHit(EndpointHitDto endpointHitDto);

    List<ViewStatsDto> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique);
}
