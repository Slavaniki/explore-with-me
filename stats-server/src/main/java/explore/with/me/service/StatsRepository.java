package explore.with.me.service;

import explore.with.me.model.EndpointHit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsRepository extends JpaRepository<EndpointHit, Integer> {
    List<EndpointHit> findEndpointHitsByUrisAndStartBeforeAndEndAfter(LocalDateTime start, LocalDateTime end, List<String> uris);
}
