package ru.practicum.explorewithme.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.dto.compilation.CompilationDto;
import ru.practicum.explorewithme.dto.compilation.NewCompilationDto;
import ru.practicum.explorewithme.service.CompilationService;

@RestController
@RequestMapping("/admin/compilations")
@Slf4j
@RequiredArgsConstructor
public class CompilationAdminController {
    private final CompilationService compilationService;

    @PostMapping
    public ResponseEntity<CompilationDto> addCompilation(@RequestBody NewCompilationDto newCompilationDto) {
        log.info("Добавить newCompilationDto " + newCompilationDto);
        CompilationDto compilationDto = compilationService.addCompilation(newCompilationDto);
        return new ResponseEntity<>(compilationDto, HttpStatus.CREATED);
    }

    @DeleteMapping("/{compId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteCompilation(@PathVariable Long compId) {
        compilationService.deleteCompilation(compId);
        log.info("Удалить подборку по id " + compId);
    }

    @DeleteMapping("/{compId}/events/{eventId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteEventFromCompilation(@PathVariable Long compId, @PathVariable Long eventId) {
        compilationService.removeAnEventFromCompilation(compId, eventId);
        log.info("Удалить событие по id " + eventId + " из подборки по id " + compId);
    }

    @PatchMapping("/{compId}")
    public CompilationDto updateCompilation(@PathVariable Long compId, @RequestBody NewCompilationDto newCompilationDto) {
        log.info("Обновить подборку по id " + compId);
        return compilationService.updateCompilation(compId, newCompilationDto);
    }

    @PatchMapping("/{compId}/events/{eventId}")
    public void addEventToCompilation(@PathVariable Long compId, @PathVariable Long eventId,
                                      @RequestBody NewCompilationDto newCompilationDto) {
        compilationService.addEventToCompilation(compId, eventId, newCompilationDto);
        log.info("Добавить событие по id " + eventId + " из подборки по id " + compId);
    }
}
