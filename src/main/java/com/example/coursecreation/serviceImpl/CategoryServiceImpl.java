package com.example.coursecreation.serviceImpl;

import com.example.coursecreation.dto.CategoryDto;
import com.example.coursecreation.exception.BadRequestException;
import com.example.coursecreation.exception.ResourceNotFoundException;
import com.example.coursecreation.mapper.CategoryMapper;
import com.example.coursecreation.model.Category;
import com.example.coursecreation.repository.CategoryRepository;
import com.example.coursecreation.response.CategoryNameResponse;
import com.example.coursecreation.response.CategoryResponse;
import com.example.coursecreation.service.CategoryService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository, CategoryMapper categoryMapper) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
    }

    @Override
    @Transactional
    public CategoryResponse createCategory(CategoryDto categoryDto) {
        Category category = categoryMapper.toCategory(categoryDto);
        if(categoryDto.getParent_id() != null) {
            category.setParentCategory(findCategoryById(categoryDto.getParent_id()));
        }

        return categoryMapper.toCategoryResponse(categoryRepository.save(category));
    }

    @Override
    @Transactional
    public CategoryResponse modifyCategoryName(Long id, CategoryDto categoryDto) {
        Category category = findCategoryById(id);

        category.setIcon(categoryDto.getIcon());
        category.setTitle(categoryDto.getTitle());
        if(categoryDto.getParent_id() != null) {
            category.setParentCategory(findCategoryById(categoryDto.getParent_id()));
        }
        return categoryMapper.toCategoryResponse(categoryRepository.save(category));
    }

    @Override
    public List<CategoryResponse> getCategoryByParentCategory(Long id) {
        Category category = findCategoryById(id);
        if (!category.getContainsCategories()) {
            throw new BadRequestException("this category has no sub categories");
        }
        return category.getChildCategories().stream()
                .map(categoryMapper::toCategoryResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<CategoryNameResponse> getCategoriesByChildCategory(Long id) {
        Category currentCategory = findCategoryById(id);
        List<CategoryNameResponse> parentCategories = new ArrayList<>();
        while (currentCategory.getParentCategory() != null) {
            currentCategory = currentCategory.getParentCategory();
            parentCategories.add(categoryMapper.toCategoryNameResponse(currentCategory));
        }
        return parentCategories;
    }

    private Category findCategoryById(Long id) {
        return categoryRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Category not found with the id: " +id)
        );
    }
}
