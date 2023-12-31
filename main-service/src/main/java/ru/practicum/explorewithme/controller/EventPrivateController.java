package ru.practicum.explorewithme.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.dto.event.EventFullDto;
import ru.practicum.explorewithme.dto.event.EventShortDto;
import ru.practicum.explorewithme.dto.event.NewEventDto;
import ru.practicum.explorewithme.dto.participation.EventRequestStatusUpdateRequest;
import ru.practicum.explorewithme.dto.participation.EventRequestStatusUpdateResult;
import ru.practicum.explorewithme.dto.participation.ParticipationRequestDto;
import ru.practicum.explorewithme.service.EventService;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/users")
@RequiredArgsConstructor
public class EventPrivateController {
    private final EventService eventService;

    @GetMapping("/{userId}/events")
    public List<EventShortDto> getEventsByUser(@PathVariable Long userId, @RequestParam(defaultValue = "0") int from,
                                               @RequestParam(defaultValue = "10") int size) {
        log.info("Получить событий пользователя с id " + userId);
        return eventService.getEventsByUserId(userId, from, size);
    }

    @PostMapping("/{userId}/events")
    public ResponseEntity<EventFullDto> addEvent(@PathVariable Long userId, @RequestBody NewEventDto eventDto) {
        log.info("Добавить событие " + eventDto);
        EventFullDto eventFullDto = eventService.addEvent(eventDto, userId);
        return new ResponseEntity<>(eventFullDto, HttpStatus.CREATED);
    }

    @GetMapping("/{userId}/events/{eventId}")
    public EventFullDto getFullInfoAboutEventByUser(@PathVariable Long userId, @PathVariable Long eventId) {
        log.info("Получить полную информацию о событии пользователя с id " + userId);
        return eventService.getEventByUserIdAndEventId(userId, eventId);
    }

    @PatchMapping("/{userId}/events/{eventId}")
    public EventFullDto updateEventByUser(@PathVariable Long userId, @PathVariable Long eventId,
                                          @RequestBody NewEventDto event) {
        log.info("Обновление события по id " + eventId);
        return eventService.updateEventByUser(userId, eventId, event);
    }

    @GetMapping("/{userId}/events/{eventId}/requests")
    public List<ParticipationRequestDto> getInfoAboutRequestByEvent(@PathVariable Long userId, @PathVariable long eventId) {
        log.info("Получить информацию о запросах на участие в событии с id " + eventId + " пользователя с id " + userId);
        return eventService.getParticipantsInEventByUser(userId, eventId);
    }

    @PatchMapping("/{userId}/events/{eventId}/requests")
    public EventRequestStatusUpdateResult confirmRequestEventByUser(@PathVariable Long userId, @PathVariable Long eventId,
                                                                    @RequestBody EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest) {
        log.info("Изменение статуса события с id " + eventId);
        return eventService.confirmRequestInEventByUser(userId, eventId, eventRequestStatusUpdateRequest);
    }
}
