package ru.practicum.explorewithme.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.dto.participation.ParticipationRequestDto;
import ru.practicum.explorewithme.service.ParticipationService;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/users")
@RequiredArgsConstructor
public class ParticipationPrivateController {
    private final ParticipationService participationService;

    @GetMapping("/{userId}/requests")
    public List<ParticipationRequestDto> getRequestsByUser(@PathVariable Long userId) {
        log.info("Получение информации о заявках пользователя c id " + userId + " на участие в чужих событиях");
        return participationService.getAllRequestsByUser(userId);
    }

    @PostMapping("/{userId}/requests")
    public ResponseEntity<ParticipationRequestDto> addRequest(@PathVariable Long userId, @RequestParam Long eventId) {
        log.info("Добавление запроса от пользователя c id " + userId + " на событие с id " + eventId);
        ParticipationRequestDto participationRequestDto = participationService.addRequestByUserForEvent(userId, eventId);
        if (participationRequestDto.getParticipantLimit() == 0) {
            return new ResponseEntity<>(participationRequestDto, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(participationRequestDto, HttpStatus.OK);
        }
    }

    @PatchMapping("/{userId}/requests/{requestId}/cancel")
    public ParticipationRequestDto cancelRequest(@PathVariable Long userId, @PathVariable Long requestId) {
        log.info("Отмена запроса с id " + requestId);
        return participationService.canselRequestByUserForEvent(userId, requestId);
    }
}
