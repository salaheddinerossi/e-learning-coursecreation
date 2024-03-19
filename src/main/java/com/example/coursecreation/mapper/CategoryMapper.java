package com.example.coursecreation.mapper;

import com.example.coursecreation.dto.CategoryDto;
import com.example.coursecreation.model.Category;
import com.example.coursecreation.response.CategoryNameResponse;
import com.example.coursecreation.response.CategoryResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CategoryMapper {


    CategoryDto toCategoryDto(Category category);

    @Mapping(target = "parentCategory", ignore = true)
    Category toCategory(CategoryDto dto);

    CategoryResponse toCategoryResponse(Category category);

    CategoryNameResponse toCategoryNameResponse(Category category);
}
