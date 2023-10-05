package ru.practicum.explorewithme.service;

import ru.practicum.explorewithme.dto.location.NewLocationDto;
import ru.practicum.explorewithme.dto.location.LocationDto;
import ru.practicum.explorewithme.dto.location.UpdateLocationDto;

import java.util.List;
import java.util.Set;

public interface LocationService {
    LocationDto addLocation(NewLocationDto locationDto);

    LocationDto updateLocation(Long locId, UpdateLocationDto locationDto);

    void deleteLocation(Long locationId);

    List<LocationDto> findLocationsByIds(Set<Long> ids, int from, int size);

    LocationDto getLocationById(Long id);

    LocationDto addEventToLocation(Long locId, Long eventId);

    void deleteEventFromLocation(Long locId, Long eventId);
}
