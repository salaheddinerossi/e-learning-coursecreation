package com.example.coursecreation.response;

import lombok.Data;

@Data
public class CategoryResponse {
    private Long id;

    private String title;

    private String icon;

    private String description;

    private Boolean containsCategories;
}
