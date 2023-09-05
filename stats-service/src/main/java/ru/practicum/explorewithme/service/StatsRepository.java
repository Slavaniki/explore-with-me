package ru.practicum.explorewithme.service;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.explorewithme.model.EndpointHit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsRepository extends JpaRepository<EndpointHit, Long> {
    @Query("select e from EndpointHit e where e.timestamp > :start and e.timestamp < :end and e.uri in :uris")
    List<EndpointHit> findEndpointHitsByUrisAndStartBeforeAndEndAfter(@Param("start") LocalDateTime start,
                                                                      @Param("end") LocalDateTime end,
                                                                      @Param("uris") List<String> uris);

    @Query("select e from EndpointHit e where e.timestamp > :start and e.timestamp < :end")
    List<EndpointHit> findEndpointHitsByUrisAndStartBeforeAndEndAfter(@Param("start") LocalDateTime start,
                                                                      @Param("end") LocalDateTime end);
}
