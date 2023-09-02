package ru.practicum.explorewithme.service;

import org.springframework.data.jpa.repository.Query;
import ru.practicum.explorewithme.model.EndpointHit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsRepository extends JpaRepository<EndpointHit, Long> {
    @Query("select e from EndpointHit e where e.timestamp > ?1 and e.timestamp < ?2 and e.uri in ?3")
    List<EndpointHit> findEndpointHitsByUriAndStartBeforeAndEndAfter(LocalDateTime start, LocalDateTime end, List<String> uris);

    @Query("select e from EndpointHit e where e.timestamp > ?1 and e.timestamp < ?2")
    List<EndpointHit> findEndpointHitsByStartBeforeAndEndAfter(LocalDateTime start, LocalDateTime end);

    @Query("select distinct ip as e from EndpointHit e where e.timestamp > ?1 and e.timestamp < ?2 and e.uri in ?3")
    List<EndpointHit> findDistinctEndpointHitsByUriAndStartBeforeAndEndAfter(LocalDateTime start, LocalDateTime end, List<String> uris);

    @Query("select distinct ip as e from EndpointHit e where e.timestamp > ?1 and e.timestamp < ?2")
    List<EndpointHit> findDistinctEndpointHitsByStartBeforeAndEndAfter(LocalDateTime start, LocalDateTime end);
}
