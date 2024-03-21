package com.example.coursecreation.controller;


import com.example.coursecreation.dto.ChapterDto;
import com.example.coursecreation.response.ChapterResponse;
import com.example.coursecreation.service.ChapterService;
import com.example.coursecreation.util.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/chapter")
public class ChapterController {

    final
    ChapterService chapterService;

    public ChapterController(ChapterService chapterService) {
        this.chapterService = chapterService;
    }

    @PostMapping("/")
    public ResponseEntity<ApiResponse<ChapterResponse>> createChapter(@RequestBody ChapterDto chapterDto){

        ChapterResponse chapterResponse = chapterService.createChapter(chapterDto);
        return ResponseEntity.ok(new ApiResponse<>(true, "chapter  has been created ",chapterResponse));

    }

}
