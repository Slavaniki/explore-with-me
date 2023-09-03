package ru.practicum.explorewithme.service;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.explorewithme.model.EndpointHit;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsRepository extends JpaRepository<EndpointHit, Long> {
    @Query("select e from EndpointHit e where e.timestamp > :start and e.timestamp < :end and e.uri in :uris")
    List<EndpointHit> findByUriInAndTimestampAfterAndTimestampBefore(List<String> uris, LocalDateTime start, LocalDateTime end);
    @Query("select e from EndpointHit e where e.timestamp > :start and e.timestamp < :end")
    List<EndpointHit> findByTimestampAfterAndTimestampBefore(LocalDateTime start, LocalDateTime end);
}
