package explore.with.me.service;

import explore.with.me.model.EndpointHit;
import explore.with.me.model.EndpointHitDto;
import explore.with.me.model.ViewStatsDto;
import javafx.util.Pair;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class EndpointHitMapper {
    public static EndpointHit ToEndpointHit(EndpointHitDto endpointHitDto) {
        return new EndpointHit(
            endpointHitDto.getId(),
            endpointHitDto.getApp(),
            endpointHitDto.getUri(),
            endpointHitDto.getIp(),
            LocalDateTime.parse(endpointHitDto.getTimestamp(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
        );
    }

    public static List<ViewStatsDto> ToViewsStatsDto(List<EndpointHit> endpointHits) {
        return endpointHits.stream().collect(Collectors.groupingBy(
                        endpointHit -> new Pair<>(endpointHit.getApp(), endpointHit.getUri()), Collectors.counting()))
                .entrySet().stream().map(entry -> new ViewStatsDto(
                        entry.getKey().getKey(),
                        entry.getKey().getValue(),
                        entry.getValue().intValue()))
                .collect(Collectors.toList());
    }
}
