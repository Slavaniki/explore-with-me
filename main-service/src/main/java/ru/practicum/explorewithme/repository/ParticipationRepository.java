package ru.practicum.explorewithme.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.explorewithme.model.Participation;

import java.util.List;

public interface ParticipationRepository extends JpaRepository<Participation, Long> {

    @Query("select count(p) from Participation p where p.event.id = ?1 and p.status like concat('%', ?2, '%')")
    Integer countByEvent_IdAndStatusContaining(Long eventId, String status);

    @Query("select count(distinct p) from Participation p where p.event.id in ?1 and p.status like concat('%', ?2, '%')")
    List<Integer> countDistinctByEvent_IdIsInAndStatusContaining(List<Long> eventsId, String status);


    List<Participation> findAllByEvent_IdAndStatusContaining(Long eventId, String status);

    List<Participation> findAllByRequester_Id(Long userId);

    List<Participation> findAllByEvent_IdAndEvent_Initiator_Id(Long eventId, Long userId);

    Participation findByRequester_IdAndEvent_Id(Long userId, Long eventId);
}
