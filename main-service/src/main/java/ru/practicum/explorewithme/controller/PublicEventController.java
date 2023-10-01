package ru.practicum.explorewithme.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.client.Client;
import ru.practicum.explorewithme.client.EndpointHit;
import ru.practicum.explorewithme.dto.event.EventFullDto;
import ru.practicum.explorewithme.dto.event.EventShortDto;
import ru.practicum.explorewithme.service.EventService;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;

@RestController
@Slf4j
@RequestMapping("/events")
public class PublicEventController {
    private final EventService eventService;
    private final Client client;

    @Autowired
    public PublicEventController(EventService eventService, Client client) {
        this.eventService = eventService;
        this.client = client;
    }

    @GetMapping()
    public List<EventShortDto> getEventsWithFilter(@RequestParam(required = false) String text,
                                                   @RequestParam(required = false) Set<Long> categories,
                                                   @RequestParam(required = false) Boolean paid,
                                                   @RequestParam(required = false) @DateTimeFormat(pattern =
                                                           "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
                                                   @RequestParam(required = false) @DateTimeFormat(pattern =
                                                           "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
                                                   @RequestParam(defaultValue = "false") Boolean onlyAvailable,
                                                   @RequestParam(required = false) String sort,
                                                   @RequestParam(defaultValue = "0") int from,
                                                   @RequestParam(defaultValue = "10") int size,
                                                   HttpServletRequest request) {
        log.info("Получить события с фильтром");
        saveView(request.getRequestURI(), request.getRemoteAddr());
        return eventService.getPublishedEventsWithFilter(text, categories, paid, rangeStart, rangeEnd, onlyAvailable,
                sort, from, size, request);
    }

    @GetMapping("/{id}")
    public EventFullDto getFullInfoAboutEventBuId(@PathVariable Long id, HttpServletRequest request) {
        log.info("Получить полную информацию о событии по id " + id);
        saveView(request.getRequestURI(), request.getRemoteAddr());
        return eventService.getPublishedEventById(id, request);
    }

    private void saveView(String uri, String ip) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        EndpointHit hit = new EndpointHit();
        hit.setApp("main-service");
        hit.setIp(ip);
        hit.setUri(uri);
        hit.setTimestamp(LocalDateTime.now().format(formatter));
        client.postEndpointHit(hit);
    }
}
