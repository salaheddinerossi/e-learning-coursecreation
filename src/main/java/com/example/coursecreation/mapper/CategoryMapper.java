package com.example.coursecreation.mapper;

import com.example.coursecreation.dto.CategoryDto;
import com.example.coursecreation.model.Category;
import com.example.coursecreation.response.CategoryNameResponse;
import com.example.coursecreation.response.CategoryResponse;
import com.example.coursecreation.response.CategoryResponseWithParentId;
import lombok.With;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoryMapper {



    @Mapping(target = "parentCategory", ignore = true)
    @Mapping(target = "icon", ignore = true)
    Category toCategory(CategoryDto dto);


    CategoryResponse toCategoryResponse(Category category);


    @Mapping(source = "category.parentCategory.id",target = "parentCategoryId" )
    CategoryResponseWithParentId toCategoryResponseWithParentId(Category category);



    List<CategoryResponseWithParentId> toCategoryResponseWithParentIdList(List<Category> categories);

    CategoryNameResponse toCategoryNameResponse(Category category);

    List<CategoryResponse> toCategoryResponseList(List<Category> categories);
}
