package com.example.coursecreation.service;

import com.example.coursecreation.dto.CategoryDto;
import com.example.coursecreation.response.CategoryNameResponse;
import com.example.coursecreation.response.CategoryResponse;
import com.example.coursecreation.response.CategoryResponseWithParentId;
import com.example.coursecreation.response.SubCategoriesResponse;

import java.io.IOException;
import java.util.List;

public interface CategoryService {

    CategoryResponse createCategory(CategoryDto categoryDto) throws IOException;

    CategoryResponse modifyCategoryName(Long id,CategoryDto categoryDto) throws IOException;

    SubCategoriesResponse getCategoryByParentCategory(Long id);

    List<CategoryNameResponse> getCategoriesByChildCategory(Long id);

    List<CategoryResponse> getRootCategories();

    List<CategoryResponseWithParentId> getAllCategories();




}
