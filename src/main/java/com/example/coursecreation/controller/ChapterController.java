package com.example.coursecreation.controller;


import com.example.coursecreation.dto.ChapterDto;
import com.example.coursecreation.dto.ChapterNameDto;
import com.example.coursecreation.exception.UnauthorizedException;
import com.example.coursecreation.response.ChapterResponse;
import com.example.coursecreation.service.AuthService;
import com.example.coursecreation.service.ChapterService;
import com.example.coursecreation.service.TeacherService;
import com.example.coursecreation.util.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/chapter")
public class ChapterController {

    final
    ChapterService chapterService;

    final
    AuthService authService;

    final
    TeacherService teacherService;

    @Value("${auth.url}")
    private String authUrl;


    public ChapterController(ChapterService chapterService, AuthService authService, TeacherService teacherService) {
        this.chapterService = chapterService;
        this.authService = authService;
        this.teacherService = teacherService;
    }

    @PostMapping("/")
    public ResponseEntity<ApiResponse<ChapterResponse>> createChapter(@RequestBody ChapterDto chapterDto, @RequestHeader("Authorization") String token){

        if (!teacherService.teacherHasCourse(chapterDto.getCourse_id(),authService.getUserDetailsFromAuthService(authUrl,token).getEmail())){
            throw new UnauthorizedException("you are not the owner of this course");
        }

        ChapterResponse chapterResponse = chapterService.createChapter(chapterDto);
        return ResponseEntity.ok(new ApiResponse<>(true, "chapter  has been created ",chapterResponse));

    }


    @PutMapping("/")
    public ResponseEntity<ApiResponse<ChapterResponse>> modifyChapter(@RequestBody ChapterNameDto chapterNameDto, @RequestHeader("Authorization") String token){

        if (!teacherService.teacherHasCourse(chapterNameDto.getId(),authService.getUserDetailsFromAuthService(authUrl,token).getEmail())){
            throw new UnauthorizedException("you are not the owner of this course");
        }

        ChapterResponse chapterResponse = chapterService.modifyChapterName(chapterNameDto);
        return ResponseEntity.ok(new ApiResponse<>(true, "chapter  has been modified ",chapterResponse));

    }
}
