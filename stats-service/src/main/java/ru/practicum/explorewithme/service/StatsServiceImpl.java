package ru.practicum.explorewithme.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explorewithme.model.EndpointHit;
import ru.practicum.explorewithme.model.EndpointHitDto;
import ru.practicum.explorewithme.model.ViewStatsDto;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

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
        List<EndpointHit> hits;
        List<String> urisWithoutBrackets = new ArrayList<>();
        if (uris != null) {
            for (String uri : uris) {
                uri = uri.replace("[", "").replace("]", "");
                urisWithoutBrackets.add(URLDecoder.decode(uri, StandardCharsets.UTF_8));
            }
            if (unique != null && unique) {
                hits = statsRepository.findDistinctEndpointHitsByStartBeforeAndEndAfter(start, end, urisWithoutBrackets);
            } else {
                hits = statsRepository.findEndpointHitsByStartBeforeAndEndAfter(start, end, urisWithoutBrackets);
            }
        } else {
            if (unique != null && unique) {
                hits = statsRepository.findDistinctEndpointHitsByStartBeforeAndEndAfter(start, end);
            } else {
                hits = statsRepository.findEndpointHitsByStartBeforeAndEndAfter(start, end);
            }
        }
        if (hits.isEmpty()) {
            ViewStatsDto nullViews = new ViewStatsDto("недоступно", "недоступно", 0L);
            return List.of(nullViews);
        } else {
            return EndpointHitMapper.toViewsStatsDto(hits);
        }
    }
}
