package com.example.coursecreation.serviceImpl;

import com.example.coursecreation.dto.CategoryDto;
import com.example.coursecreation.exception.BadRequestException;
import com.example.coursecreation.exception.ResourceNotFoundException;
import com.example.coursecreation.mapper.CategoryMapper;
import com.example.coursecreation.model.Category;
import com.example.coursecreation.repository.CategoryRepository;
import com.example.coursecreation.response.CategoryNameResponse;
import com.example.coursecreation.response.CategoryResponse;
import com.example.coursecreation.response.CategoryResponseWithParentId;
import com.example.coursecreation.response.SubCategoriesResponse;
import com.example.coursecreation.service.CategoryService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {

    final
    StorageService storageService;
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository, CategoryMapper categoryMapper, StorageService storageService) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
        this.storageService = storageService;
    }

    @Override
    @Transactional
    public CategoryResponse createCategory(CategoryDto categoryDto) throws IOException {


        Category category = categoryMapper.toCategory(categoryDto);
        if(categoryDto.getParent_id() != null) {
            category.setParentCategory(findCategoryById(categoryDto.getParent_id()));
        }

        String fileName = generateUniqueFileName(Objects.requireNonNull(categoryDto.getIcon().getOriginalFilename()));
        String presignedUrl = storageService.generatePresignedUrl(fileName);
        storageService.uploadFileToS3(categoryDto.getIcon(), presignedUrl);

        category.setIcon(storageService.getFileUrl(fileName).toString());
        return categoryMapper.toCategoryResponse(categoryRepository.save(category));
    }

    @Override
    public CategoryResponse modifyCategoryName(Long id, CategoryDto categoryDto) throws IOException {

        Category category = findCategoryById(id);

        String fileName = generateUniqueFileName(Objects.requireNonNull(categoryDto.getIcon().getOriginalFilename()));
        String presignedUrl = storageService.generatePresignedUrl(fileName);
        storageService.uploadFileToS3(categoryDto.getIcon(), presignedUrl);

        category.setIcon(storageService.getFileUrl(fileName).toString());
        category.setTitle(categoryDto.getTitle());
        category.setDescription(categoryDto.getDescription());
        return categoryMapper.toCategoryResponse(categoryRepository.save(category));
    }

    @Override
    public SubCategoriesResponse getCategoryByParentCategory(Long id) {
        Category category = findCategoryById(id);
        if (!category.getContainsCategories()) {
            throw new BadRequestException("this category has no sub categories");
        }
        List<CategoryResponse> categoryResponses =  category.getChildCategories().stream()
                .map(categoryMapper::toCategoryResponse)
                .collect(Collectors.toList());

        SubCategoriesResponse subCategoriesResponse = new SubCategoriesResponse();
        subCategoriesResponse.setId(category.getId());
        subCategoriesResponse.setCategoryResponses(categoryResponses);
        subCategoriesResponse.setDescription(category.getDescription());
        subCategoriesResponse.setTitle(category.getTitle());

        return subCategoriesResponse;
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

    @Override
    public List<CategoryResponse> getRootCategories() {
        List<Category> rootCategories = new ArrayList<>();

        for (Category category:categoryRepository.findAll() ){

            if (category.getParentCategory()==null){
                rootCategories.add(category);
            }

        }



        return categoryMapper.toCategoryResponseList(rootCategories);

    }

    @Override
    public List<CategoryResponseWithParentId> getAllCategories() {

        List<Category> categories = categoryRepository.findAll();
        return categoryMapper.toCategoryResponseWithParentIdList(categories);
    }

    private Category findCategoryById(Long id) {
        return categoryRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Category not found with the id: " +id)
        );
    }

    private String generateUniqueFileName(String originalFileName) {
        String extension = "";
        int i = originalFileName.lastIndexOf('.');
        if (i > 0) {
            extension = originalFileName.substring(i);
        }
        return UUID.randomUUID().toString() + extension;
    }


}
