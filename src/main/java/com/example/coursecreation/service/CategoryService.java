package com.example.coursecreation.service;

import com.example.coursecreation.dto.CategoryDto;
import com.example.coursecreation.response.CategoryNameResponse;
import com.example.coursecreation.response.CategoryResponse;

import java.util.List;

public interface CategoryService {

    CategoryResponse createCategory(CategoryDto categoryDto);

    CategoryResponse modifyCategoryName(Long id,CategoryDto categoryDto);

    List<CategoryResponse> getCategoryByParentCategory(Long id);

    List<CategoryNameResponse> getCategoriesByChildCategory(Long id);


}
