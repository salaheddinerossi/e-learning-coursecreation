package com.example.coursecreation.controller;

import com.example.coursecreation.dto.CategoryDto;
import com.example.coursecreation.dto.UserDetailsDto;
import com.example.coursecreation.exception.UnauthorizedException;
import com.example.coursecreation.response.CategoryNameResponse;
import com.example.coursecreation.response.CategoryResponse;
import com.example.coursecreation.service.AuthService;
import com.example.coursecreation.service.CategoryService;
import com.example.coursecreation.util.ApiResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {

    @Value("${auth.url}")
    private String authUrl;


    final
    CategoryService categoryService;

    final
    AuthService authService;

    public CategoryController(CategoryService categoryService, AuthService authService) {
        this.categoryService = categoryService;
        this.authService = authService;
    }

    @PostMapping("/")
    public ResponseEntity<ApiResponse<CategoryResponse>>  createCategory(@RequestBody CategoryDto categoryDto,@RequestHeader("Authorization") String token){

        UserDetailsDto userDetailsDto = authService.getUserDetailsFromAuthService(authUrl,token);
        if (!authService.isAdmin(userDetailsDto.getRole())){
            throw new UnauthorizedException("you are not authorized to perform this action");
        }

        CategoryResponse categoryResponse = categoryService.createCategory(categoryDto);
        return ResponseEntity.ok(new ApiResponse<>(true, "Category created successfully",categoryResponse));

    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CategoryResponse>>  modifyCategory(@PathVariable Long id,@RequestBody CategoryDto categoryDto,@RequestHeader("Authorization") String token){

        UserDetailsDto userDetailsDto = authService.getUserDetailsFromAuthService(authUrl,token);
        if (!authService.isAdmin(userDetailsDto.getRole())){
            throw new UnauthorizedException("you are not authorized to perform this action");
        }

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
