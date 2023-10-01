package ru.practicum.explorewithme.service;

import ru.practicum.explorewithme.dto.participation.ParticipationRequestDto;

import java.util.List;

public interface ParticipationService {
    List<ParticipationRequestDto> getAllRequestsByUser(Long userId);

    ParticipationRequestDto addRequestByUserForEvent(Long userId, Long eventId);

    ParticipationRequestDto canselRequestByUserForEvent(Long userId, Long eventId);
}