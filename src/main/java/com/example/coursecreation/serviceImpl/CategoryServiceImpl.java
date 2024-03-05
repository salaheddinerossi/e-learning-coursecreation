package com.example.coursecreation.serviceImpl;

import com.example.coursecreation.dto.CategoryDto;
import com.example.coursecreation.exception.CategoryNotFoundException;
import com.example.coursecreation.exception.NoChildCategoriesException;
import com.example.coursecreation.model.Category;
import com.example.coursecreation.repository.CategoryRepository;
import com.example.coursecreation.response.CategoryResponse;
import com.example.coursecreation.service.CategoryService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    final
    CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public void createCategory(CategoryDto categoryDto) {
        Category category = new Category();

        category.setIcon(categoryDto.getIcon());
        category.setTitle(categoryDto.getTitle());
        category.setContainsCategories(categoryDto.getContainsCategories());
        category.setParentCategory(findCategoryById(categoryDto.getParent_id()));

        categoryRepository.save(category);
    }

    @Override
    public void modifyCategoryName(Long id,CategoryDto categoryDto) {
        Category category = findCategoryById(id);

        category.setIcon(categoryDto.getIcon());
        category.setTitle(categoryDto.getTitle());
        category.setParentCategory(findCategoryById(categoryDto.getParent_id()));

        categoryRepository.save(category);

    }

    @Override
    public List<CategoryResponse> getCategoryByParentCategory(Long id) {
        Category category = findCategoryById(id);

        if (!category.getContainsCategories()){
            throw new NoChildCategoriesException();
        }

        List<Category> categories = category.getChildCategories();
        List<CategoryResponse> categoryResponses = new ArrayList<>();

        for (Category ChildCategory:categories){
            categoryResponses.add(setCategoryResponse(ChildCategory));
        }

        return categoryResponses;
    }

    public Category findCategoryById(Long id){
        return categoryRepository.findById(id).orElseThrow(
                CategoryNotFoundException::new
        );
    }

    CategoryResponse setCategoryResponse(Category category){
        CategoryResponse categoryResponse = new CategoryResponse();

        categoryResponse.setId(category.getId());
        categoryResponse.setIcon(category.getIcon());
        categoryResponse.setTitle(category.getTitle());

        return categoryResponse;
    }

}
