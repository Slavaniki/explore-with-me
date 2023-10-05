package ru.practicum.explorewithme.mapper;

import lombok.experimental.UtilityClass;
import org.springframework.data.domain.Page;
import ru.practicum.explorewithme.dto.location.NewLocationDto;
import ru.practicum.explorewithme.dto.location.LocationDto;
import ru.practicum.explorewithme.model.Location;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class LocationMapper {

    public static Location newSpecificLocationToSpecificLocation(NewLocationDto locationDto) {
        Location location = new Location();
        location.setEvents(Collections.emptyList());
        location.setName(locationDto.getName());
        location.setLatitude(locationDto.getLatitude());
        location.setLongitude(locationDto.getLongitude());
        location.setRadius(locationDto.getRadius());
        return location;
    }

    public static LocationDto specificLocationToSpecificLocationDto(Location location) {
        LocationDto locationDto = new LocationDto();
        locationDto.setId(location.getId());
        locationDto.setEvents(location.getEvents());
        locationDto.setName(location.getName());
        locationDto.setRadius(location.getRadius());
        locationDto.setLongitude(location.getLongitude());
        locationDto.setLatitude(location.getLatitude());
        return locationDto;
    }

    public static List<LocationDto> locationsToLocationsDto(Page<Location> locations) {
        return locations.stream().map(LocationMapper::specificLocationToSpecificLocationDto).collect(Collectors.toList());
    }
}
