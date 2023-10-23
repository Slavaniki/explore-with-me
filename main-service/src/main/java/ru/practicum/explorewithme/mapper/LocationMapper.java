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
        return Location.builder()
                .name(locationDto.getName())
                .events(Collections.emptyList())
                .latitude(locationDto.getLatitude())
                .longitude(locationDto.getLongitude())
                .radius(locationDto.getRadius())
                .build();
    }

    public static LocationDto specificLocationToSpecificLocationDto(Location location) {
        return LocationDto.builder()
                .id(location.getId())
                .name(location.getName())
                .events(location.getEvents())
                .latitude(location.getLatitude())
                .longitude(location.getLongitude())
                .radius(location.getRadius())
                .build();
    }

    public static List<LocationDto> locationsToLocationsDto(Page<Location> locations) {
        return locations.stream().map(LocationMapper::specificLocationToSpecificLocationDto).collect(Collectors.toList());
    }
}
