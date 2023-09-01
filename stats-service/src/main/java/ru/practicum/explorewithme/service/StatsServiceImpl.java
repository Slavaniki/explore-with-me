package ru.practicum.explorewithme.service;

import ru.practicum.explorewithme.model.EndpointHit;
import ru.practicum.explorewithme.model.EndpointHitDto;
import ru.practicum.explorewithme.model.ViewStatsDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class StatsServiceImpl implements StatsService {
    private final StatsRepository statsRepository;

    @Override
    @Transactional
    public void createHit(EndpointHitDto endpointHitDto) {
        endpointHitDto.setTimestamp(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        statsRepository.save(EndpointHitMapper.toEndpointHit(endpointHitDto));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ViewStatsDto> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
        List<String> urisWithoutBrackets = new ArrayList<>();
        for (String uri : uris) {
            uri = uri.replace("[", "").replace("]", "");
            urisWithoutBrackets.add(URLDecoder.decode(uri, StandardCharsets.UTF_8));
        }
        List<EndpointHit> hits = statsRepository.findEndpointHitsByUrisAndStartBeforeAndEndAfter(start, end, urisWithoutBrackets);
        if (unique) {
            hits = hits.stream()
                    .sorted(Comparator.comparing(EndpointHit::getIp))
                    .distinct()
                    .collect(Collectors.toList());
        }
        if (hits.isEmpty()) {
            ViewStatsDto nullViews = new ViewStatsDto("unavailable", "unavailable", 0L);
            return List.of(nullViews);
        } else {
            return EndpointHitMapper.toViewsStatsDto(hits);
        }
    }
}
