package com.example.coursecreation.response;


import lombok.Data;

@Data
public class ChapterResponse {
    private Long id;

    private String title;

    private Boolean containsChapters;

    private Long parentChapter_id;
}
