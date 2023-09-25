package ru.practicum.explorewithme.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explorewithme.dto.categories.CategoryDto;
import ru.practicum.explorewithme.dto.categories.NewCategoryDto;
import ru.practicum.explorewithme.exeption.EventsException;
import ru.practicum.explorewithme.exeption.NotFoundException;
import ru.practicum.explorewithme.exeption.RequestException;
import ru.practicum.explorewithme.mapper.CategoryMapper;
import ru.practicum.explorewithme.model.Category;
import ru.practicum.explorewithme.repository.CategoryRepository;
import ru.practicum.explorewithme.repository.EventRepository;

import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
@Transactional(readOnly = true)
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final EventRepository eventRepository;

    @Override
    @Transactional
    public CategoryDto addCategory(NewCategoryDto categoryDto) {
        if (categoryDto.getName().isBlank() || categoryDto.getName().length() > 50) {
            throw new RequestException("Количество символов имени не может быть больше 250 или пустым");
        }
        return CategoryMapper.categoryToCategoryDto(categoryRepository.save(CategoryMapper
                .newCategoryDtoToCategory(categoryDto)));
    }

    @Override
    @Transactional
    public CategoryDto updateCategory(Long categoryId, CategoryDto categoryDto) {
        if (!categoryRepository.existsById(categoryId)) {
            throw new NotFoundException("Категория с id " + categoryDto.getId() + " не найдена");
        }
        Category oldCategory = categoryRepository.getReferenceById(categoryId);
        if (oldCategory.getName().equals(categoryDto.getName())){
            return CategoryMapper.categoryToCategoryDto(oldCategory);
        } else {
            return CategoryMapper.categoryToCategoryDto(categoryRepository.save(CategoryMapper
                    .categoryDtoToCategory(categoryDto)));
        }
    }

    @Override
    @Transactional
    public void deleteCategory(Long categoryId) {
        if (!categoryRepository.existsById(categoryId)) {
            throw new NotFoundException("Категория с id " + categoryId + " не найдена");
        }
        Long numberOfRelatedEvents = eventRepository.countByCategory_Id(categoryId);
        if (numberOfRelatedEvents != 0) {
            throw new EventsException("Категорию нельзя удалить, так как с ней связанно " + numberOfRelatedEvents
                    + " событий");
        }
        categoryRepository.deleteById(categoryId);
    }

    @Override
    public List<CategoryDto> getAllCategories(int from, int size) {
        return CategoryMapper.categoriesToCategoriesDto(categoryRepository
                .findAll(PageRequest.of(from, size)).getContent());
    }

    @Override
    public CategoryDto getCategoryById(Long categoryId) {
        return CategoryMapper.categoryToCategoryDto(categoryRepository.findById(categoryId)
                .orElseThrow(() -> new NotFoundException("Категория с id " + categoryId + " не найдена")));
    }
}
