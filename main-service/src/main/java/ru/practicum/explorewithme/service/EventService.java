package ru.practicum.explorewithme.service;

import ru.practicum.explorewithme.dto.event.EventFullDto;
import ru.practicum.explorewithme.dto.event.EventShortDto;
import ru.practicum.explorewithme.dto.event.NewEventDto;
import ru.practicum.explorewithme.dto.participation.ParticipationRequestDto;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public interface EventService {
    List<EventShortDto> getPublishedEventsWithFilter(String text, Set<Long> categoriesId, Boolean paid,
                                                     LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                                     Boolean onlyAvailable, String sort, int from, int size,
                                                     HttpServletRequest request);

    EventFullDto getPublishedEventById(Long eventId, HttpServletRequest request);

    EventFullDto addEvent(NewEventDto eventDto, Long userId);

    List<EventShortDto> getEventsByUserId(Long userId, int from, int size);

    EventFullDto getEventByUserIdAndEventId(Long userId, Long eventId);

    EventFullDto canceledEventByUser(Long userId, Long eventId);

    List<ParticipationRequestDto> getParticipantsInEventByUser(Long userId, Long eventId);

    ParticipationRequestDto confirmRequestInEventByUser(Long userId, Long eventId, Long reqId);

    List<EventFullDto> searchEventsByAdmin(Set<Long> usersId, Set<String> states, Set<Long> categoriesId,
                                           LocalDateTime rangeStart, LocalDateTime rangeEnd, int from, int size);

    EventFullDto updateEventByAdmin(Long eventId, NewEventDto eventDto);
}