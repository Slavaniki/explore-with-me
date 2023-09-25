package ru.practicum.explorewithme.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explorewithme.dto.compilation.CompilationDto;
import ru.practicum.explorewithme.dto.compilation.NewCompilationDto;
import ru.practicum.explorewithme.dto.event.EventShortDto;
import ru.practicum.explorewithme.exeption.NotFoundException;
import ru.practicum.explorewithme.exeption.RequestException;
import ru.practicum.explorewithme.mapper.CompilationMapper;
import ru.practicum.explorewithme.mapper.EventMapper;
import ru.practicum.explorewithme.model.Compilation;
import ru.practicum.explorewithme.model.Event;
import ru.practicum.explorewithme.repository.CompilationRepository;
import ru.practicum.explorewithme.repository.EventRepository;
import ru.practicum.explorewithme.repository.ParticipationRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CompilationServiceImpl implements CompilationService {
    private final CompilationRepository compRepository;
    private final EventRepository eventRepository;
    private final ParticipationRepository participationRepository;

    @Override
    @Transactional
    public CompilationDto addCompilation(NewCompilationDto compilationDto) {
        if (compilationDto.getTitle() == null || compilationDto.getTitle().isBlank() ||
                compilationDto.getTitle().length() > 50) {
            throw new RequestException("Поле title не должно быть пустым или длиннее 50 символов");
        }
        List<EventShortDto> eventsDto = new ArrayList<>();
        List<Event> events = new ArrayList<>();
        if (compilationDto.getEvents() != null) {
            events = eventRepository.findAllById(compilationDto.getEvents());
            for (Event event : events) {
                eventsDto.add(EventMapper.eventToEventShortDto(event,
                        participationRepository.countByEvent_IdAndStatusContaining(event.getId(), "CONFIRMED")));
            }
        }
        return CompilationMapper.compilationToCompilationDto(compRepository.save(CompilationMapper
                .newCompilationDtoToCompilation(compilationDto, events)), eventsDto);
    }

    @Override
    @Transactional
    public void deleteCompilation(Long compId) {
        if (!compRepository.existsById(compId)) {
            throw new NotFoundException("Подборка с id " + compId + " не найдена");
        }
        compRepository.deleteById(compId);
    }

    @Override
    @Transactional
    public void removeAnEventFromCompilation(Long compId, Long eventId) {
        Compilation compilation = compRepository.findById(compId).orElseThrow(() ->
                new NotFoundException("Подборка с id " + compId + " не найдена"));
        if (!eventRepository.existsById(eventId)) {
            throw new NotFoundException("Событие с id " + eventId + " не найдено");
        }
        List<Event> events = compilation.getEvents().stream()
                .filter(event -> !event.getId().equals(eventId))
                .collect(Collectors.toList());
        compilation.setEvents(events);
        compRepository.save(compilation);
    }

    @Override
    @Transactional
    public CompilationDto updateCompilation(Long compId, NewCompilationDto newCompilationDto) {
        Compilation compilation = compRepository.findById(compId).orElseThrow(() ->
                new NotFoundException("Подборка с id " + compId + " не найдена"));
        if (newCompilationDto.getTitle().length() > 50) {
            throw new RequestException("Поле title не должно быть пустым или длиннее 50 символов");
        }
        Compilation newCompilation = compRepository.save(compilation);
        List<Long> ev = newCompilationDto.getEvents();
        List<EventShortDto> eventsDto = new ArrayList<>();
        if (ev != null) {
            List<Event> events = eventRepository.findAllById(ev);
            for (Event event : events) {
                eventsDto.add(EventMapper.eventToEventShortDto(event,
                        participationRepository.countByEvent_IdAndStatusContaining(event.getId(), "CONFIRMED")));
            }
        }
        return CompilationMapper.compilationToCompilationDto(newCompilation, eventsDto);
    }

    @Override
    @Transactional
    public void addEventToCompilation(Long compId, Long eventId, NewCompilationDto newCompilationDto) {
        Compilation compilation = compRepository.findById(compId).orElseThrow(() ->
                new NotFoundException("Подборка с id " + compId + " не найдена"));
        compilation.getEvents().add(eventRepository.findById(eventId).orElseThrow(() ->
                new NotFoundException("Событие с id " + eventId + " не найдено")));
        if (newCompilationDto.getTitle().length() > 50) {
            throw new RequestException("Поле title не должно быть пустым или длиннее 50 символов");
        }
        compRepository.save(compilation);
    }

    @Override
    public List<CompilationDto> getCompilations(Boolean pinned, int from, int size) {
        List<CompilationDto> compDto = new ArrayList<>();
        List<Compilation> compilations;
        if (pinned == null) {
            compilations = compRepository.findAll(PageRequest.of(from, size)).getContent();
            for (Compilation compilation : compilations) {
                List<EventShortDto> eventsDto = new ArrayList<>();
                List<Event> events = compilation.getEvents();
                for (Event event : events) {
                    eventsDto.add(EventMapper.eventToEventShortDto(event,
                            participationRepository.countByEvent_IdAndStatusContaining(event.getId(),
                                    "CONFIRMED")));
                }
                compDto.add(CompilationMapper.compilationToCompilationDto(compilation, eventsDto));
            }
        } else {
            compilations = compRepository.findAllByPinned(pinned, PageRequest.of(from, size)).getContent();
            for (Compilation compilation : compilations) {
                List<EventShortDto> eventsDto = new ArrayList<>();
                List<Event> events = compilation.getEvents();
                for (Event event : events) {
                    eventsDto.add(EventMapper.eventToEventShortDto(event,
                            participationRepository.countByEvent_IdAndStatusContaining(event.getId(),
                                    "CONFIRMED")));
                }
                compDto.add(CompilationMapper.compilationToCompilationDto(compilation, eventsDto));
            }
        }
        return compDto;
    }

    @Override
    public CompilationDto getCompilationById(Long compId) {
        Compilation compilation = compRepository.findById(compId).orElseThrow(() ->
                new NotFoundException("Подборка с id " + compId + " не найдена"));
        List<Event> events = compilation.getEvents();
        List<EventShortDto> eventsDto = new ArrayList<>();
        for (Event event : events) {
            eventsDto.add(EventMapper.eventToEventShortDto(event,
                    participationRepository.countByEvent_IdAndStatusContaining(event.getId(), "CONFIRMED")));
        }
        return CompilationMapper.compilationToCompilationDto(compilation, eventsDto);
    }
}
