package ru.practicum.explorewithme.service;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.explorewithme.model.EndpointHit;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsRepository extends JpaRepository<EndpointHit, Long> {
    List<EndpointHit> findByUriInAndTimestampAfterAndTimestampBefore(List<String> uris, LocalDateTime start, LocalDateTime end);

    List<EndpointHit> findByTimestampAfterAndTimestampBefore(LocalDateTime start, LocalDateTime end);
}
