package ru.practicum.explorewithme.service;

import ru.practicum.explorewithme.dto.categories.CategoryDto;
import ru.practicum.explorewithme.dto.categories.NewCategoryDto;

import java.util.List;

public interface CategoryService {

    CategoryDto addCategory(NewCategoryDto categoryDto);

    CategoryDto updateCategory(CategoryDto categoryDto);

    void deleteCategory(Long categoryId);

    List<CategoryDto> getAllCategories(int from, int size);

    CategoryDto getCategoryById(Long categoryId);
}
