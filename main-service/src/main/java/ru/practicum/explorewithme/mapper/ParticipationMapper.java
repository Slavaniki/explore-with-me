package ru.practicum.explorewithme.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.explorewithme.dto.participation.ParticipationRequestDto;
import ru.practicum.explorewithme.model.Event;
import ru.practicum.explorewithme.model.Participation;
import ru.practicum.explorewithme.model.User;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class ParticipationMapper {

    public static ParticipationRequestDto participationToParticipationDto(Participation participation) {
        ParticipationRequestDto participationRequestDto = new ParticipationRequestDto();
        participationRequestDto.setId(participation.getId());
        participationRequestDto.setCreated(participation.getCreated());
        participationRequestDto.setEvent(participation.getEvent().getId());
        participationRequestDto.setRequester(participation.getRequester().getId());
        participationRequestDto.setStatus(participation.getStatus());
        participationRequestDto.setParticipantLimit(participation.getEvent().getParticipantLimit());
        return participationRequestDto;
    }

    public static Participation participationDtoToParticipation(ParticipationRequestDto participationRequestDto, User requester,
                                                                Event event) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String time = participationRequestDto.getCreated().format(formatter);
        Participation participation = new Participation();
        participation.setId(participationRequestDto.getId());
        participation.setCreated(LocalDateTime.parse(time, formatter));
        participation.setRequester(requester);
        participation.setStatus(participationRequestDto.getStatus());
        participation.setEvent(event);
        return participation;
    }

    public static List<ParticipationRequestDto> participantsToParticipantsDto(List<Participation> participants) {
        return participants.stream().map(ParticipationMapper::participationToParticipationDto)
                .collect(Collectors.toList());
    }
}
