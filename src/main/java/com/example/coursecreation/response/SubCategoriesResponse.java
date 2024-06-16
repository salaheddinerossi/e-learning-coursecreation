package com.example.coursecreation.response;

import lombok.Data;

import java.util.List;


@Data
public class SubCategoriesResponse {

    private Long id;

    private String title;

    private String description;

    List<CategoryResponse> categoryResponses;
}
