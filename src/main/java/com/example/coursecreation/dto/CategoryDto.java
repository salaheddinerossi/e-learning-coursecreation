package com.example.coursecreation.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class CategoryDto {

    private Long parent_id;

    private String title;

    private Boolean containsCategories;

    private MultipartFile icon;

    private String description;

}
