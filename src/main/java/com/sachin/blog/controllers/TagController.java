package com.sachin.blog.controllers;


import com.sachin.blog.domain.dtos.CreateTagsRequest;
import com.sachin.blog.domain.dtos.TagResponse;
import com.sachin.blog.domain.entities.Tag;
import com.sachin.blog.mappers.TagMapper;
import com.sachin.blog.services.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/tags")
@RequiredArgsConstructor
public class TagController {

    private final TagMapper tagMapper;
    private final TagService tagService;
    @GetMapping
    public ResponseEntity<List<TagResponse>> getAllTags(){

        List<Tag> tags = tagService.getTags();

        List<TagResponse> tagResponses = tags.stream().map(
                tagMapper::toTagResponse
        ).toList();

        return ResponseEntity.ok(tagResponses);
    }

    @PostMapping
    public ResponseEntity<List<TagResponse>> createTags(@RequestBody CreateTagsRequest createTagsRequest){
        List<Tag> savedTags = tagService.createTags(createTagsRequest.getNames());

        List<TagResponse> createdTagResponses = savedTags.stream().map(tagMapper::toTagResponse).toList();

        return new ResponseEntity<>(
                createdTagResponses,
                HttpStatus.CREATED
        );
    }
}
