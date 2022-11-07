package ru.practicum.main_service.categories;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.main_service.categories.dto.CategoryDto;
import ru.practicum.main_service.categories.model.Category;
import ru.practicum.main_service.exception.NotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public CategoryDto create(CategoryDto categoryDto) {
        Category category = CategoryConverter.toCategory(categoryDto);
        log.info("Save new category: {}", category);
        return CategoryConverter.toCategoryDto(categoryRepository.save(category));
    }

    @Override
    public CategoryDto getCategory(long id) {
        if (categoryRepository.findById(id).isEmpty()) {
            log.error("Error, validation failed. Category with given id does not exist: {}", id);
            throw new NotFoundException("Error, validation failed. Category with given id does not exist");
        } else {
            log.info("Find category with id: {} {}", id, categoryRepository.getReferenceById(id));
            return CategoryConverter.toCategoryDto(categoryRepository.getReferenceById(id));
        }
    }

    @Override
    public List<CategoryDto> getCategories(PageRequest pageRequest) {
        Page<Category> categories = categoryRepository.findAll(pageRequest);
        log.info("Find all existing categories: {}", categories);
        return categories.stream()
                .map(CategoryConverter::toCategoryDto)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryDto update(CategoryDto categoryDto) {
        Category category = categoryRepository.getReferenceById(categoryDto.getId());
        String name = categoryDto.getName();
        if (name != null) {
            category.setName(name);
        }
        log.info("Update category name: {}", category);
        return CategoryConverter.toCategoryDto(categoryRepository.save(category));
    }

    @Override
    public void delete(long id) {
        log.info("Delete category with id: {}", id);
        categoryRepository.deleteById(id);
    }
}
