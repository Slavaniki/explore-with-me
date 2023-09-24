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

@RestController
@RequestMapping("/admin/categories")
@Slf4j
@RequiredArgsConstructor
public class CategoryAdminController {
    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<CategoryDto> addCategory(@RequestBody NewCategoryDto categoryDto) {
        if (categoryDto.getName() == null) {
            throw new RequestException("Поле name не должно быть пустым");
        }
        log.info("Создать новую категорию " + categoryDto.getName());
        CategoryDto categoryDtoRes = categoryService.addCategory(categoryDto);
        return new ResponseEntity<>(categoryDtoRes, HttpStatus.CREATED);
    }

    @PatchMapping
    public CategoryDto updateCategory(@RequestBody CategoryDto categoryDto) {
        if (categoryDto.getId() == null || categoryDto.getName() == null) {
            throw new RequestException("В теле запроса не должно быть пустых полей");
        }
        log.info("Обновить категорию " + categoryDto);
        return categoryService.updateCategory(categoryDto);
    }

    @DeleteMapping("/{categoryId}")
    public void deleteCategory(@PathVariable Long categoryId) {
        log.info("Удалить категорию " + categoryId);
        categoryService.deleteCategory(categoryId);
    }
}
