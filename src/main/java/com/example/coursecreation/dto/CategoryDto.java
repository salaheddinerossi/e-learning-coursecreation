package com.example.coursecreation.dto;

import lombok.Data;

@Data
public class CategoryDto {

    private Long parent_id;

    private String title;

    private Boolean containsCategories;

    private String icon;

}
