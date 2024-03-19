package com.example.coursecreation.dto;

import lombok.Data;

@Data
public class ChapterDto {

    private String title;

    private Boolean containsChapters;

    private Long parent_id;

}
