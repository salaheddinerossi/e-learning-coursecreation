package com.example.coursecreation.mapper;

import com.example.coursecreation.dto.ChapterDto;
import com.example.coursecreation.dto.ChapterNameDto;
import com.example.coursecreation.exception.ResourceNotFoundException;
import com.example.coursecreation.model.Chapter;
import com.example.coursecreation.repository.ChapterRepository;
import com.example.coursecreation.response.ChapterResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.AfterMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public abstract class ChapterMapper {

    @Autowired
    protected ChapterRepository chapterRepository;

    @Mapping(target = "parentChapter", ignore = true)
    public abstract Chapter toChapter(ChapterDto dto);

    public abstract ChapterNameDto toChapterNameDto(Chapter chapter);


    @Mapping(target = "parentChapter_id" , source = "parentChapter.id")
    public abstract ChapterResponse toChapterResponse(Chapter chapter);


    @AfterMapping
    protected void setParentChapter(ChapterDto dto, @MappingTarget Chapter chapter) {
        if (dto.getParent_id() != null) {
            chapter.setParentChapter(chapterRepository.findById(dto.getParent_id())
                    .orElseThrow(
                            ()-> new ResourceNotFoundException("chapter not found with the id: "+ dto.getParent_id())
                    ));
        }
    }
}
