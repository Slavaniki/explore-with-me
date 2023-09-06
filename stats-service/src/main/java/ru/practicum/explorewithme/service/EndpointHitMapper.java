package ru.practicum.explorewithme.service;

import ru.practicum.explorewithme.model.EndpointHit;
import ru.practicum.explorewithme.model.EndpointHitDto;
import ru.practicum.explorewithme.model.ViewStatsDto;
import javafx.util.Pair;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class EndpointHitMapper {
    public static EndpointHit toEndpointHit(EndpointHitDto endpointHitDto) {
        return new EndpointHit(
            endpointHitDto.getId(),
            endpointHitDto.getApp(),
            endpointHitDto.getUri(),
            endpointHitDto.getIp(),
            LocalDateTime.parse(endpointHitDto.getTimestamp(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
        );
    }

    public static List<ViewStatsDto> toViewsStatsDto(List<EndpointHit> endpointHits) {
        return endpointHits.stream().collect(Collectors.groupingBy(
                        endpointHit -> new Pair<>(endpointHit.getApp(), endpointHit.getUri()), Collectors.counting()))
                .entrySet().stream().map(entry -> new ViewStatsDto(
                        entry.getKey().getKey(),
                        entry.getKey().getValue(),
                        entry.getValue().longValue()))
                .collect(Collectors.toList());
    }
}
