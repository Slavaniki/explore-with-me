package ru.practicum.explorewithme.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.dto.participation.ParticipationRequestDto;
import ru.practicum.explorewithme.exeption.EventsException;
import ru.practicum.explorewithme.exeption.NotFoundException;
import ru.practicum.explorewithme.exeption.RequestException;
import ru.practicum.explorewithme.mapper.ParticipationMapper;
import ru.practicum.explorewithme.model.*;
import ru.practicum.explorewithme.repository.EventRepository;
import ru.practicum.explorewithme.repository.ParticipationRepository;
import ru.practicum.explorewithme.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ParticipationServiceImpl implements ParticipationService {
    private final ParticipationRepository participationRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    @Override
    public List<ParticipationRequestDto> getAllRequestsByUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("Пользователь с id " + userId + " не найден");
        }
        return ParticipationMapper.participantsToParticipantsDto(participationRepository.findAllByRequester_Id(userId));
    }

    @Override
    public ParticipationRequestDto addRequestByUserForEvent(Long userId, Long eventId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь с id "
                + userId + "не найден"));
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException("Событие с id "
                + eventId + " не найдено"));
        if (participationRepository.findByRequester_IdAndEvent_Id(userId, eventId) != null) {
            throw new EventsException("Запрос на участие в событии: " + event.getTitle() + " от пользователя с id "
                    + userId + " уже существует");
        }
        if (event.getInitiator().getId().equals(userId)) {
            throw new EventsException("Инициатор не может подать заявку на своё событие");
        }
        if (event.getState().equals(EventState.CANCELED)) {
            throw new EventsException("Нельзя подать заявку на отмененное событие");
        }
        if (event.getParticipantLimit().equals(participationRepository
                .countByEvent_IdAndStatusContaining(eventId, "CONFIRMED")) && event.getParticipantLimit() != 0) {
            throw new EventsException("Лимит заявок на событие исчерпан");
        }
        ParticipationRequestDto participationDto = new ParticipationRequestDto();
        participationDto.setRequester(userId);
        participationDto.setEvent(eventId);
        if (!event.getRequestModeration()) {
            participationDto.setStatus("CONFIRMED");
        } else {
            participationDto.setStatus("PENDING");
        }
        if (event.getParticipantLimit() == 0) {
            participationDto.setStatus("CONFIRMED");
        }
        participationDto.setCreated(LocalDateTime.now());
        return ParticipationMapper.participationToParticipationDto(participationRepository.save(ParticipationMapper
                .participationDtoToParticipation(participationDto, user, event)));
    }

    @Override
    public ParticipationRequestDto canselRequestByUserForEvent(Long userId, Long requestId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("Пользователь с id " + userId + " не найден");
        }
        Participation participation = participationRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException("Запрос с id " + requestId + " не найден"));
        if (participation.getStatus().equals("PENDING")) {
            participation.setStatus("CANCELED");
            return ParticipationMapper.participationToParticipationDto(participationRepository.save(participation));
        } else {
            throw new RequestException("Статус заявки находится на в ожидании. Статус - " + participation.getStatus());
        }
    }
}

