package ru.practicum.main_service.categories;

import org.springframework.data.domain.PageRequest;
import ru.practicum.main_service.categories.dto.CategoryDto;


import java.util.List;

public interface CategoryService {

    CategoryDto create(CategoryDto categoryDto);

    CategoryDto getCategory(long id);

    List<CategoryDto> getCategories(PageRequest pageRequest);

    CategoryDto update(CategoryDto categoryDto);

    void delete(long id);
}
