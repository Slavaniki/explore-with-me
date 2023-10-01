package ru.practicum.explorewithme.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.explorewithme.dto.compilation.CompilationDto;
import ru.practicum.explorewithme.dto.compilation.NewCompilationDto;
import ru.practicum.explorewithme.dto.event.EventShortDto;
import ru.practicum.explorewithme.model.Compilation;
import ru.practicum.explorewithme.model.Event;

import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class CompilationMapper {

    public static CompilationDto compilationToCompilationDto(Compilation compilation, List<EventShortDto> events) {
        CompilationDto compilationDto = new CompilationDto();
        compilationDto.setId(compilation.getId());
        compilationDto.setPinned(compilation.getPinned());
        compilationDto.setTitle(compilation.getTitle());
        compilationDto.setEvents(eventsShortDtoToCompilationsDtoEventShortDto(events));
        return compilationDto;
    }

    public static Compilation newCompilationDtoToCompilation(NewCompilationDto newCompilationDto, List<Event> events) {
        Compilation compilation = new Compilation();
        compilation.setEvents(events);
        compilation.setPinned(newCompilationDto.getPinned());
        compilation.setTitle(newCompilationDto.getTitle());
        return compilation;
    }

    public static EventShortDto eventShortDtoToCompilationDtoEventShortDto(EventShortDto dto) {
        EventShortDto eventShort = new EventShortDto();
        eventShort.setEventDate(dto.getEventDate());
        eventShort.setAnnotation(dto.getAnnotation());
        eventShort.setCategory(dto.getCategory());
        eventShort.setConfirmedRequests(dto.getConfirmedRequests());
        eventShort.setInitiator(dto.getInitiator());
        eventShort.setPaid(dto.getPaid());
        eventShort.setViews(dto.getViews());
        eventShort.setTitle(dto.getTitle());
        eventShort.setId(dto.getId());
        return eventShort;
    }

    public static List<EventShortDto> eventsShortDtoToCompilationsDtoEventShortDto(List<EventShortDto> dtos) {
        return dtos.stream().map(CompilationMapper::eventShortDtoToCompilationDtoEventShortDto)
                .collect(Collectors.toList());
    }
}
