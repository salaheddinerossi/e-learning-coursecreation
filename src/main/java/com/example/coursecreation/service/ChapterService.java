package com.example.coursecreation.service;

import com.example.coursecreation.dto.ChapterDto;
import com.example.coursecreation.dto.ChapterNameDto;
import com.example.coursecreation.response.ChapterResponse;

import java.util.List;

public interface ChapterService {

    ChapterResponse createChapter(ChapterDto chapterDto);

    ChapterResponse modifyChapterName(Long id,ChapterNameDto chapterNameDto);

    void deleteChapter(Long id);


    List<ChapterNameDto> getChildChapters(Long id);

    List<ChapterNameDto> getParentChapters(Long id);


    ChapterResponse getChapterById(Long chapterId);
}
