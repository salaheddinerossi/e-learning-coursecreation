package com.example.coursecreation.serviceImpl;

import com.example.coursecreation.dto.ChapterDto;
import com.example.coursecreation.dto.ChapterNameDto;
import com.example.coursecreation.exception.ResourceNotFoundException;
import com.example.coursecreation.mapper.ChapterMapper;
import com.example.coursecreation.model.Chapter;
import com.example.coursecreation.model.Course;
import com.example.coursecreation.repository.ChapterRepository;
import com.example.coursecreation.repository.CourseRepository;
import com.example.coursecreation.response.ChapterResponse;
import com.example.coursecreation.service.ChapterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChapterServiceImpl implements ChapterService {

    private final ChapterRepository chapterRepository;
    private final ChapterMapper chapterMapper;

    final
    CourseRepository courseRepository;

    @Autowired
    public ChapterServiceImpl(ChapterRepository chapterRepository, ChapterMapper chapterMapper, CourseRepository courseRepository) {
        this.chapterRepository = chapterRepository;
        this.chapterMapper = chapterMapper;
        this.courseRepository = courseRepository;
    }

    @Override
    public ChapterResponse createChapter(ChapterDto chapterDto) {
        Chapter chapter = chapterMapper.toChapter(chapterDto);

        chapter.setCourse(findCourseById(chapterDto.getCourse_id()));

        return chapterMapper.toChapterResponse(chapterRepository.save(chapter));
    }

    @Override
    public ChapterResponse modifyChapterName(ChapterNameDto chapterNameDto) {
        Chapter chapter = findChapterById(chapterNameDto.getId());
        chapter.setTitle(chapterNameDto.getTitle());

        return chapterMapper.toChapterResponse(chapterRepository.save(chapter));
    }

    @Override
    public List<ChapterNameDto> getChildChapters(Long id) {
        Chapter parentChapter = findChapterById(id);
        return chapterRepository.findByParentChapter(parentChapter).stream()
                .map(chapterMapper::toChapterNameDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ChapterNameDto> getParentChapters(Long id) {
        Chapter currentChapter = findChapterById(id);
        List<ChapterNameDto> parentChapters = new ArrayList<>();
        while (currentChapter.getParentChapter() != null) {
            currentChapter = currentChapter.getParentChapter();
            parentChapters.add(chapterMapper.toChapterNameDto(currentChapter));
        }
        return parentChapters;
    }

    private Chapter findChapterById(Long id) {
        return chapterRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("chapter not found with the id:"+ id)
        );
    }

    private Course findCourseById(Long id) {
        return courseRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("course not found with the id:"+ id)
        );
    }
}
