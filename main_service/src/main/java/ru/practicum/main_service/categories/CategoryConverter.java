package ru.practicum.main_service.categories;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.main_service.categories.dto.CategoryDto;
import ru.practicum.main_service.categories.dto.NewCategoryDto;
import ru.practicum.main_service.categories.model.Category;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CategoryConverter {

    public static NewCategoryDto toNewCategoryDto(Category category) {
        return new NewCategoryDto(
                category.getName());
    }

    public static CategoryDto toCategoryDto(Category category) {
        return new CategoryDto(
                category.getId(),
                category.getName());
    }

    public static Category toCategory(CategoryDto categoryDto) {
        return new Category(
                categoryDto.getId(),
                categoryDto.getName());
    }
}
