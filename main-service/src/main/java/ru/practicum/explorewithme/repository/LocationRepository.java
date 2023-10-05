package ru.practicum.explorewithme.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.explorewithme.model.Location;

import java.util.Set;

public interface LocationRepository extends JpaRepository<Location, Long> {
    Page<Location> findAllByIdIn(Set<Long> ids, Pageable pageable);
}
