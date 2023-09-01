package explore.with.me.service;

import explore.with.me.model.EndpointHitDto;
import explore.with.me.model.ViewStatsDto;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsService {
    void createHit(EndpointHitDto endpointHitDto);

    List<ViewStatsDto> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique);
}
