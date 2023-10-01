package ru.practicum.explorewithme.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.dto.categories.CategoryDto;
import ru.practicum.explorewithme.dto.categories.NewCategoryDto;
import ru.practicum.explorewithme.exeption.RequestException;
import ru.practicum.explorewithme.service.CategoryService;

import java.util.Objects;

@RestController
@RequestMapping("/admin/categories")
@Slf4j
@RequiredArgsConstructor
public class CategoryAdminController {
    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<CategoryDto> addCategory(@RequestBody NewCategoryDto categoryDto) {
        if (Objects.isNull(categoryDto.getName())) {
            throw new RequestException("Поле name не должно быть пустым");
        }
        log.info("Создать новую категорию " + categoryDto.getName());
        CategoryDto categoryDtoRes = categoryService.addCategory(categoryDto);
        return new ResponseEntity<>(categoryDtoRes, HttpStatus.CREATED);
    }

    @PatchMapping("/{catId}")
    public CategoryDto updateCategory(@PathVariable Long catId, @RequestBody CategoryDto categoryDto) {
        if (Objects.requireNonNull(categoryDto.getName()).length() > 50) {
            throw new RequestException("В теле запроса не должно быть длинна более 50");
        }
        log.info("Обновить категорию " + categoryDto);
        return categoryService.updateCategory(catId, categoryDto);
    }

    @DeleteMapping("/{categoryId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable Long categoryId) {
        log.info("Удалить категорию " + categoryId);
        categoryService.deleteCategory(categoryId);
    }
}
