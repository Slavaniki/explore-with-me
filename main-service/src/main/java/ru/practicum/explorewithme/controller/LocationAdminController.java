package ru.practicum.explorewithme.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.dto.location.NewLocationDto;
import ru.practicum.explorewithme.dto.location.LocationDto;
import ru.practicum.explorewithme.dto.location.UpdateLocationDto;
import ru.practicum.explorewithme.service.LocationService;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;

@RestController
@Slf4j
@RequestMapping("/admin/location")
@RequiredArgsConstructor
public class LocationAdminController {
    private final LocationService locationService;

    @PostMapping
    public LocationDto addLocation(@RequestBody @Valid NewLocationDto locationDto) {
        log.info("Добавить локацию " + locationDto);
        return locationService.addLocation(locationDto);
    }

    @PutMapping("/{locId}")
    public LocationDto updateLocation(@PathVariable Long locId,
                                      @RequestBody UpdateLocationDto locationDto) {
        log.info("Обновить локацию " + locationDto);
        return locationService.updateLocation(locId, locationDto);
    }

    @DeleteMapping("/{locId}")
    public void deleteLocation(@PathVariable Long locId) {
        locationService.deleteLocation(locId);
        log.info("Удалить локацию по id: " + locId);
    }

    @GetMapping
    public List<LocationDto> findLocations(@RequestParam(required = false) Set<Long> ids,
                                           @RequestParam int from,
                                           @RequestParam int size) {
        if (ids == null) {
            log.info("Получить все локации");
        } else {
            log.info("Получить локации с id: " + ids);
        }
        return locationService.findLocationsByIds(ids, from, size);
    }

    @GetMapping("/{locId}")
    public LocationDto getLocationById(@PathVariable Long locId) {
        log.info("Получить локацию по id: " + locId);
        return locationService.getLocationById(locId);
    }

    @PatchMapping("/{locId}/events/{eventId}")
    public LocationDto addEventToLocation(@PathVariable Long locId, @PathVariable Long eventId) {
        LocationDto locationDto = locationService.addEventToLocation(locId, eventId);
        log.info("Добавить событие с id " + eventId + " в локацию с id " + locId);
        return locationDto;
    }

    @DeleteMapping("/{locId}/events/{eventId}")
    public void deleteEventsToLocation(@PathVariable Long locId, @PathVariable Long eventId) {
        locationService.deleteEventFromLocation(locId, eventId);
        log.info("Удалить событие с id " + eventId + " из локации с id " + locId);
    }
}
