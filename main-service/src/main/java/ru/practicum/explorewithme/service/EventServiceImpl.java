package ru.practicum.explorewithme.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explorewithme.client.Client;
import ru.practicum.explorewithme.client.ViewsStats;
import ru.practicum.explorewithme.dto.event.EventFullDto;
import ru.practicum.explorewithme.dto.event.EventShortDto;
import ru.practicum.explorewithme.dto.event.NewEventDto;
import ru.practicum.explorewithme.dto.participation.EventRequestStatusUpdateRequest;
import ru.practicum.explorewithme.dto.participation.EventRequestStatusUpdateResult;
import ru.practicum.explorewithme.dto.participation.ParticipationRequestDto;
import ru.practicum.explorewithme.exeption.EventsException;
import ru.practicum.explorewithme.exeption.NotFoundException;
import ru.practicum.explorewithme.exeption.RequestException;
import ru.practicum.explorewithme.mapper.EventMapper;
import ru.practicum.explorewithme.mapper.ParticipationMapper;
import ru.practicum.explorewithme.model.Category;
import ru.practicum.explorewithme.model.Event;
import ru.practicum.explorewithme.model.EventState;
import ru.practicum.explorewithme.model.Participation;
import ru.practicum.explorewithme.repository.CategoryRepository;
import ru.practicum.explorewithme.repository.EventRepository;
import ru.practicum.explorewithme.repository.ParticipationRepository;
import ru.practicum.explorewithme.repository.UserRepository;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;
    private final ParticipationRepository participationRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final Client statsClient;

    @Override
    public List<EventShortDto> getPublishedEventsWithFilter(String text, Set<Long> categoriesId, Boolean paid,
                                                            LocalDateTime rangeStart, LocalDateTime  rangeEnd,
                                                            Boolean onlyAvailable, String sort, int from, int size,
                                                            HttpServletRequest request) {
        if (rangeStart != null && rangeEnd != null && rangeStart.isAfter(rangeEnd)) {
            throw new RequestException("Дата начала не может быть позже даты конца");
        }
        List<Event> eventsWithSort;
        LocalDateTime start = rangeStart == null ? LocalDateTime.now().minusYears(5) : rangeStart;
        LocalDateTime end = rangeEnd == null ? LocalDateTime.now().plusYears(5) : rangeEnd;
        if (sort != null && sort.equals("EVENT_DATE")) {
            eventsWithSort = eventRepository.getEventsWithSortEventDate(categoriesId,
                    paid, rangeStart, rangeEnd, EventState.PUBLISHED, text, text, PageRequest.of(from, size));
        } else if (sort != null && sort.equals("VIEWS")) {
            eventsWithSort = eventRepository.getEventsWithSortViews(categoriesId,
                    paid, rangeStart, rangeEnd, EventState.PUBLISHED, text, text, PageRequest.of(from, size));
        } else if (categoriesId != null) {
            List<Event> allEvents = eventRepository.findAll();
            log.info("categoriesId: " + categoriesId);
            eventsWithSort = allEvents.stream()
                    .filter(event -> categoriesId.contains(event.getCategory().getId()))
                    .limit(size)
                    .collect(Collectors.toList());
        } else if (text != null) {
            eventsWithSort = eventRepository.findAll(PageRequest.of(from, size)).stream()
                    .filter(event -> event.getAnnotation().equalsIgnoreCase(text)
                            && event.getPaid() == paid
                            && event.getEventDate().isAfter(start)
                            && event.getEventDate().isBefore(end))
                    .collect(Collectors.toList());
        } else if (paid != null) {
            eventsWithSort = eventRepository.findAll(PageRequest.of(from, size)).stream()
                    .filter(event -> event.getPaid() == paid
                            && event.getEventDate().isAfter(start)
                            && event.getEventDate().isBefore(end))
                    .collect(Collectors.toList());
        } else {
            eventsWithSort = eventRepository.findAll(PageRequest.of(from, size)).stream()
                    .filter(event -> event.getEventDate().isAfter(start)
                            && event.getEventDate().isBefore(end))
                    .collect(Collectors.toList());
        }
        List<EventShortDto> eventsShortDto = new ArrayList<>();
        for (Event event : Objects.requireNonNull(eventsWithSort)) {
            eventsShortDto.add(EventMapper.eventToEventShortDto(event, participationRepository
                    .countByEvent_IdAndStatusContaining(event.getId(), "CONFIRMED")));
        }
        return eventsShortDto;
    }

    @Override
    public EventFullDto getPublishedEventById(Long eventId, HttpServletRequest request) {
        if (!eventRepository.existsById(eventId)) {
            throw new NotFoundException("Событие с id " + eventId + " не найдено");
        }
        Event event = eventRepository.findEventByIdAndStateContainsIgnoreCase(eventId, EventState.PUBLISHED.name());
        if (event == null) {
            throw new NotFoundException("Событие с id " + eventId + " не найдено");
        }
        List<ViewsStats> lvs = getViewsByEvent(LocalDateTime.now().minusYears(5), LocalDateTime.now().plusSeconds(20),
                List.of(request.getRequestURI() + "/" + eventId));
        if (lvs.isEmpty()) {
            throw new NotFoundException("Событие с id " + eventId + " не найдено");
        }
        event.setViews(new Long(lvs.size()));
        return EventMapper.eventToEventFullDto(event,
                participationRepository.countByEvent_IdAndStatusContaining(eventId, "CONFIRMED"));
    }

    @Override
    @Transactional
    public EventFullDto addEvent(NewEventDto eventDto, Long userId) {
        if (eventDto.getAnnotation() == null || eventDto.getCategory() == null || eventDto.getDescription() == null
                || eventDto.getDescription().isBlank() || eventDto.getAnnotation().isBlank() ||
                eventDto.getEventDate() == null || eventDto.getLocation() == null || eventDto.getTitle() == null) {
            throw new RequestException("Пустыми могут быть только поля paid, participantLimit, requestModeration");
        }
        if (eventDto.getDescription().length() < 20) {
            throw new RequestException("Количество символов описания меньше 20");
        }
        if (eventDto.getAnnotation().length() > 2000 || eventDto.getAnnotation().length() < 20) {
            throw new RequestException("Количество символов аннотации меньше 20 или больше 2000");
        }
        if (eventDto.getTitle().length() < 3 || eventDto.getTitle().length() > 120) {
            throw new RequestException("Количество символов заголовка меньше 3 или больше 120");
        }
        Category category = categoryRepository.findById(eventDto.getCategory()).orElseThrow(() ->
                new RequestException("Категория с id " + eventDto.getCategory() + " не найдена"));
        if (eventDto.getEventDate().isBefore(LocalDateTime.now().plusHours(2))) {
            throw new RequestException("Время начала события не может быть раньше, чем через 2 часа");
        }
        Event event = EventMapper.newEventDtoToEvent(eventDto, category, userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException("Пользователь с id " + userId + " не найден")));
        return EventMapper.eventToEventFullDto(eventRepository.save(event), 0);
    }

    @Override
    public List<EventShortDto> getEventsByUserId(Long userId, int from, int size) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("Пользователь с id " + userId + " не найден");
        }
        Page<Event> events = eventRepository.findAllByInitiator_Id(userId, PageRequest.of(from, size));
        List<EventShortDto> eventsShortDto = new ArrayList<>();
        for (Event event : events) {
            eventsShortDto.add(EventMapper.eventToEventShortDto(event, participationRepository
                    .countByEvent_IdAndStatusContaining(event.getId(), "CONFIRMED")));
        }
        return eventsShortDto;
    }

    @Override
    public EventFullDto getEventByUserIdAndEventId(Long userId, Long eventId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("Пользователь с id " + userId + " не найден");
        }
        if (!eventRepository.existsById(eventId)) {
            throw new NotFoundException("Событие с id " + eventId + " не найдено");
        }
        return EventMapper.eventToEventFullDto(eventRepository.findByIdAndInitiator_Id(eventId, userId),
                participationRepository.countByEvent_IdAndStatusContaining(eventId, "CONFIRMED"));
    }

    @Override
    @Transactional
    public EventFullDto updateEventByUser(Long userId, Long eventId, NewEventDto newEvent) {
        Event event = eventRepository.findById(eventId).orElseThrow(() ->
                new NotFoundException("Событие с id " + eventId + " не найдено"));
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("Пользователь с id " + userId + " не найден");
        }
        if (!event.getInitiator().getId().equals(userId)) {
            throw new RequestException("Пользователь с id " + userId + " не создатель события");
        }
        if (event.getState() == EventState.PUBLISHED) {
            throw new EventsException("Нельзя изменить опубликованное событие");
        }
        if (newEvent.getEventDate() != null) {
            if (event.getEventDate().isAfter(newEvent.getEventDate())) {
                throw new RequestException("Дата " + newEvent.getEventDate() + " раньше события");
            }
        }
        if (newEvent.getTitle() != null && (newEvent.getTitle().length() < 3 || newEvent.getTitle().length() > 120)) {
            throw new RequestException("getTitle");
        }
        if (newEvent.getDescription() != null &&
                (newEvent.getDescription().length() < 20 || newEvent.getDescription().length() > 7000)) {
            throw new RequestException("getDescription");
        }
        if (newEvent.getAnnotation() != null &&
                (newEvent.getAnnotation().length() < 20 || newEvent.getAnnotation().length() > 2000)) {
            throw new RequestException("getAnnotation");
        }
        if (newEvent.getStateAction() != null && newEvent.getStateAction().equals("SEND_TO_REVIEW")) {
            event.setState(EventState.PENDING);
        }
        if (newEvent.getStateAction() != null && newEvent.getStateAction().equals("CANCEL_REVIEW")) {
            event.setState(EventState.CANCELED);
        }
        if (newEvent.getStateAction() != null && newEvent.getStateAction().equals("PUBLISH_EVENT")) {
            event.setState(EventState.PUBLISHED);
        }
        return EventMapper.eventToEventFullDto(eventRepository.save(event), participationRepository
                .countByEvent_IdAndStatusContaining(eventId, "CONFIRMED"));
    }

    @Override
    public List<ParticipationRequestDto> getParticipantsInEventByUser(Long userId, Long eventId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("Пользователь с id " + userId + " не найден");
        }
        if (!eventRepository.existsById(eventId)) {
            throw new NotFoundException("Событие с id " + eventId + " не найдено");
        }
        return ParticipationMapper.participantsToParticipantsDto(participationRepository
                .findAllByEvent_IdAndEvent_Initiator_Id(eventId, userId));
    }

    @Override
    @Transactional
    public EventRequestStatusUpdateResult confirmRequestInEventByUser(Long userId, Long eventId,
                                                                      EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("Пользователь с id " + userId + " не найден");
        }
        Event event = eventRepository.findById(eventId).orElseThrow(() ->
                new NotFoundException("Событие с id " + eventId + " не найдено"));
        if (!event.getInitiator().getId().equals(userId)) {
            throw new RequestException("Пользователь с id " + userId + " не создатель события");
        }
        EventRequestStatusUpdateResult eventRequestStatusUpdateResult = new EventRequestStatusUpdateResult();
        List<ParticipationRequestDto> participationDtos = new ArrayList<>();
        List<ParticipationRequestDto> rparticipationDtos = new ArrayList<>();
        eventRequestStatusUpdateRequest.getRequestIds().forEach(reqId -> {
            Participation confirmRequest = participationRepository.findById(reqId).orElseThrow(() ->
                    new NotFoundException("Запрос на участие с id " + reqId + " не найден"));
            if (participationRepository.countByEvent_IdAndStatusContaining(eventId, "CONFIRMED")
                    .equals(event.getParticipantLimit())) {
                throw new EventsException("Лимит заявок на событие id " + eventId + " уже исчерпан");
            }
            confirmRequest.setStatus(eventRequestStatusUpdateRequest.getStatus().name());
            ParticipationRequestDto participationDto = ParticipationMapper
                    .participationToParticipationDto(participationRepository.save(confirmRequest));
            if (participationRepository.countByEvent_IdAndStatusContaining(eventId, "CONFIRMED") >=
                    (event.getParticipantLimit())) {
                changeStatusOfParticipantToRejected(eventId);
            }
            if (eventRequestStatusUpdateRequest.getStatus() == EventRequestStatusUpdateRequest.StatusEnum.CONFIRMED) {
                participationDtos.add(participationDto);
            } else {
                rparticipationDtos.add(participationDto);
            }
        });
        eventRequestStatusUpdateResult.setConfirmedRequests(participationDtos);
        eventRequestStatusUpdateResult.setRejectedRequests(rparticipationDtos);
        return eventRequestStatusUpdateResult;
    }

    @Override
    public List<EventFullDto> searchEventsByAdmin(Set<Long> usersId, Set<String> states, Set<Long> categoriesId,
                                                  LocalDateTime rangeStart, LocalDateTime rangeEnd, int from, int size) {
        List<EventState> statesReform = new ArrayList<>();
        if (states != null) {
            for (String state : states) {
                if (state.equals(EventState.PENDING.name())) {
                    statesReform.add(EventState.PENDING);
                } else if (state.equals(EventState.CANCELED.name())) {
                    statesReform.add(EventState.CANCELED);
                } else if (state.equals(EventState.PUBLISHED.name())) {
                    statesReform.add(EventState.PUBLISHED);
                } else {
                    throw new RequestException("Параметр state " + state + " не найден");
                }
            }
        }
        Page<Event> eventsPage;
        if (states != null && !states.isEmpty()) {
            eventsPage = eventRepository.findEventsByInitiator_IdInAndStateInAndCategory_IdInAndEventDateIsBetween(usersId,
                    statesReform, categoriesId, rangeStart, rangeEnd, PageRequest.of(from, size));
        } else {
            eventsPage = eventRepository.findAll(PageRequest.of(from, size));
        }
        List<EventFullDto> events = new ArrayList<>();
        for (Event event : eventsPage) {
            events.add(EventMapper.eventToEventFullDto(event, participationRepository
                    .countByEvent_IdAndStatusContaining(event.getId(), "CONFIRMED")));
        }
        return events;
    }

    @Override
    @Transactional
    public EventFullDto updateEventByAdmin(Long eventId, NewEventDto eventDto) {
        Event adminUpdateEvent = eventRepository.findById(eventId).orElseThrow(() ->
                new NotFoundException("Событие с id " + eventId + " не найдено"));
        if (adminUpdateEvent.getState() == EventState.PUBLISHED &&
                eventDto.getStateAction().equals("PUBLISH_EVENT")) {
            throw new EventsException("Событие уже опубликованно");
        }
        if (adminUpdateEvent.getState() == EventState.CANCELED &&
                eventDto.getStateAction().equals("PUBLISH_EVENT")) {
            throw new EventsException("Событие уже отменено");
        }
        if (adminUpdateEvent.getState() == EventState.PUBLISHED &&
                eventDto.getStateAction().equals("REJECT_EVENT")) {
            throw new EventsException("Событие уже опубликованно");
        }
        if (eventDto.getDescription() != null && (eventDto.getDescription().length() < 20 ||
                eventDto.getDescription().length() > 7000)) {
            throw new RequestException("Количество символов описания меньше 20");
        }
        if (eventDto.getAnnotation() != null && (eventDto.getAnnotation().length() > 2000 ||
                eventDto.getAnnotation().length() < 20)) {
            throw new RequestException("Количество символов аннотации меньше 20 или больше 2000");
        }
        if (eventDto.getTitle() != null && (eventDto.getTitle().length() < 3 || eventDto.getTitle().length() > 120)) {
            throw new RequestException("Количество символов заголовка меньше 3 или больше 120");
        }
        if (eventDto.getAnnotation() != null && !eventDto.getAnnotation().equals(adminUpdateEvent.getAnnotation())) {
            adminUpdateEvent.setAnnotation(eventDto.getAnnotation());
        }
        if (eventDto.getCategory() != null && !eventDto.getCategory().equals(adminUpdateEvent.getCategory().getId())) {
            adminUpdateEvent.setCategory(categoryRepository.findById(eventDto.getCategory()).orElseThrow(() ->
                    new NotFoundException("Категория с id " + eventDto.getCategory() + " не найдена")));
        }
        if (eventDto.getDescription() != null && !eventDto.getDescription().equals(adminUpdateEvent.getDescription())) {
            adminUpdateEvent.setDescription(eventDto.getDescription());
        }
        if (eventDto.getEventDate() != null && !eventDto.getEventDate().equals(adminUpdateEvent.getEventDate())
                && eventDto.getEventDate().isAfter(LocalDateTime.now().plusHours(1))) {
            adminUpdateEvent.setEventDate(eventDto.getEventDate());
        }
        if (eventDto.getEventDate() != null && eventDto.getEventDate().isBefore(LocalDateTime.now())) {
            throw new RequestException("Нельзя изменить дату на уже наступившую");
        }
        if (eventDto.getPaid() != null && eventDto.getPaid() != (adminUpdateEvent.getPaid())) {
            adminUpdateEvent.setPaid(eventDto.getPaid());
        }
        if (eventDto.getParticipantLimit() != null && !eventDto.getParticipantLimit().equals(adminUpdateEvent
                .getParticipantLimit())) {
            adminUpdateEvent.setParticipantLimit(eventDto.getParticipantLimit());
        }
        if (eventDto.getTitle() != null && !eventDto.getTitle().equals(adminUpdateEvent.getTitle())) {
            adminUpdateEvent.setTitle(eventDto.getTitle());
        }
        if (eventDto.getLocation() != null && eventDto.getLocation().getLatitude() != null &&
                !eventDto.getLocation().getLatitude().equals(adminUpdateEvent.getLatitude())) {
            adminUpdateEvent.setLatitude(eventDto.getLocation().getLatitude());
        }
        if (eventDto.getLocation() != null && eventDto.getLocation().getLongitude() != null &&
                !eventDto.getLocation().getLongitude().equals(adminUpdateEvent.getLongitude())) {
            adminUpdateEvent.setLongitude(eventDto.getLocation().getLongitude());
        }
        if (eventDto.getStateAction() != null && eventDto.getStateAction().equals("SEND_REVIEW")) {
            adminUpdateEvent.setState(EventState.PENDING);
        }
        if (eventDto.getStateAction() != null && eventDto.getStateAction().equals("REJECT_EVENT")) {
            adminUpdateEvent.setState(EventState.CANCELED);
        }
        if (eventDto.getStateAction() != null && eventDto.getStateAction().equals("PUBLISH_EVENT")) {
            adminUpdateEvent.setState(EventState.PUBLISHED);
        }
        return EventMapper.eventToEventFullDto(eventRepository.save(adminUpdateEvent),
                participationRepository.countByEvent_IdAndStatusContaining(adminUpdateEvent.getId(), "CONFIRMED"));
    }

    private List<ViewsStats> getViewsByEvent(LocalDateTime rangeStart, LocalDateTime rangeEnd, List<String> uris) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String start = rangeStart == null ? LocalDateTime.now().minusYears(5).format(formatter) : rangeStart.format(formatter);
        String end = rangeEnd == null ? LocalDateTime.now().plusSeconds(20).format(formatter) : rangeEnd.format(formatter);
        return statsClient.getViews(start, end, uris, false);
    }

    private void changeStatusOfParticipantToRejected(Long eventId) {
        List<Participation> rejectedRequests = participationRepository
                .findAllByEvent_IdAndStatusContaining(eventId, "PENDING");
        rejectedRequests.forEach(request -> request.setStatus("REJECTED"));
        participationRepository.saveAll(rejectedRequests);
    }
}
