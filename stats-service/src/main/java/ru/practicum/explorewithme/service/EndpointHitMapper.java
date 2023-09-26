package ru.practicum.explorewithme.service;

import javafx.util.Pair;
import lombok.extern.slf4j.Slf4j;
import ru.practicum.explorewithme.model.EndpointHit;
import ru.practicum.explorewithme.model.EndpointHitDto;
import ru.practicum.explorewithme.model.ViewStatsDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
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
        List<ViewStatsDto> eph = endpointHits.stream().collect(Collectors.groupingBy(
                        endpointHit -> new Pair(endpointHit.getApp(), endpointHit.getUri()), Collectors.counting()))
                .entrySet().stream().map(entry -> new ViewStatsDto(
                        entry.getKey().getKey().toString(),
                        entry.getKey().getValue().toString(),
                        entry.getValue()
                ))
                .collect(Collectors.toList());
        Collections.sort(eph, Comparator.comparing(ViewStatsDto::getHits));
        return eph;
    }
}
