package com.example.coursecreation.response;


import lombok.Data;

@Data
public class CategoryResponseWithParentId {

    private long id;

    private String title;

    private String description;

    private Boolean containsCategories;

    private Long parentCategoryId;
}
