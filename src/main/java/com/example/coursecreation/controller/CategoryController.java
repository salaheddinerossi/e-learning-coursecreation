package com.example.coursecreation.controller;

import com.example.coursecreation.dto.CategoryDto;
import com.example.coursecreation.response.CategoryNameResponse;
import com.example.coursecreation.response.CategoryResponse;
import com.example.coursecreation.service.CategoryService;
import com.example.coursecreation.util.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {

    final
    CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping("/")
    public ResponseEntity<ApiResponse<CategoryResponse>>  createCategory(@RequestBody CategoryDto categoryDto){

        CategoryResponse categoryResponse = categoryService.createCategory(categoryDto);
        return ResponseEntity.ok(new ApiResponse<>(true, "Category created successfully",categoryResponse));

    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CategoryResponse>>  modifyCategory(@PathVariable Long id,@RequestBody CategoryDto categoryDto){

        CategoryResponse categoryResponse = categoryService.modifyCategoryName(id, categoryDto);
        return ResponseEntity.ok(new ApiResponse<>(true, "Category modified successfully",categoryResponse));

    }

    @GetMapping("/subCategories/{id}")
    public ResponseEntity<ApiResponse<List<CategoryResponse>>> getCategoryByParentCategory(@PathVariable Long id){

        List<CategoryResponse> categoryResponses = categoryService.getCategoryByParentCategory(id);
        return ResponseEntity.ok(new ApiResponse<>(true,"categories fetched successfully" , categoryResponses));

    }

    @GetMapping("/parentCategories/{id}")
    public ResponseEntity<ApiResponse<List<CategoryNameResponse>>> getParentCategories(@PathVariable Long id){

        List<CategoryNameResponse> categoryResponses = categoryService.getCategoriesByChildCategory(id);
        return ResponseEntity.ok(new ApiResponse<>(true,"categories fetched successfully" , categoryResponses));

    }





}
