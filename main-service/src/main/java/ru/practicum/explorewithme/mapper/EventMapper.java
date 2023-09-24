package ru.practicum.explorewithme.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.explorewithme.dto.event.EventFullDto;
import ru.practicum.explorewithme.dto.event.EventShortDto;
import ru.practicum.explorewithme.dto.event.Location;
import ru.practicum.explorewithme.dto.event.NewEventDto;
import ru.practicum.explorewithme.model.Category;
import ru.practicum.explorewithme.model.Event;
import ru.practicum.explorewithme.model.User;

import java.time.format.DateTimeFormatter;

@UtilityClass
public class EventMapper {

    public static Event newEventDtoToEvent(NewEventDto newEventDto, Category category, User initiator) {
        Event event = new Event();
        event.setAnnotation(newEventDto.getAnnotation());
        event.setCategory(category);
        event.setDescription(newEventDto.getDescription());
        event.setEventDate(newEventDto.getEventDate());
        event.setLatitude(newEventDto.getLocation().getLat());
        event.setLongitude(newEventDto.getLocation().getLon());
        event.setPaid(newEventDto.getPaid());
        event.setParticipantLimit(newEventDto.getParticipantLimit());
        event.setRequestModeration(newEventDto.getRequestModeration());
        event.setTitle(newEventDto.getTitle());
        event.setInitiator(initiator);
        return event;
    }

    public static EventFullDto eventToEventFullDto(Event event, Integer confirmedRequest) {
        Boolean isPaid = false;
        Integer participantLimit = 0;
        Boolean requestModeration = true;
        if (event.getPaid() != null) {
            isPaid = event.getPaid();
        }
        if (event.getParticipantLimit() != null) {
            participantLimit = event.getParticipantLimit();
        }
        if (event.getRequestModeration() != null) {
            requestModeration = event.getRequestModeration();
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        Location location = new Location();
        location.setLat(event.getLatitude());
        location.setLon(event.getLongitude());
        EventFullDto eventFullDto = new EventFullDto();
        eventFullDto.setId(event.getId());
        eventFullDto.setAnnotation(event.getAnnotation());
        eventFullDto.setConfirmedRequests(confirmedRequest);
        eventFullDto.setCategory(event.getCategory());
        eventFullDto.setCreatedOn(event.getCreationOn());
        eventFullDto.setDescription(event.getDescription());
        eventFullDto.setEventDate(event.getEventDate().format(formatter));
        eventFullDto.setInitiator(event.getInitiator());
        eventFullDto.setPaid(isPaid);
        eventFullDto.setParticipantLimit(participantLimit);
        eventFullDto.setPublishedOn(event.getPublishedOn());
        eventFullDto.setRequestModeration(requestModeration);
        eventFullDto.setState(event.getState().name());
        eventFullDto.setTitle(event.getTitle());
        eventFullDto.setViews(event.getViews());
        eventFullDto.setLocation(location);
        return eventFullDto;
    }

    public static EventShortDto eventToEventShortDto(Event event, Integer confirmedRequests) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        EventShortDto eventDto = new EventShortDto();
        eventDto.setId(event.getId());
        eventDto.setEventDate(event.getEventDate().format(formatter));
        eventDto.setAnnotation(event.getAnnotation());
        eventDto.setCategory(event.getCategory());
        eventDto.setConfirmedRequests(confirmedRequests);
        eventDto.setInitiator(event.getInitiator());
        eventDto.setPaid(event.getPaid());
        eventDto.setViews(event.getViews());
        eventDto.setTitle(event.getTitle());
        return eventDto;
    }
}
