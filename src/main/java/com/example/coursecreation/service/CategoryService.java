package com.example.coursecreation.service;

import com.example.coursecreation.dto.CategoryDto;
import com.example.coursecreation.response.CategoryResponse;

import java.util.List;

public interface CategoryService {

    void createCategory(CategoryDto categoryDto);

    void modifyCategoryName(Long id,CategoryDto categoryDto);

    List<CategoryResponse> getCategoryByParentCategory(Long id);

}
