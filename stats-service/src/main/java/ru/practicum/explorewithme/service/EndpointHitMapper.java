package ru.practicum.explorewithme.service;

import ru.practicum.explorewithme.model.EndpointHit;
import ru.practicum.explorewithme.model.EndpointHitDto;
import ru.practicum.explorewithme.model.Triple;
import ru.practicum.explorewithme.model.ViewStatsDto;

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
        EndpointHit eh;
        return endpointHits.stream().collect(Collectors.groupingBy(
                        endpointHit -> new Triple(endpointHit.getApp(), endpointHit.getUri(), endpointHit.getIp()),
                        Collectors.counting()))
                .entrySet().stream().map(entry -> new ViewStatsDto(
                        entry.getKey().getFirst().toString(),
                        entry.getKey().getSecond().toString(),
                        entry.getValue().longValue(),
                        entry.getKey().getThird().toString()
                        ))
                .collect(Collectors.toList());
    }
}
